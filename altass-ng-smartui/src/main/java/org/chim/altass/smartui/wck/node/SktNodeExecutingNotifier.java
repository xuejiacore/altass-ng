/*
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.node
 * Author: Xuejia
 * Date Time: 2017/2/17 8:44
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.smartui.wck.node;

import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.smartui.wck.face.IWebSocket;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisPubSubConnection;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.pubsub.PubSubType;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class Name: SktNodeExecutingNotifier
 * Create Date: 2017/2/17 8:44
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点执行状态通知分发器
 */
@ServerEndpoint("/executorStatus")
public class SktNodeExecutingNotifier implements IWebSocket {

    private static CopyOnWriteArrayList<SktNodeExecutingNotifier> webSockets = new CopyOnWriteArrayList<>();
    private Session session = null;
    /**
     * 临时存储作业的目录
     */
    private static final String SAVE_DIR = "/data/eureka/data/jdf";

    @OnOpen
    @Override
    public void onOpen(Session session) {
        System.err.println("监听WebSocket打开事件");
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        System.err.println(requestParameterMap);
        List<String> jobIdList = requestParameterMap.get("jobId");
        webSockets.add(this);


        String jobId = jobIdList.get(0);
        String fileName = "JOB_" + jobId + ".jdf";
        Job job = null;
        try {
            job = EXmlParser.readFrom(SAVE_DIR + File.separator + fileName, Job.class);
        } catch (XmlParserException | IOException e) {
            e.printStackTrace();
        }
        if (job == null) {
            System.err.println("无法找到作业");
            return;
        }
        System.err.println(job + " | " + fileName);
        RedisClient c = new RedisClient("127.0.0.1:6379");
        RedisPubSubConnection pubSubConnection = c.connectPubSub();
        pubSubConnection.addListener(new RedisPubSubListener<Object>() {

            @Override
            public boolean onStatus(PubSubType type, String channel) {
                return true;
            }

            @Override
            public void onMessage(String channel, Object message) {
                System.err.println("channel: " + channel + " | message: " + message);
                synchronized (SktNodeExecutingNotifier.class) {
                    try {
                        RemoteEndpoint.Async asyncRemote = session.getAsyncRemote();
                        asyncRemote.sendText(channel.replace("_ENTRY_STATUS", "") + "," + message);
//                        asyncRemote.flushBatch();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPatternMessage(String pattern, String channel, Object message) {
            }
        });

        List<IEntry> entries = job.getEntries().getEntries();
        String[] entryId = new String[entries.size() + 3];
        entryId[0] = jobId + "_ENTRY_STATUS";
        entryId[1] = job.getStartNode().getNodeId() + "_ENTRY_STATUS";
        entryId[2] = job.getEndNode().getNodeId() + "_ENTRY_STATUS";
        int i = 3;
        for (IEntry entry : entries) {
            entryId[i++] = entry.getNodeId() + "_ENTRY_STATUS";
        }
        System.err.println(Arrays.toString(entryId));

        pubSubConnection.subscribe(StringCodec.INSTANCE, entryId);
    }

    @OnClose
    @Override
    public void onClose() {
        webSockets.remove(this);
        System.out.println("关闭当前的连接");
    }

    @OnError
    @Override
    public void onError(Session session, Throwable err) {
        err.printStackTrace();
        System.err.println("异常了");
    }

    @OnMessage
    @Override
    public void onMessage(String message, Session session) {
        System.err.println("接受到消息：" + message);
    }
}
