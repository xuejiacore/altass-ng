/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.centers.log
 * Author: Xuejia
 * Date Time: 2016/12/16 14:40
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.log;

import java.util.Date;

/**
 * Class Name: Logger
 * Create Date: 2016/12/16 14:40
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class Logger implements ILogger{

    private String loggerName = null;

    public Logger(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public void info(String msg) {
        System.out.println("[" + new Date() + "] [" + loggerName + "] - " + msg);
    }

    @Override
    public void error(String msg) {
        System.err.println("[" + new Date() + "] [" + loggerName + "] - " + msg);
    }
}
