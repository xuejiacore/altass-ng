package org.chim.altass.core.constant;

/**
 * Class Name: Status
 * Create Date: 11/2/18 9:23 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public enum Status {
    JOB_STATED(1, "作业启动"),

    RUNNING(2, "正在运行"),
    SKIPPED(3, "跳过"),
    INITIALIZING(4, "正在初始化"),
    INITIALIZED(5, "初始化完成"),
    STARTING(6, "正在启动"),
    STARTED(7, "启动完成"),
    FINISHED_BEFORE_PROCESS(8, "完成预处理"),
    FINISHED_PROCESSING(9, "完成处理"),
    FINISHED_AFTER_PROCESS(10, "完成后处理"),
    FINISHED_FINALLY(11, "完成FINALLY块"),
    EXCEPTION(12, "异常处理完毕"),
    FINISHED(13, "完成"),
    ENTRY_FINISHED(14, "节点/作业完成"),
    ENTRY_CHILD_FINISHED(15, "子节点/作业完成"),
    ENTRY_PAUSED(16, "节点暂停"),
    ENTRY_PAUSING(17, "节点暂停中"),
    ENTRY_RESUMING(18, "节点恢复中"),
    ENTRY_STOPPING(19, "节点停止中"),
    ENTRY_STOPPED(20, "节点已经停止"),
    JOB_REARRANGEMENT(21, "作业进行更新事件"),

    JOB_FINISHED(22, "作业完成"),

    UNKNOWN(-1, "未知状态");

    private Integer statusCode = null;
    private String desc;

    Status(Integer statusCode, String desc) {
        this.statusCode = statusCode;
        this.desc = desc;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Status value(Integer code) {
        Status[] values = Status.values();
        for (Status value : values) {
            if (value.getStatusCode().equals(code)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
