/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution
 * Author: Xuejia
 * Date Time: 2017/2/15 14:02
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.manager;

import org.chim.altass.core.constant.Status;
import org.chim.altass.core.domain.StatusInfo;
import org.chim.altass.core.executor.face.IExecutorListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Class Name: ExecutorStatusPublisher
 * Create Date: 2017/2/15 14:02
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 分布式执行器状态发布者
 */
public class ExecutorStatusPublisher implements IExecutorListener {

    private JedisPool jedisPool = null;

    /**
     * 节点执行状态
     *
     * @param entryId 节点ID
     * @param status  状态
     */
    @Override
    public void onStatusChanging(String entryId, StatusInfo status) {
        Jedis jedisResource = null;
        try {
            jedisResource = jedisPool.getResource();
            // 将节点的执行状态发布到消息中心（Redis）中
            // 所有的节点状态都会发布到Redis中
            jedisResource.publish(entryId + "_ENTRY_STATUS", Status.value(status.getStatusCode()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedisResource);
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
