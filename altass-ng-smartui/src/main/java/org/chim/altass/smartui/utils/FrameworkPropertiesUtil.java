/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/11/18 19:27
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.utils;


import org.chim.altass.base.utils.config.AbstractPropertiesUtil;

import java.io.IOException;

/**
 * Class Name: FrameworkPropertiesUtil
 * Create Date: 2016/11/18 19:27
 * Creator: Xuejia
 * Version: v1.0
 * Updater:詹学佳
 * Date Time:2016-11-18 19:27:43
 * Description:
 * 框架配置读取类
 */
public class FrameworkPropertiesUtil extends AbstractPropertiesUtil {

    // 框架配置文件路径
    private static final String PROPERTY_PATH = "/framework.properties";

    /**
     * zookeeper的IP地址
     */
    public static final String PROPERTY_ZOOKEEPER_IP = "ZOOKEEPER_ADDRESS";
    /**
     * zookeeper的端口号
     */
    public static final String PROPERTY_ZOOKEEPER_PORT = "ZOOKEEPER_PORT";


    static {
        try {
            propertiesUtils.put(FrameworkPropertiesUtil.class.getName(),
                    new FrameworkPropertiesUtil(FrameworkPropertiesUtil.class, PROPERTY_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化一个配置管理工具类
     *
     * @param propertyClz 属性配置类的类名
     * @param path        配置文件的路径
     * @throws IOException
     */
    protected FrameworkPropertiesUtil(Class propertyClz, String path) throws IOException {
        super(propertyClz, path);
    }

    /**
     * @param key 配置的键值
     * @return 返回配置值
     */
    public static String getValue(String key) {
        return getValue(FrameworkPropertiesUtil.class.getName(), key);
    }
}
