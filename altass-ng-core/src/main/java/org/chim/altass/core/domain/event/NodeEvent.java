/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.domain.event
 * Author: Xuejia
 * Date Time: 2016/12/15 18:10
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.event;


import org.chim.altass.base.parser.xml.annotation.Element;

/**
 * Class Name: NodeEvent
 * Create Date: 2016/12/15 18:10
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Element(name = "node_event")
public class NodeEvent extends MetaEvent {

    public interface EventType {
        int ON_PREPARE = 0x01;
        int ON_START = 0x02;
        int ON_PROCESSING = 0X04;
        int AFTER_PROCESS = 0x08;
        int ON_STOP = 0x10;
    }
}
