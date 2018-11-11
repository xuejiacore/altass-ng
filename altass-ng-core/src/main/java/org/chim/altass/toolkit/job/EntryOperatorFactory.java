package org.chim.altass.toolkit.job;

import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.toolkit.RedissonToolkit;

/**
 * Class Name: EntryOperatorFactory
 * Create Date: 18-4-4 上午12:45
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点操作器工厂类
 */
public final class EntryOperatorFactory {

    /**
     * 生成一个暂停临近节点的节点操作器
     */
    public static EntryOperator pauseClosestActiveOperator() {

        return new EntryOperator() {

            @Override
            public boolean onCondition(IEntry entry) {
                // 如果节点活动的，那么进行暂停
                Integer currentStatus = (Integer) RedissonToolkit.getInstance().getBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, entry.getNodeId());
                return currentStatus != null;
            }

            @Override
            public boolean operate(IEntry entry, IEntry root) {
                entry.pause(false);
                // 只暂停临近的节点
                return entry.getNodeId().equals(root.getNodeId());
            }
        };
    }
}
