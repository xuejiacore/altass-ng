/**
 * Project: x-framework
 * Package Name: org.ike.controller
 * Author: Xuejia
 * Date Time: 2016/10/1 18:36
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class Name: TestController
 * Create Date: 2016/10/1 18:36
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Controller
@RequestMapping("tester")
public class TestController {

    @RequestMapping("test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }

}
