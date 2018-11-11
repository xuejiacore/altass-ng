/**
 * Project: x-framework
 * Package Name: org.ike.core
 * Author: Xuejia
 * Date Time: 2016/12/10 15:13
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base;

import javax.servlet.ServletContext;
import java.io.Serializable;

/**
 * Class Name: ApplicationContext
 * Create Date: 2016/12/10 15:13
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ApplicationContext implements Serializable {

    private static final ApplicationContext context = new ApplicationContext();
    private ServletContext servletcontext;

    public static ApplicationContext getContext() {
        return context;
    }

    public ServletContext getServletcontext() {
        return servletcontext;
    }

    public void setServletcontext(ServletContext servletcontext) {
        this.servletcontext = servletcontext;
    }
}
