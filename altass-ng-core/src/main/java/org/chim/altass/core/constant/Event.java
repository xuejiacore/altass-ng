package org.chim.altass.core.constant;

/**
 * Class Name: Event
 * Create Date: 18-4-5 上午10:32
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public enum Event {

    PUBLISH_JOB_REARRANGEMENT(0x1, "发布一个作业重新编排的消息，数据为UpdateAnalysis");

    private String desc = null;
    private Integer id = null;

    Event(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String redisKey() {
        return this.name() + "#" + this.id;
    }

    public String channel() {
        return redisKey();
    }
}
