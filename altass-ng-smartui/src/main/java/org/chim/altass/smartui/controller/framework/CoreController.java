/**
 * Project: x-framework
 * Package Name: org.ike.controller.framework
 * Author: Xuejia
 * Date Time: 2016/11/23 0:21
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.controller.framework;

import org.chim.altass.smartui.common.BaseController;
import org.chim.altass.smartui.common.StaticConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Class Name: CoreController
 * Create Date: 2016/11/23 0:21
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 框架的基础视图控制器
 */
@Controller
@RequestMapping("home")
public class CoreController extends BaseController {

    /**
     * 主页
     *
     * @return 返回框架主页视图
     */
    @RequestMapping(method = RequestMethod.GET, value = "index")
    public String index() {
        return "framework/framework";
    }

    @RequestMapping(method = RequestMethod.GET, value = "dashboard")
    public String dashboard() {
        return "framework/index";
    }

    /**
     * 未经授权的请求页面
     *
     * @return 返回未经授权的请求页面的跳转
     */
    @RequestMapping(method = RequestMethod.GET, value = "invalidateAuthPage")
    public String invalidAuthPage(HttpServletRequest request) {

        // 转发请求中的Attribute
        request.setAttribute(StaticConfig.KEY_ERR_PAGE_ATTR_NAME_MESSAGE, request.getAttribute(StaticConfig.KEY_ERR_PAGE_ATTR_NAME_MESSAGE));
        request.getSession();
        return "framework/sys/invalidateAuthPage";
    }

    /**
     * 不合法参数的视图
     *
     * @return 返回不合法参数的视图
     */
    @RequestMapping(method = RequestMethod.GET, value = "invalidateParamPage")
    public String invalidateParamPage() {
        return "framework/sys/invalidateParamPage";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/node/jdfViewer")
    public String jdfSourceViewer(HttpServletRequest request, @RequestParam(value = "jobId") String jobId) {
        request.setAttribute("jobId", jobId);
        return "support/flow/jdfSource";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/node/nodeRunLog")
    public String nodeRunLog(HttpServletRequest request,
                             @RequestParam(value = "jobId", required = true) String jobId,
                             @RequestParam(value = "nodeId", required = true) String nodeId) {
        request.setAttribute("jobId", jobId);
        request.setAttribute("nodeId", nodeId);
        return "support/flow/runningLog";
    }
}
