package org.chim.altass.smartui.controller.api;

import org.chim.altass.smartui.common.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Class Name: TaskCenterController
 * Create Date: 2017/10/5 15:06
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Controller
@RequestMapping(value = "/api/task")
public class ApiTaskCenterController {

    public CommonResult jobListApi(@RequestParam(value = "uid", required = true) String userId,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        return null;
    }
}
