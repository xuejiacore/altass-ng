/**
 * Project: x-framework
 * Package Name: org.ike.core.config
 * Author: Xuejia
 * Date Time: 2016/11/20 19:30
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.config;

import org.chim.altass.base.exception.ConfigurationException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * Class Name: Configuration
 * Create Date: 2016/11/20 19:30
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 配置文件读取类
 */
public class XmlConfiguration {

    private Logger logger = LoggerFactory.getLogger(XmlConfiguration.class);
    private static XmlConfiguration xmlConfiguration = null;                                    // 配置读取
    private String configPath = null;                                                           // 核心框架文件的配置路径
    private Element root = null;                                                                // 核心框架文件的根节点配置

    public static final String CONFIG_K_FRAMEWORK_CORE_APP_NAME = "CORE_APP_NAME";              // 核心框架的名称
    public static final String CONFIG_K_ZOOKEEPER = "ZOOKEEPER";                                // Zookeeper
    public static final String CONFIG_K_ZOOKEEPER_IP = "ZOOKEEPER_IP";                          // Zookeeper的IP
    public static final String CONFIG_K_ZOOKEEPER_PORT = "ZOOKEEPER_PORT";                      // Zookeeper的PORT
    public static final String CONFIG_K_ZOOKEEPER_MODE = "ZOOKEEPER_MODE";                      // Zookeeper的节点建立模式0/1
    public static final String CONFIG_K_LOGIN_SESSION = "LOGIN_SESSION";                        // 当前用户登录的Session存储键

    private static HashMap<String, Object> configMap = new HashMap<>();                         // 配置全局存储池

    /**
     * 根据当前的servlet context获得一个xmlConfiguration实例
     *
     * @param servletContext servlet context
     * @return 返回xml configuration实例
     */
    public static XmlConfiguration getInstance(ServletContext servletContext) {
        if (xmlConfiguration == null) {
            synchronized (XmlConfiguration.class) {
                if (xmlConfiguration == null) {
                    xmlConfiguration = new XmlConfiguration(servletContext);
                }
            }
        }
        return xmlConfiguration;
    }

    /**
     * 根据文件的路径获得一个xmlConfiguration实例
     *
     * @param path 配置文件的路径
     * @return 返回xml configuration
     */
    public static XmlConfiguration getInstance(String path) {
        if (xmlConfiguration == null) {
            synchronized (XmlConfiguration.class) {
                if (xmlConfiguration == null) {
                    xmlConfiguration = new XmlConfiguration(path);
                }
            }
        }
        return xmlConfiguration;
    }

    /**
     * XML configuration构造函数
     *
     * @param servletContext servlet context
     */
    public XmlConfiguration(ServletContext servletContext) {
        String configPathVal = servletContext.getInitParameter("FrameworkConfigPath");
        if (configPathVal.startsWith("classpath:")) {
            configPathVal = "/WEB-INF/classes/" + configPathVal.substring(configPathVal.substring(10).startsWith("/") ? 11 : 10);
        }
        this.configPath = servletContext.getRealPath(configPathVal);
    }

    /**
     * 根据配置文件的路径创建一个xml configuration实例
     *
     * @param configPath xml配置文件的路径
     */
    public XmlConfiguration(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 加载配置文件
     *
     * @return 如果加载成功，返回值为true，否则返回值为false
     */
    public boolean loadConfig() throws ConfigurationException {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.configPath);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(fileInputStream);
            root = document.getRootElement();
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Exception throw when read the config file, please check whether config path correcting!", e);
        } catch (DocumentException e) {
            throw new ConfigurationException("Could not parse the config file, please check whether config is valid!", e);
        }

        // 读取基本配置
        readSystemNode();
        return true;
    }

    /**
     * 读取系统配置节点
     */
    public void readSystemNode() {
        Element systemEle = root.element("system");
        Element appEle = systemEle.element("app");
        Element helpersEle = appEle.element("helpers");
        List helperList = helpersEle.elements("helper");

        // 遍历支持配置
        for (Object element : helperList) {
            Element helperNameEle = ((Element) element).element("helper-name");
            Element helperValue = ((Element) element).element("helper-value");
            Element enableEle = ((Element) element).element("enable");
            String helperName = helperNameEle.getStringValue();
            String enableStr = enableEle.getStringValue();

            if ("Zookeeper".equals(helperName) && "true".equals(enableStr)) {

                // 初始化Zookeeper的配置
                for (Object valEle : helperValue.elements("value")) {
                    Attribute attr = ((Element) valEle).attribute(0);
                    String attrName = attr.getName();
                    String value = attr.getStringValue();
                    if ("ip".equals(attrName)) {
                        configMap.put(CONFIG_K_ZOOKEEPER_IP, value);
                    } else if ("port".equals(attrName)) {
                        configMap.put(CONFIG_K_ZOOKEEPER_PORT, value);
                    } else if ("mode".equals(attrName)) {
                        configMap.put(CONFIG_K_ZOOKEEPER_MODE, value);
                    }
                }
            } else if ("LoginSession".equals(helperName) && "true".equals(enableStr)) {

                // 登陆Session配置
                List values = helperValue.elements("value");
                configMap.put(CONFIG_K_LOGIN_SESSION, ((Element) values.get(0)).attribute(0).getStringValue());
            }
        }
        logger.info("核心框架配置文件读取解析：" + configMap.toString());
    }

    /**
     * 获得当前系统的配置池
     *
     * @return 返回配置池中的数据
     */
    public HashMap<String, Object> getConfigMap() {
        return configMap;
    }
}
