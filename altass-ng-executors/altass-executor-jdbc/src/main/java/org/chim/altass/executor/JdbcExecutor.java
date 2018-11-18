package org.chim.altass.executor;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.SqlSession;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.jdbc.DataSourceConfig;
import org.chim.altass.executor.jdbc.SqlConfig;
import org.chim.altass.executor.jdbc.SqlTool;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
 * jdbc executor
 */
@Executable(name = "jdbcExecutor", assemble = true)
@Resource(
        name = "DBConnector", clazz = JdbcExecutor.class, midImage = "res/images/executor/jdbc_bg.png",
        pageUrl = "nodeConfigs/ext/jdbcNodeConfig.jsp",
        readme = "META-INF/org.chim.altass.executor.JdbcExecutor.md"
)
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

    /**
     * sql execute tool
     */
    private SqlTool sqlTool = null;

    /**
     * previous input params
     */
    private Map<String, Object> inputParseParams;

    /**
     * output key name
     */
    private static final String KEY_AFFECTED_ROWS = "_affected_rows";

    /**
     * To initialized executor
     *
     * @param executeId execute id
     */
    public JdbcExecutor(String executeId) throws ExecuteException {
        super(executeId);
        this.inputParseParams = new HashMap<String, Object>();
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

        InputParam inputParam = entry.getInputParam();
        if (inputParam == null) {
            return true;
        }
        List<MetaData> params = inputParam.getParams();

        String field;
        for (MetaData param : params) {
            field = param.getField();
            this.inputParseParams.put(field.replace("$$", ""), param.getValue());
        }
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
        Map<String, Object> previousOutput = JSON.parseObject(String.valueOf(streamData.getData()), Map.class);
        this.processSqlScript(previousOutput, true);
    }

    /**
     * process sql script
     *
     * @param previousOutput previous output parameters
     * @param isStreaming    true if is streaming process
     */
    private void processSqlScript(Map<String, Object> previousOutput, boolean isStreaming) throws ExecuteException {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlTool.openSession();

            switch (this.sqlConfig.getSqlCommandType()) {
                case SELECT:
                    // Select Sql Command
                    this.doSelectCmd(sqlSession, previousOutput, isStreaming);
                    break;
                case INSERT:
                    // Insert Sql Command
                    this.doInsertCmd(sqlSession, previousOutput, isStreaming);
                    break;
                case DELETE:
                    // Delete Sql Command
                    this.doDeleteCmd(sqlSession, previousOutput, isStreaming);
                    break;
                case UPDATE:
                    // Update Sql Command
                    this.doUpdateCmd(sqlSession, previousOutput, isStreaming);
                    break;
                case UNKNOWN:
                    throw new ExecuteException(new UnsupportedOperationException("Unknown Sql Command Type"));
            }
        } catch (SQLException e) {
            throw new ExecuteException(e);
        } finally {
            if (sqlSession != null) {
                // Close Sql Session
                sqlTool.close(sqlSession);
            }
            if (isStreaming) {
                postFinished();
            }
        }
    }

    /**
     * execute insert sql script
     *
     * @param sqlSession     sql session
     * @param previousOutput previous output parameters
     * @param isStreaming    true if is streaming, else false
     */
    private void doInsertCmd(SqlSession sqlSession, Map<String, Object> previousOutput, boolean isStreaming)
            throws ExecuteException {

        // int The number of rows affected by the insert.
        int insertResult = sqlSession.insert(this.executeId, previousOutput);
        previousOutput.put(KEY_AFFECTED_ROWS, insertResult);
        pushData(new StreamData(this.executeId, null, previousOutput));
    }

    /**
     * execute select sql script
     *
     * @param sqlSession     sql session
     * @param previousOutput previous output parameters
     * @param isStreaming    true if is streaming, else false
     */
    private void doSelectCmd(SqlSession sqlSession, Map<String, Object> previousOutput, boolean isStreaming) {
        sqlSession.select(this.executeId, previousOutput, context -> {
            Object resultObject = context.getResultObject();
            try {
                pushData(new StreamData(this.executeId, null, resultObject));
            } catch (ExecuteException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * execute update sql script
     *
     * @param sqlSession     sql session
     * @param previousOutput previous output parameters
     * @param isStreaming    true if is streaming, else false
     */
    private void doUpdateCmd(SqlSession sqlSession, Map<String, Object> previousOutput, boolean isStreaming)
            throws ExecuteException {

        // int The number of rows affected by the update.
        int updateResult = sqlSession.update(this.executeId, previousOutput);
        previousOutput.put(KEY_AFFECTED_ROWS, updateResult);
        pushData(new StreamData(this.executeId, null, previousOutput));
    }

    /**
     * execute delete sql script
     *
     * @param sqlSession     sql session
     * @param previousOutput previous output parameters
     * @param isStreaming    true if is streaming, else false
     */
    private void doDeleteCmd(SqlSession sqlSession, Map<String, Object> previousOutput, boolean isStreaming)
            throws ExecuteException {

        // int The number of rows affected by the delete.
        int deleteResult = sqlSession.delete(this.executeId, previousOutput);
        previousOutput.put(KEY_AFFECTED_ROWS, deleteResult);
        pushData(new StreamData(this.executeId, null, previousOutput));
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        throw new ExecuteException(new UnsupportedOperationException());
//        processSqlScript(this.inputParseParams, false);
//        return true;
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }
}
