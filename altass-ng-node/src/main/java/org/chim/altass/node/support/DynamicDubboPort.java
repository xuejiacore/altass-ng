package org.chim.altass.node.support;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import org.apache.log4j.Logger;
import org.chim.altass.core.constant.SystemEnv;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Class Name: DynamicDubboPort
 * Create Date: 18-2-24 下午7:44
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Dynamic set dubbo server port and init system env.
 */
@Component
public class DynamicDubboPort implements ApplicationContextAware {

    private Logger logger = Logger.getLogger(getClass());

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {

        Map<String, ProtocolConfig> dubboProtocolConfig =
                applicationContext.getBeansOfType(ProtocolConfig.class);

        Map<String, ApplicationConfig> dubboApplicationConfig =
                applicationContext.getBeansOfType(ApplicationConfig.class);

        // Use available qos port
        for (Map.Entry<String, ApplicationConfig> config : dubboApplicationConfig.entrySet()) {
            int port = NetUtils.getAvailablePort();
            config.getValue().setQosPort(port);
            logger.trace("Dubbo Application: " + config.getKey() + " - Qos Server [" + SystemEnv.SERVICE_NODE_HOST + ":" + port + "]");
        }

        // Use available dubbo port
        SystemEnv.SERVICE_NODE_HOST = NetUtils.getLocalHost();
        for (Map.Entry<String, ProtocolConfig> config : dubboProtocolConfig.entrySet()) {
            int PORT = NetUtils.getAvailablePort();
            SystemEnv.SERVICE_NODE_NAME = SystemEnv.SERVICE_NODE_HOST + "#" + SystemEnv.SERVICE_NODE_ID;
            logger.trace("Dubbo Protocol: " + config.getKey() + " - Service Node Listen on [" + SystemEnv.SERVICE_NODE_HOST + ":" + PORT + "]");
            config.getValue().setPort(PORT);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
