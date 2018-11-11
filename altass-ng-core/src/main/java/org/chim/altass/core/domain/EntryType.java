/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain
 * Author: Xuejia
 * Date Time: 2017/1/5 8:35
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain;

/**
 * Class Name: EntryType
 * Create Date: 2017/1/5 8:35
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface EntryType {
    interface Job {
        Integer TYPE_JOB = 0x0;
    }

    interface Node {
        /**
         * 开始节点
         */
        Integer TYPE_START_NODE = 0x1;
        /**
         * 结束节点
         */
        Integer TYPE_END_NODE = 0x2;
        /**
         * 简单阻塞节点
         */
        Integer TYPE_SIMPLE_BLOCK = 0x3;
        /**
         * 定时器类型
         */
        Integer TYPE_TIMER = 0x4;
        /**
         * 创建目录
         */
        Integer TYPE_CREATE_FOLDER = 0x5;

    }
}
