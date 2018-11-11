/**
 * Project: x-framework
 * Package Name: org.ike.core.listener
 * Author: Xuejia
 * Date Time: 2016/11/20 21:05
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.listener;

import org.chim.altass.base.config.XmlConfiguration;
import org.chim.altass.base.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Class Name: FrameworkListener
 * Create Date: 2016/11/20 21:05
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 框架级别配置加载，该配置加载主要是加载框架基础支持所需的所有支持项目的支持配置
 * <p>
 * 目前使用该监听器加载了框架级的配置文件
 */
public class FrameworkListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(FrameworkListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        XmlConfiguration xmlConfiguration = XmlConfiguration.getInstance(servletContextEvent.getServletContext());
        try {
            xmlConfiguration.loadConfig();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            logger.error("Could not load xml configuration!");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("核心框架应用关闭");
    }
}
