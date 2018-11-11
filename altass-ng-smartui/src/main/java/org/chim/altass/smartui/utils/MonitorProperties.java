/**
 * Project: x-framework
 * Package Name: org.ike.monitor.utils
 * Author: Xuejia
 * Date Time: 2016/10/9 16:56
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.utils;

import org.chim.altass.base.utils.config.AbstractPropertiesUtil;

import java.io.IOException;

/**
 * Class Name: MonitorProperties
 * Create Date: 2016/10/9 16:56
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 监视器配置
 */
public class MonitorProperties extends AbstractPropertiesUtil {
    private static final String PROPERTY_PATH = "/config_monitor.properties";

    static {
        try {
            propertiesUtils.put(MonitorProperties.class.getName(), new MonitorProperties(MonitorProperties.class, PROPERTY_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected MonitorProperties(Class propertyClz, String path) throws IOException {
        super(propertyClz, path);
    }

    public static String getValue(String key) {
        return getValue(MonitorProperties.class.getName(), key);
    }
}
