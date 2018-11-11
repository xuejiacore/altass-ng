/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/10/9 15:15
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.config;


import org.chim.altass.base.ConfigHotSwapper;
import org.chim.altass.base.IConfSwap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Class Name: AbstractPropertiesUtil
 * Create Date: 2016/10/9 15:15
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 抽象的配置工具类
 */
public abstract class AbstractPropertiesUtil implements IConfSwap {

    protected static HashMap<String, Properties> properties = new HashMap<>();
    protected static HashMap<String, AbstractPropertiesUtil> propertiesUtils = new HashMap<>();

    private Class propertyClz = null;
    private String path = null;

    /**
     * 初始化一个配置管理工具类
     *
     * @param propertyClz 属性配置类的类名
     * @param path        配置文件的路径
     * @throws IOException
     */
    protected AbstractPropertiesUtil(Class propertyClz, String path) throws IOException {
        this.propertyClz = propertyClz;
        this.path = path;
        String clzName = propertyClz.getName();
        properties.put(clzName, new Properties());
        properties.get(clzName).load(propertyClz.getResourceAsStream(path));
        register(clzName);
    }

    /**
     * 获得配置的属性值
     *
     * @param propertyClzName 属性配置工具类的类名
     * @param key             需要获取的键值
     * @return 返回属性值
     */
    protected static String getValue(String propertyClzName, String key) {
        return propertiesUtils.get(propertyClzName).getProperties(propertyClzName).getProperty(key);
    }

    /**
     * 根据属性工具类的雷鸣获取属性实例
     *
     * @param propertyClzName 属性工具类的类名
     * @return 返回属性实例
     */
    public Properties getProperties(String propertyClzName) {
        return properties.get(propertyClzName);
    }

    /**
     * @return
     */
    @Override
    public boolean hotSwap() {
        try {
            properties.get(propertyClz.getName()).load(propertyClz.getResourceAsStream(path));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void register(String tag) {
        ConfigHotSwapper.add2Monitor(tag, this);
    }
}
