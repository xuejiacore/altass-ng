package org.chim.altass.core.executor;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.chim.altass.core.manager.ConnectorManager;
import org.chim.altass.core.domain.buildin.attr.AEvent;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.ExecuteException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Class Name: AbstractTriggerExecutor
 * Create Date: 2017/9/15 0:21
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("FieldCanBeLocal")
public abstract class AbstractTriggerExecutor extends AbstractNodeExecutor {

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
    protected AbstractTriggerExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public boolean onInit() throws ExecuteException {
        event = ((Entry) entry).getEvent();

        // TODO:测试
        AEvent event = new AEvent();
        event.setTopic("rabbitmq.test3.name");
        event.setTriggerData("{\"testData\":\"testDataValue\"}");
        this.event = event;

        connection = ConnectorManager.getFactory().getConnection();
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onStart() throws ExecuteException {
        try {
            String queueName = event.getTopic();
            AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
            declareOk.getConsumerCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 消息发送节点只是单纯地发布一个触发命令和数据到MQ中，无需阻塞，并且节点的执行是很快的
     *
     * @return 如果执行成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException 节点运行时异常
     */
    @Override
    public boolean onNodeProcessing() throws ExecuteException {
        String triggerData = event.getTriggerData();
        String pattern = "";
        try {
            // 发布一个消息
            channel.basicPublish(pattern, event.getTopic(), null, triggerData.getBytes("UTF-8"));
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    @Override
    protected boolean onDisconnect() {
        throw new UnsupportedOperationException();
    }

}
