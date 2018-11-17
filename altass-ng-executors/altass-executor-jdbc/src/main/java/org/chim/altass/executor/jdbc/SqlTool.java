package org.chim.altass.executor.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.chim.altass.datasource.sql.AltassSqlSessionFactory;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Class Name: SqlTool
 * Create Date: 11/17/18 3:07 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class SqlTool {

    /**
     * SqlSessionFactory
     */
    private AltassSqlSessionFactory altassSqlSessionFactory = null;

    private String id = null;

    private DruidDataSource dataSource = null;

    private String databaseId = null;

    public SqlTool(String id, DataSourceConfig dataSourceConfig) {
        // Config datasource
        dataSource = new DruidDataSource();
        this.databaseId = dataSourceConfig.getDatabaseId();
        dataSource.setUrl(dataSourceConfig.getUrl());
        dataSource.setUsername(dataSourceConfig.getUsername());
        dataSource.setPassword(dataSourceConfig.getPassword());
        dataSource.setInitialSize(dataSourceConfig.getInitialSize());
        dataSource.setMinIdle(dataSourceConfig.getMinIdle());
        dataSource.setMaxActive(dataSourceConfig.getMaxActive());
        dataSource.setMaxWait(dataSourceConfig.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(dataSourceConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(dataSourceConfig.getMinEvictableIdleTimeMillis());
        dataSource.setMaxEvictableIdleTimeMillis(dataSourceConfig.getMaxEvictableIdleTimeMillis());
        dataSource.setValidationQuery(dataSourceConfig.getValidationQuery());
        dataSource.setTestWhileIdle(dataSourceConfig.isTestWhileIdle());
        dataSource.setTestOnBorrow(dataSourceConfig.isTestOnBorrow());
        dataSource.setTestOnReturn(dataSourceConfig.isTestOnReturn());
        dataSource.setKeepAlive(dataSourceConfig.isKeepAlive());
        dataSource.setPhyMaxUseCount(dataSourceConfig.getPhyMaxUseCount());
        this.altassSqlSessionFactory = new AltassSqlSessionFactory(dataSource);
        try {
            this.altassSqlSessionFactory.build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.id = id;
    }

    public void prepareSql(SqlConfig sqlConfig) {
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(altassSqlSessionFactory
                .getConfiguration(), this.id);

        RawSqlSource rawSqlSource = new RawSqlSource(altassSqlSessionFactory.getConfiguration(), sqlConfig.getSql(), sqlConfig.getParameterType());
        assistant.addMappedStatement(
                this.id,
                rawSqlSource,
                StatementType.PREPARED,
                sqlConfig.getSqlCommandType(),
                Integer.MIN_VALUE,
                sqlConfig.getTimeout(),
                null,
                HashMap.class,
                null,
                sqlConfig.getResultType(),
                ResultSetType.FORWARD_ONLY,
                true,
                true,
                true,
                new NoKeyGenerator(),
                sqlConfig.getKeyProperty(),
                sqlConfig.getKeyColumn(),
                this.databaseId,
                new RawLanguageDriver());

    }

    public SqlSession openSession() throws SQLException {
        SqlSessionFactory sqlSessionFactory = this.altassSqlSessionFactory.getSqlSessionFactory();
        return sqlSessionFactory.openSession(dataSource.getConnection());
    }

    public void close(SqlSession sqlSession) {
        sqlSession.close();
    }

}
