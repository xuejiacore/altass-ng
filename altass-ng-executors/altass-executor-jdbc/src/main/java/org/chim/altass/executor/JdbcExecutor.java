package org.chim.altass.executor;

import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.jdbc.DataSourceConfig;
import org.chim.altass.executor.jdbc.DataSourceManager;
import org.chim.altass.executor.jdbc.SqlConfig;
import org.chim.altass.toolkit.job.UpdateAnalysis;

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
    private DataSourceConfig dataSource = null;

    /**
     * sql的运行配置
     */
    @AltassAutowired
    private SqlConfig sqlConfig = null;

    private DataSourceManager dataSourceManager = null;

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
        if (this.dataSource == null) {
            throw new IllegalArgumentException("Not found data source configuration.");
        }

        dataSourceManager = new DataSourceManager(this.dataSource, this.sqlConfig);
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

    @Override
    public StreamData onStreamProcessing(byte[] data) throws ExecuteException {
        return null;
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
