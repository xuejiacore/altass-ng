package org.chim.altass.core.executor.general;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractEventExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: SimpleAbstractEventAbstractNodeExecutor
 * Create Date: 2017/9/14 20:04
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 简单事件监听阻塞节点
 * <p>
 * 该类节点会持续监听一个事件的触发，直到事件到达并且确认数据后，停止监听，并且继续执行后续作业节点
 */
@Executable(name = "simpleEventExecutor", assemble = true)
@Resource(name = "事件监听", clazz = SimpleEventExecutor.class, midImage = "res/images/node/event_bg.png")
public class SimpleEventExecutor extends AbstractEventExecutor {

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public SimpleEventExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    protected void onEventListening() {
        System.err.println("onEventListening--------------->>>");
    }

    @Override
    protected boolean onEvent(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, JSONObject jsonObject) {
        System.err.println(jsonObject.toJSONString());
        return true;
    }

    @Override
    protected void onEventCheckPassed() {

    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }
}
