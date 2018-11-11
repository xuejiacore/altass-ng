package org.chim.altass.core.executor.minirun;

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
}
