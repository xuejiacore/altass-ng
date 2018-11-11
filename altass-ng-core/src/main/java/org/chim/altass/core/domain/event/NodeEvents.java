/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.domain.event
 * Author: Xuejia
 * Date Time: 2016/12/15 18:13
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.event;


import org.chim.altass.core.domain.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: NodeEvents
 * Create Date: 2016/12/15 18:13
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@org.chim.altass.base.parser.xml.annotation.Element(name = "events")
public class NodeEvents extends Element {

    @org.chim.altass.base.parser.xml.annotation.Element(name = "event")
    private List<NodeEvent> events = null;

    public List<NodeEvent> getEvents() {
        return events;
    }

    public void setEvents(List<NodeEvent> events) {
        this.events = events;
    }

    /**
     * 添加事件处理
     *
     * @param event 需要添加的事件
     */
    public void addEvent(NodeEvent event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
    }
}
