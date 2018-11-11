package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;
import org.chim.altass.core.manager.ConnectorManager;
import org.chim.altass.core.domain.buildin.attr.AEvent;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * Class Name: AbstractEventExecutor
 * Create Date: 2017/9/12 13:06
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 事件驱动-事件监听抽象类
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class AbstractEventExecutor extends AbstractNodeExecutor implements Consumer {

    private boolean durable = true;                 // 持久化
    private boolean exclusive = false;              // 排外
    private boolean autoDelete = true;             // 自动删除

    /**
     * 事件参数
     */
    private AEvent event = null;
    /**
     * MQ Channel
     */
    private Channel channel = null;
    /**
     * MQ Connection
     */
    private Connection connection = null;
    /**
     * Consumer Tag
     */
    private volatile String _consumerTag;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    protected AbstractEventExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public boolean onInit() throws ExecuteException {
        event = ((Entry) entry).getEvent();

        // TODO:测试
        AEvent event = new AEvent();
        event.setTopic("rabbitmq.test3.name");
        this.event = event;

        connection = ConnectorManager.getFactory().getConnection();
        try {
            channel = connection.createChannel();
            // 这里根据节点配置来获取需要处理的事件，当获得事件后，抽取对应的事件数据加载到当前的节点中
            channel.queueDeclare(event.getTopic(), durable, exclusive, autoDelete, null);
        } catch (IOException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    @Override
    public boolean onStart() throws ExecuteException {
        try {
            channel.basicConsume(event.getTopic(), true, this);
        } catch (IOException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    @Override
    public final boolean onNodeProcessing() throws ExecuteException {
        try {
            EXECUTOR_LOGGER("msg", "事件节点等待事件");
            onEventListening();
            await();
            onEventCheckPassed();
            EXECUTOR_LOGGER("msg", "事件检查点检查通过，执行下一节点");
        } catch (InterruptedException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    @Override
    public void onFinally() throws ExecuteException {
        super.onFinally();
        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            throw new ExecuteException(e);
        }
    }

    /**
     * 接收到事件后的处理
     *
     * @param consumerTag
     * @param envelope
     * @param properties
     * @param body
     * @throws IOException
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        String msg = new String(body, "UTF-8");
        JSONObject jsonObject = JSON.parseObject(msg);
        EXECUTOR_LOGGER("msg", "On Handle Delivery.", "json", jsonObject);
        // 只有事件触发后释放阻塞
        if (this.onEvent(consumerTag, envelope, properties, jsonObject)) {

            Set<String> keys = jsonObject.keySet();
            // 将事件数据作为当前节点的输出参数，提供给下一个节点使用
            for (String key : keys) {
                addOutputParam(new MetaData(key, jsonObject.get(key).toString()));
            }

            // 事件触发通过，释放锁
            wakeup();
            EXECUTOR_LOGGER("msg", "事件检查通过，释放锁");
        }
    }

    /**
     * 事件开始监听中
     */
    protected abstract void onEventListening();

    /**
     * 事件到达的时候的触发事件
     *
     * @param consumerTag 消费tag
     * @param envelope    envelope
     * @param properties  基本属性
     * @param jsonObject  消息体
     * @return 如果检验通过，确定是能够触发的消息，那么返回值为true，放行当前事件节点，继续走其他流程，否则返回值为false
     */
    protected abstract boolean onEvent(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       JSONObject jsonObject);

    /**
     * 时间检查通过后的触发接口
     */
    protected abstract void onEventCheckPassed();

    @Override
    public void handleConsumeOk(String consumerTag) {
        EXECUTOR_LOGGER("msg", "eventHandleConsumeOk", "data", consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        this._consumerTag = consumerTag;
        EXECUTOR_LOGGER("msg", "eventHandleCancelOk", "consumerTag", consumerTag);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        EXECUTOR_LOGGER("msg", "eventHandleCancel", "consumerTag", consumerTag);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException e) {
        EXECUTOR_LOGGER("msg", "eventHandleShutdownSignal", "consumerTag", consumerTag);
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        EXECUTOR_LOGGER("msg", "eventHandleRecoverOk", "consumerTag", consumerTag);
    }

    @Override
    protected boolean onDisconnect() {
        throw new UnsupportedOperationException();
    }

    public Channel getChannel() {
        return this.channel;
    }

    public String getConsumerTag() {
        return this._consumerTag;
    }
}
