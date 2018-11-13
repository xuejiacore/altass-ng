package org.chim.altass.core.executor.minirun;

import org.chim.altass.core.executor.config.ColumnConfig;
import org.chim.altass.core.domain.buildin.attr.CommonStreamConfig;
import org.chim.altass.core.domain.buildin.attr.FileStreamConfig;
import org.chim.altass.core.domain.buildin.attr.StartNodeConfig;
import org.chim.altass.core.exception.ExecuteException;

import java.util.Map;

/**
 * Class Name: MiniRunnable
 * Create Date: 11/10/18 10:06 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Abstract Mini Runnable
 * <p>
 * As StartNode's child executable
 */
public abstract class MiniRunnable {

    public interface DataCallback {

        void onDataFlush(Object data) throws ExecuteException;

    }

    /**
     * running parameters map
     */
    protected Map<String, Object> runParamMap = null;

    /**
     * callback interface
     */
    protected DataCallback dataCallback = null;

    protected StartNodeConfig startNodeConfig = null;

    // File stream configuration
    protected FileStreamConfig fileStreamConfig = null;

    // Column split rule configuration
    protected ColumnConfig columnConfig = null;

    // Basic common stream configuration
    protected CommonStreamConfig commonStreamConfig = null;

    public abstract void run() throws ExecuteException;

    public Map<String, Object> getRunParamMap() {
        return runParamMap;
    }

    public void setRunParamMap(Map<String, Object> runParamMap) {
        this.runParamMap = runParamMap;
    }

    public DataCallback getDataCallback() {
        return dataCallback;
    }

    public void setDataCallback(DataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    public StartNodeConfig getStartNodeConfig() {
        return startNodeConfig;
    }

    public void setStartNodeConfig(StartNodeConfig startNodeConfig) {
        this.startNodeConfig = startNodeConfig;
    }

    public FileStreamConfig getFileStreamConfig() {
        return fileStreamConfig;
    }

    public void setFileStreamConfig(FileStreamConfig fileStreamConfig) {
        this.fileStreamConfig = fileStreamConfig;
    }

    public ColumnConfig getColumnConfig() {
        return columnConfig;
    }

    public void setColumnConfig(ColumnConfig columnConfig) {
        this.columnConfig = columnConfig;
    }

    public CommonStreamConfig getCommonStreamConfig() {
        return commonStreamConfig;
    }

    public void setCommonStreamConfig(CommonStreamConfig commonStreamConfig) {
        this.commonStreamConfig = commonStreamConfig;
    }
}
