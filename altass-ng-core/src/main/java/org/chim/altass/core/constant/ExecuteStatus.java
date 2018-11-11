/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.face
 * Author: Xuejia
 * Date Time: 2016/12/19 13:41
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.constant;

/**
 * Class Name: ExecuteStatus
 * Create Date: 2016/12/19 13:41
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 执行器的执行状态
 */
public enum ExecuteStatus {
    SUCCESS(0x01, "执行成功"),
    FAILURE(0x02, "执行失败"),
    SKIPPED(0x04, "忽略执行");

    private int value;
    private String info;

    ExecuteStatus(int value, String info) {
        this.value = value;
        this.info = info;
    }

    /**
     * 获得当前执行状态的值
     *
     * @return 执行状态的值
     */
    public int value() {
        return this.value;
    }

    /**
     * 获得当前执行的信息
     *
     * @return 执行信息
     */
    public String info() {
        return this.info;
    }

    @Override
    public String toString() {
        return "ExecuteStatus{" +
                "value=" + value +
                ", info='" + info + '\'' +
                '}';
    }
}
