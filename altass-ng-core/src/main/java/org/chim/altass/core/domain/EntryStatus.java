/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain
 * Author: Xuejia
 * Date Time: 2016/12/28 9:27
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

/**
 * Class Name: EntryStatus
 * Create Date: 2016/12/28 9:27
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface EntryStatus {
    /**
     * 正在运行
     */
    Integer RUNNING = 0x0;
    /**
     * 跳过
     */
    Integer SKIPPED = 0x1;
    /**
     * 正在初始化
     */
    Integer INITIALIZING = 0x2;
    /**
     * 初始化完成
     */
    Integer INITIALIZED = 0x3;
    /**
     * 正在启动
     */
    Integer STARTING = 0x4;
    /**
     * 启动完成
     */
    Integer STARTED = 0x5;
    /**
     * 完成预处理
     */
    Integer FINISHED_BEFORE_PROCESS = 0x6;
    /**
     * 完成处理
     */
    Integer FINISHED_PROCESSING = 0x7;
    /**
     * 完成后处理
     */
    Integer FINISHED_AFTER_PROCESS = 0x8;
    /**
     * 完成FINALLY块
     */
    Integer FINISHED_FINALLY = 0x9;
    /**
     * 异常处理完毕
     */
    Integer EXCEPTION = 0xa;
    /**
     * 完成
     */
    Integer FINISHED = 0xb;
    /**
     * 节点/作业完成
     */
    Integer ENTRY_FINISHED = 0xc;

    /**
     * 子节点/作业完成
     */
    Integer ENTRY_CHILD_FINISHED = 0xd;

    /**
     * 节点暂停
     */
    Integer ENTRY_PAUSED = 0x10;

    /**
     * 节点暂停中
     */
    Integer ENTRY_PAUSING = 0x20;

    /**
     * 节点恢复中
     */
    Integer ENTRY_RESUMING = 0x30;

    /**
     * 节点停止中
     */
    Integer ENTRY_STOPPING = 0x40;

    /**
     * 节点已经停止
     */
    Integer ENTRY_STOPPED = 0x50;

    /**
     * 作业进行更新事件
     */
    Integer JOB_REARRANGEMENT = 0x60;
}
