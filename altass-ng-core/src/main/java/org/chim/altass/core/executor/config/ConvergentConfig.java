package org.chim.altass.core.executor.config;

import java.util.Map;

/**
 * Class Name: ConvergentConfig
 * Create Date: 11/29/18 8:51 AM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ConvergentConfig {

    private boolean autoArray = false;

    private Map<String, String> mergeMap = null;

    public boolean isAutoArray() {
        return autoArray;
    }

    public void setAutoArray(boolean autoArray) {
        this.autoArray = autoArray;
    }

    public Map<String, String> getMergeMap() {
        return mergeMap;
    }

    public void setMergeMap(Map<String, String> mergeMap) {
        this.mergeMap = mergeMap;
    }
}
