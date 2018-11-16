package org.chim.altass.executor.jdbc;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class Name: DataSource
 * Create Date: 11/13/18 11:49 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DataSourceManager {

    private DruidDataSource dataSource = null;
    //声明线程共享变量
    private ThreadLocal<Connection> container = new ThreadLocal<Connection>();

    private SqlConfig sqlConfig = null;

    public DataSourceManager(DataSourceConfig dataSourceConfig, SqlConfig sqlConfig) {
        dataSource = new DruidDataSource();

        dataSource.setUrl(dataSourceConfig.getUrl());
        dataSource.setUsername(dataSourceConfig.getUsername());
        dataSource.setPassword(dataSourceConfig.getPassword());
        dataSource.setInitialSize(dataSourceConfig.getInitialSize());
        dataSource.setMaxActive(dataSourceConfig.getMaxActive());
        dataSource.setMinIdle(dataSourceConfig.getMinIdle());
        dataSource.setMaxWait(dataSourceConfig.getMaxWait());
        dataSource.setValidationQuery(dataSourceConfig.getValidationQuery());
        dataSource.setTestOnBorrow(dataSourceConfig.isTestOnBorrow());
        dataSource.setTestWhileIdle(dataSourceConfig.isTestWhileIdle());

        this.sqlConfig = sqlConfig;
    }

    /**
     * 获取数据连接
     *
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            System.out.println(Thread.currentThread().getName() + "连接已经开启......");
            container.set(conn);
        } catch (Exception e) {
            System.out.println("连接获取失败");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 执行一个sql
     *
     * @param sqlRunner 需要执行的sql
     */
    public void run(SqlRunner sqlRunner) {
        this.startTransaction();
        try {
            sqlRunner.onSqlRunner(container.get());
            this.commit();
        } catch (Exception e) {
            this.rollback();
        } finally {
            this.close();
        }
    }

    /**
     * 获取当前线程上的连接开启事务
     */
    public void startTransaction() {
        Connection conn = container.get();//首先获取当前线程的连接
        if (conn == null) {//如果连接为空
            conn = getConnection();//从连接池中获取连接
            container.set(conn);//将此连接放在当前线程上
            System.out.println(Thread.currentThread().getName() + "空连接从dataSource获取连接");
        } else {
            System.out.println(Thread.currentThread().getName() + "从缓存中获取连接");
        }
        try {
            conn.setAutoCommit(false);//开启事务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 提交事务
    public void commit() {
        try {
            Connection conn = container.get();//从当前线程上获取连接if(conn!=null){//如果连接为空，则不做处理
            if (null != conn) {
                conn.commit();//提交事务
                System.out.println(Thread.currentThread().getName() + "事务已经提交......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***回滚事务*/
    public void rollback() {
        try {
            Connection conn = container.get();//检查当前线程是否存在连接
            if (conn != null) {
                conn.rollback();//回滚事务
                container.remove();//如果回滚了，就移除这个连接
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***关闭连接*/
    public void close() {
        try {
            Connection conn = container.get();
            if (conn != null) {
                conn.close();
                System.out.println(Thread.currentThread().getName() + "连接关闭");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                container.remove();//从当前线程移除连接切记
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
