package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: AEvent
 * Create Date: 2017/9/14 19:05
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "event", version = "1.0")
public class AEvent {
    @Attr(alias = "eventId")
    private String eventId = null;                                                  // 事件ID
    @Attr(alias = "name")
    private String name = null;                                                     // 事件的名称
    @Attr(alias = "topic")
    private String topic = null;                                                    // 消息的topic
    @Attr(alias = "triggerData")
    private String triggerData = null;                                              // 触发数据
    @Attr(alias = "eventData")
    private String eventData = null;                                                // 事件触发数据

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTriggerData() {
        return triggerData;
    }

    public void setTriggerData(String triggerData) {
        this.triggerData = triggerData;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
}
