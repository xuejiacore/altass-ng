/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.domain.meta
 * Author: Xuejia
 * Date Time: 2016/12/15 17:20
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.meta;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: MetaData
 * Create Date: 2016/12/15 17:20
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "meta")
public class MetaData {
    /**
     * 数据值
     */
    @Attr(alias = "value")
    private String value = null;

    /**
     * 数据类型
     */
    @Attr(alias = "type")
    private String type = null;

    /**
     * 数据长度
     */
    @Attr(alias = "length")
    private String length = null;

    /**
     * 数据域
     */
    @Attr(alias = "field")
    private String field = null;

    /**
     * 数据名称
     */
    @Attr(alias = "comment")
    private String comment = null;

    /**
     * 数据格式化信息
     */
    @Attr(alias = "fmt")
    private String formatter = null;

    /**
     * 数据校验信息
     */
    @Attr(alias = "validate")
    private String validation = null;

    /**
     * 数据表达式
     */
    @Attr(alias = "express")
    private String expression = null;

    public MetaData() {
    }

    /**
     * 元数据
     *
     * @param field 数据名称
     * @param value 数据的值
     */
    public MetaData(String field, String value) {
        this.value = value;
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", length='" + length + '\'' +
                ", field='" + field + '\'' +
                ", comment='" + comment + '\'' +
                ", formatter='" + formatter + '\'' +
                ", validation='" + validation + '\'' +
                ", expression='" + expression + '\'' +
                '}';
    }

    public String info() {
        return "field:" + field + ", value:" + value;
    }
}
