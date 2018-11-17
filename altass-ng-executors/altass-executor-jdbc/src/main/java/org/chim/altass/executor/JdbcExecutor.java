package org.chim.altass.executor;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.SqlSession;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.jdbc.DataSourceConfig;
import org.chim.altass.executor.jdbc.SqlConfig;
import org.chim.altass.executor.jdbc.SqlTool;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.sql.SQLException;
import java.util.Map;

/**
 * Class Name: JdbcExecutor
 * Create Date: 11/13/18 11:33 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * jdbc执行器
 */
@Executable(name = "jdbcExecutor", assemble = true)
@Resource(name = "DBConnector", clazz = JdbcExecutor.class, midImage = "res/images/executor/jdbc_bg.png", pageUrl = "nodeConfigs/ext/jdbcNodeConfig.jsp")
public class JdbcExecutor extends AbstractStreamNodeExecutor {

    /**
     * 数据源配置
     */
    @AltassAutowired
    private DataSourceConfig dataSourceConfig = null;

    /**
     * sql的运行配置
     */
    @AltassAutowired
    private SqlConfig sqlConfig = null;

    private SqlTool sqlTool = null;

    /**
     * To initialized executor
     *
     * @param executeId execute id
     */
    public JdbcExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    protected boolean onChildInit() throws ExecuteException {
        if (this.dataSourceConfig == null) {
            throw new IllegalArgumentException("Not found data source configuration.");
        }

        if (this.sqlConfig == null) {
            throw new IllegalArgumentException("Not found sql configuration.");
        }

        // dataSource.setFilters(this.dataSourceConfig.getFilters());
        sqlTool = new SqlTool(this.executeId, this.dataSourceConfig);

        // prepare sql that will be execute
        sqlTool.prepareSql(this.sqlConfig);
        return true;
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStreamProcessing(byte[] data) throws ExecuteException {
        StreamData streamData = transformData(data);
        Map<String, Object> params = JSON.parseObject(String.valueOf(streamData.getData()), Map.class);

        SqlSession sqlSession = null;
        try {
            sqlSession = sqlTool.openSession();

            sqlSession.select(this.executeId, params, context -> {
                try {
                    Object resultObject = context.getResultObject();
                    pushData(new StreamData(this.executeId, null, resultObject));
                } catch (ExecuteException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            throw new ExecuteException(e);
        } finally {
            if (sqlSession != null) {
                sqlTool.close(sqlSession);
            }
            postFinished();
        }
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        return false;
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }
}
