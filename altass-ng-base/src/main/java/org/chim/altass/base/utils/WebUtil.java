/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/8 23:41
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.*;

/**
 * Class Name: WebUtil
 * Create Date: 2016/12/8 23:41
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class WebUtil {


    public WebUtil() {
    }

    public static Map getParametersStartingWith(ServletRequest request, String prefix) {
        AssertUtil.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        TreeMap params = new TreeMap();
        if (prefix == null) {
            prefix = "";
        }

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values != null && values.length != 0) {
                    if (values.length > 1) {
                        params.put(unprefixed, values);
                    } else {
                        params.put(unprefixed, values[0]);
                    }
                }
            }
        }

        return params;
    }

    public static Map<String, Object> getWebContext(PageContext pageContext) {
        HashMap context = new HashMap();
        HashMap paramScope = new HashMap();
        HashMap pageScope = new HashMap();
        HashMap requestScope = new HashMap();
        HashMap sessionScope = new HashMap();
        HashMap applicationScope = new HashMap();
        Map parameterMap = pageContext.getRequest().getParameterMap();
        Iterator attributeNames = parameterMap.keySet().iterator();

        String attributeName;
        while (attributeNames.hasNext()) {
            attributeName = (String) attributeNames.next();
            String[] parameterValues = (String[]) ((String[]) parameterMap.get(attributeName));
            if (parameterValues.length == 1) {
                paramScope.put(attributeName, parameterValues[0]);
                context.put(attributeName, parameterValues[0]);
            } else {
                paramScope.put(attributeName, parameterValues);
                context.put(attributeName, parameterValues);
            }
        }

        Enumeration attributeNames1 = pageContext.getAttributeNamesInScope(4);
        if (attributeNames1 != null) {
            while (attributeNames1.hasMoreElements()) {
                attributeName = (String) attributeNames1.nextElement();
                applicationScope.put(attributeName, pageContext.getAttribute(attributeName, 4));
                context.put(attributeName, pageContext.getAttribute(attributeName, 4));
            }
        }

        attributeNames1 = pageContext.getAttributeNamesInScope(3);
        if (attributeNames1 != null) {
            while (attributeNames1.hasMoreElements()) {
                attributeName = (String) attributeNames1.nextElement();
                sessionScope.put(attributeName, pageContext.getAttribute(attributeName, 3));
                context.put(attributeName, pageContext.getAttribute(attributeName, 3));
            }
        }

        attributeNames1 = pageContext.getAttributeNamesInScope(2);
        if (attributeNames1 != null) {
            while (attributeNames1.hasMoreElements()) {
                attributeName = (String) attributeNames1.nextElement();
                requestScope.put(attributeName, pageContext.getAttribute(attributeName, 2));
                context.put(attributeName, pageContext.getAttribute(attributeName, 2));
            }
        }

        attributeNames1 = pageContext.getAttributeNamesInScope(1);
        if (attributeNames1 != null) {
            while (attributeNames1.hasMoreElements()) {
                attributeName = (String) attributeNames1.nextElement();
                pageScope.put(attributeName, pageContext.getAttribute(attributeName, 1));
                context.put(attributeName, pageContext.getAttribute(attributeName, 1));
            }
        }

        context.put("param", paramScope);
        context.put("page", pageScope);
        context.put("request", requestScope);
        context.put("session", sessionScope);
        context.put("application", applicationScope);
        return context;
    }

}
