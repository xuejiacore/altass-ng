/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/10/9 16:09
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.config;

import java.io.IOException;

/**
 * Class Name: PropertiesUtil
 * Create Date: 2016/10/9 16:09
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class PropertiesUtil extends AbstractPropertiesUtil {

    private static final String PROPERTY_PATH = "/configt.properties";

    static {
        try {
            propertiesUtils.put(PropertiesUtil.class.getName(), new PropertiesUtil(PropertiesUtil.class, PROPERTY_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected PropertiesUtil(Class propertyClz, String path) throws IOException {
        super(propertyClz, path);
    }

    public static String getValue(String key) {
        return getValue(PropertiesUtil.class.getName(), key);
    }
}
