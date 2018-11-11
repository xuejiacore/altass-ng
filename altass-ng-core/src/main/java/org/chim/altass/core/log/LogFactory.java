/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.centers.log
 * Author: Xuejia
 * Date Time: 2016/12/16 14:37
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: Log
 * Create Date: 2016/12/16 14:37
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class LogFactory {

    private static Map<String, ILogger> loggerMap = new ConcurrentHashMap<>();

    public static ILogger getInstance(Class<?> clazz) {
        ILogger logger;
        String className = clazz.getName();
        if ((logger = loggerMap.get(className)) == null) {
            synchronized (LogFactory.class) {
                if ((logger = loggerMap.get(className)) == null) {
                    logger = new Logger(className);
                    loggerMap.put(className, logger);
                }
            }
        }
        return logger;
    }

}
