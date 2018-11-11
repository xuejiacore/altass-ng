/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.face
 * Author: Xuejia
 * Date Time: 2017/2/15 10:18
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.executor.face;


import org.chim.altass.core.domain.StatusInfo;

/**
 * Class Name: IExecutorListener
 * Create Date: 2017/2/15 10:18
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 作业 / 节点执行监听器
 */
public interface IExecutorListener {

    // 监听作业或者是节点的当前运行状态
    void onStatusChanging(String entryId, StatusInfo status);
}
