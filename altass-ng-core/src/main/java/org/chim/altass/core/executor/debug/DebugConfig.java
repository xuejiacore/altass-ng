package org.chim.altass.core.executor.debug;

/**
 * Class Name: DebugConfig
 * Create Date: 2017/10/23 17:00
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DebugConfig {
    // Debug Name
    private String name = null;
    // 强制抛出异常
    private boolean throwError = false;
    // 运行时长，默认300ms
    private long delay = 300;
    // 异常描述
    private String exceptionDesc = null;

    private String outputJson = null;

    public boolean isThrowError() {
        return throwError;
    }

    public void setThrowError(boolean throwError) {
        this.throwError = throwError;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutputJson() {
        return outputJson;
    }

    public void setOutputJson(String outputJson) {
        this.outputJson = outputJson;
    }
}
