package org.chim.altass.core.streaming;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.constant.StreamEvent;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.manager.ConnectorManager;

import java.io.IOException;

/**
 * Class Name: AltassRabbitChannel
 * Create Date: 11/3/18 8:57 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class AltassRabbitChannel extends AbstractAltassChannel {

    private static final boolean durable = true;                    // Persistent
    private static final boolean exclusive = false;                 // Exclusive
    private static final boolean autoDelete = false;                // Auto delete queue
    private QueueingConsumer consumer = null;                       // Mq Consumer.
    protected Channel channel = null;                               // MQ Channel

    public AltassRabbitChannel(StreamingInfo streamingInfo, ITransaction transaction) {
        super(streamingInfo, transaction);
    }

    @Override
    protected void onChannelClose() {
    }

    @Override
    public void close(String dataSrc) {
        String currentNodeId = streamingInfo.getEntry().getNodeId();
        // 跳出之前需要把对应的数据队列需要手动删除
        try {
            channel.queueDelete(dataSrc + "." + currentNodeId + ".EVENT", false, true);
            channel.queueDelete(dataSrc + "." + currentNodeId + ".DATA", false, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void open() {
        Connection connection = ConnectorManager.getFactory().getConnection();
        try {
            channel = connection.createChannel();
            channel.exchangeDeclare(streamingInfo.getEntry().getNodeId(), BuiltinExchangeType.TOPIC, durable, autoDelete, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectPrevious(String precursorId) {
        String currentNodeId = streamingInfo.getEntry().getNodeId();
        String dataQueueName = precursorId + "." + currentNodeId + ".DATA";
        String eventQueueName = precursorId + "." + currentNodeId + ".EVENT";
        if (consumer == null)
            consumer = new QueueingConsumer(channel);
        try {
            channel.queueBind(eventQueueName, precursorId, "#." + currentNodeId + ".EVENT");
            channel.queueBind(dataQueueName, precursorId, "#." + currentNodeId + ".DATA");
            channel.basicConsume(dataQueueName, true, consumer);
            channel.basicConsume(eventQueueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public void connectSuccessor(String successorsId) {
        String currentNodeId = streamingInfo.getEntry().getNodeId();
        String currentNodeExchange = currentNodeId;
        String eventQueueName = currentNodeId + "." + successorsId + ".EVENT";
        String dataQueueName = currentNodeId + "." + successorsId + ".DATA";
        // 强制清空已存在的数据及事件队列：不允许重复队列存在，更不允许重复数据的存在，每一次执行到管道流
        try {
            channel.queueDeleteNoWait(eventQueueName, false, false);
            channel.queueDeleteNoWait(dataQueueName, false, false);

            // 定义数据队
            channel.queueDeclare(dataQueueName, durable, exclusive, autoDelete, null);
            // 定义事件队
            channel.queueDeclare(eventQueueName, durable, exclusive, autoDelete, null);

            String routingChar = streamingInfo.isDistributeNext() ? successorsId : "*";
            // 队列绑定
            channel.queueBind(dataQueueName, currentNodeExchange, currentNodeId + "." + routingChar + ".DATA", null);
            channel.queueBind(eventQueueName, currentNodeExchange, currentNodeId + "." + routingChar + ".EVENT", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doPublish(StreamData data) {
        String currentNodeExchange = streamingInfo.getEntry().getNodeId();
        String currentNodeId = streamingInfo.getEntry().getNodeId();
        byte event = data.getEvent();
        String routingKey;
        if (event == StreamEvent.EVENT_GROUP_FINISHED || event == StreamEvent.EVENT_FINISHED || event == StreamEvent.EVENT_SKIP) {
            data.setHead("EVENT");
            routingKey = currentNodeId + "." + currentNodeId + ".EVENT";
        } else if (event == StreamEvent.EVENT_START) {
            data.setHead("EVENT");
            routingKey = currentNodeId + ".*.EVENT";
        } else {
            data.setHead("DATA");
            routingKey = this.generateRoutingKey();
        }
        try {
            channel.basicPublish(currentNodeExchange, routingKey, null,
                    JSON.toJSONString(data).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public byte[] doReceive() {
        // 子类进行流式数据处理，若无异常，必须返回非null数据，否则被判定处理失败，进入重试环节
        QueueingConsumer.Delivery delivery = null;
        try {
            delivery = consumer.nextDelivery();
            String routingKey = delivery.getEnvelope().getRoutingKey();

            // 获取流推送的数据，并解析成为 StreamData 对象
            return delivery.getBody();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成数据路由键
     *
     * @return RoutingKey
     */
    private synchronized String generateRoutingKey() {
        String currentNodeId = streamingInfo.getEntry().getNodeId();

        int streamSuccessorIdxMapSize = streamingInfo.getStreamSuccessorIdxMap().size(); // The index map size of streaming successor.
        if (streamingInfo.isDistributeNext() && streamSuccessorIdxMapSize > 0) {
            // 计算分发推送的目标

            // TODO:允许使用多种策略，开发阶段默认使用轮询
            IEntry routingEntry = streamingInfo.getStreamSuccessorIdxMap().get(streamingInfo.getDataPushCount() % streamSuccessorIdxMapSize);
            String routingEntryNodeId = routingEntry.getNodeId();
            Integer lastCnt = streamingInfo.getPushDataCntMap().get(routingEntryNodeId);
            streamingInfo.getPushDataCntMap().put(routingEntryNodeId, lastCnt == null ? 1 : lastCnt + 1);
            return currentNodeId + "." + routingEntryNodeId + ".DATA";
        } else {
            // 复制模式
            return currentNodeId + ".*.DATA";
        }
    }

    public QueueingConsumer getConsumer() {
        return consumer;
    }
}
