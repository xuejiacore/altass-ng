package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.core.executor.minirun.MiniRunnable;

import java.util.Map;

/**
 * Class Name: StartNodeConfig
 * Create Date: 11/10/18 10:05 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class StartNodeConfig {

    private Class<? extends MiniRunnable> runnableClz = null;

    private Map<String, Object> runnableParamMap = null;

    public Class<? extends MiniRunnable> getRunnableClz() {
        return runnableClz;
    }

    public void setRunnableClz(Class<? extends MiniRunnable> runnableClz) {
        this.runnableClz = runnableClz;
    }

    public Map<String, Object> getRunnableParamMap() {
        return runnableParamMap;
    }

    public void setRunnableParamMap(Map<String, Object> runnableParamMap) {
        this.runnableParamMap = runnableParamMap;
    }
}
