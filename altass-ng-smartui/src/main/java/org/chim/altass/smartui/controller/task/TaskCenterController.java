package org.chim.altass.smartui.controller.task;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Class Name: TaskCenterController
 * Create Date: 2017/10/5 15:11
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Controller
@RequestMapping(value = "/task")
public class TaskCenterController {

    @RequestMapping(value = "/index")
    public String taskDashboard(@RequestParam(value = "uid", required = true) String uid) {
        return "support/task/task_dashboard";
    }
}
