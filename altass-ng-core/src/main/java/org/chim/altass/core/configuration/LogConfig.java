package org.chim.altass.core.configuration;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: LogConfig
 * Create Date: 2017/9/6 21:04
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "log-config")
public class LogConfig {

    @Attr(alias = "exeLogDir")
    private String executorLogDir = null;

    @Attr(alias = "sysLogDir")
    private String systemLogDir = null;

    public String getExecutorLogDir() {
        return executorLogDir;
    }

    public void setExecutorLogDir(String executorLogDir) {
        this.executorLogDir = executorLogDir;
    }

    public String getSystemLogDir() {
        return systemLogDir;
    }

    public void setSystemLogDir(String systemLogDir) {
        this.systemLogDir = systemLogDir;
    }
}
