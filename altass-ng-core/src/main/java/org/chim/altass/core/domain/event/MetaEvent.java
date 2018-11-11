/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.domain.event
 * Author: Xuejia
 * Date Time: 2016/12/15 18:00
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.event;


import org.chim.altass.base.parser.xml.annotation.Attribute;
import org.chim.altass.core.domain.Element;

/**
 * Class Name: MetaEvent
 * Create Date: 2016/12/15 18:00
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@org.chim.altass.base.parser.xml.annotation.Element(name = "eventMeta")
public class MetaEvent extends Element {

    /**
     * 事件类型
     */
    @Attribute(name = "type")
    private Integer eventType = null;

    /**
     * 事件的消息
     */
    @Attribute(name = "msg")
    private String msg = null;

    /**
     * 事件调用
     */
    @Attribute(name = "call")
    private MetaCallable call = null;

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MetaCallable getCall() {
        return call;
    }

    public void setCall(MetaCallable call) {
        this.call = call;
    }
}
