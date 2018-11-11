/**
 * Project: x-framework
 * Package Name: org.ike.controller.framework
 * Author: Xuejia
 * Date Time: 2016/11/21 20:01
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.controller.framework;

import com.google.gson.Gson;
import org.chim.altass.base.config.XmlConfiguration;
import org.chim.altass.smartui.common.BaseController;
import org.chim.altass.smartui.common.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Class Name: ConfigurationController
 * Create Date: 2016/11/21 20:01
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 框架配置接口服务
 * 规定访问权限为系统内部级，访问级别为SYSTEM
 */
@Controller
@RequestMapping("_system")
public class ConfigurationController extends BaseController {

    /**
     * 获得zookeeper的配置信息
     *
     * @param request request
     * @return 返回相应实体
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "_getZookeeper")
    @ResponseStatus(HttpStatus.OK)
    public CommonResult getZookeeperConfig(HttpServletRequest request) {
        CommonResult result = new CommonResult();

        XmlConfiguration configInstance = XmlConfiguration.getInstance(request.getSession().getServletContext());

        HashMap<String, Object> zookeeperConfigMap = new HashMap<>();
        HashMap<String, Object> configMap = configInstance.getConfigMap();
        // 获取当前核心框架中配置的zookeeper的IP以及端口号
        zookeeperConfigMap.put(XmlConfiguration.CONFIG_K_ZOOKEEPER_IP, configMap.get(XmlConfiguration.CONFIG_K_ZOOKEEPER_IP));
        zookeeperConfigMap.put(XmlConfiguration.CONFIG_K_ZOOKEEPER_PORT, configMap.get(XmlConfiguration.CONFIG_K_ZOOKEEPER_PORT));

        result.setFlag(true);
        result.setResult(new Gson().toJson(zookeeperConfigMap));
        return result;
    }

    /**
     * 获得核心框架应用的配置信息
     *
     * @param request -
     * @return 返回核心框架的信息
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "_getCoreAppInfo")
    @ResponseStatus(HttpStatus.OK)
    public CommonResult getCoreAppInfo(HttpServletRequest request) {
        CommonResult result = new CommonResult();

        XmlConfiguration xmlConfiguration = XmlConfiguration.getInstance(request.getSession().getServletContext());
        HashMap<String, Object> configMap = xmlConfiguration.getConfigMap();
        HashMap<String, Object> coreAppConfig = new HashMap<>();
        // 核心框架应用的启动节点名称
        coreAppConfig.put(XmlConfiguration.CONFIG_K_FRAMEWORK_CORE_APP_NAME, configMap.get(XmlConfiguration.CONFIG_K_FRAMEWORK_CORE_APP_NAME));

        result.setFlag(true);
        result.setResult(new Gson().toJson(coreAppConfig));
        return result;
    }
}
