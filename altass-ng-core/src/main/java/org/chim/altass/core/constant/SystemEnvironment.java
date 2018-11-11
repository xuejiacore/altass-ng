/*
 * Project: x-framework
 * Package Name: org.ike.etl.core
 * Author: Xuejia
 * Date Time: 2017/2/7 9:33
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.constant;

import org.redisson.api.RedissonClient;

/**
 * Class Name: SystemEnvironment
 * Create Date: 2017/2/7 9:33
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 系统环境
 */
public class SystemEnvironment {

    /**
     * 节点的运行模式
     */
    public enum RunningMode {
        NATIVE("native"), DISTRIBUTION("distribution");

        private String mode = "native";

        RunningMode(String runningMode) {
            mode = runningMode;
        }

        public String getMode() {
            return mode;
        }

        /**
         * 是否本地单机启动
         *
         * @return 如果是，返回值为true，否则返回值为false
         */
        public boolean isNative() {
            return "native".equals(mode);
        }
    }

    public static RunningMode RUNNING_MODE = RunningMode.NATIVE;

    /**
     * redisson客户端
     */
    public static RedissonClient redissonClient = null;
}
