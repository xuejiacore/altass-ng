/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.domain.meta
 * Author: Xuejia
 * Date Time: 2016/12/15 17:29
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.meta;


import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: OutputParam
 * Create Date: 2016/12/15 17:29
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 输出参数
 */
@Elem(alias = "outputParams")
public class OutputParam extends Element {

    private static final long serialVersionUID = -5241338802614276579L;
    @Elem(alias = "meta")
    private List<MetaData> params = null;

    public List<MetaData> getParams() {
        if (params == null) {
            this.params = new ArrayList<>();
        }
        return params;
    }

    public void setParams(List<MetaData> params) {
        this.params = params;
    }

    /**
     * 向参数列表中添加参数
     *
     * @param param 需要添加的参数
     */
    public void addParameter(MetaData param) {
        if (this.params == null) {
            this.params = new ArrayList<>();
        }
        this.params.add(param);
    }

    /**
     * 获得参数参数列表中指定域的参数信息
     *
     * @param field 参数域
     * @return 返回参数信息
     */
    public MetaData getParameter(String field) {
        if (params == null) {
            return null;
        }
        for (MetaData param : params) {
            if (field.equals(param.getField())) {
                return param;
            }
        }
        return null;
    }
}
