package org.chim.altass.testing.base;

import com.alibaba.dubbo.rpc.RpcException;
import org.chim.altass.base.utils.type.DateUtil;
import org.chim.altass.core.AltassNode;
import org.chim.altass.core.constant.Status;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.attr.StartNodeConfig;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.general.JobExecutor;
import org.chim.altass.core.executor.general.StartExecutor;
import org.chim.altass.core.executor.io.FileOutputStreamExecutor;
import org.chim.altass.core.executor.minirun.FileInputMiniRunnable;
import org.chim.altass.toolkit.JDFWrapper;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisPubSubConnection;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.pubsub.PubSubType;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

/**
 * Class Name: AbstractTesting
 * Create Date: 11/2/18 7:39 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractTesting {

    private AltassNode node;

    public AbstractTesting() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                obtainXmlApplicationConfigLocation()
        );
        context.start();
        node = context.getBean(AltassNode.class);
    }

    protected void execute(String tag) throws FlowDescException, InterruptedException {
        Job job = createJob();
        this.executorDecorator(tag, job, job.getStartNode(), job.getEndNode());
        CountDownLatch latch = this.bindingCallback(job);

        Thread.sleep(200);
        try {
            this.node.run(new JDFWrapper(job));
        } catch (RpcException e) {
            throw new RpcException("无法连接到阿尔塔斯集群. [Failure to connect AltassCluster.]");
        }
        latch.await();
    }

    protected Job createJob() {
        Job job = new Job("START", "END");
        job.setJobId(DateUtil.format(new Date(), DateUtil.SDFYYYYMMDD));
        job.setExecutorClz(JobExecutor.class);
        return job;
    }

    private CountDownLatch bindingCallback(Job job) throws InterruptedException {
        String jobId = job.getJobId();
        CountDownLatch latch = new CountDownLatch(1);
        RedisClient c = new RedisClient("127.0.0.1:6379");
        RedisPubSubConnection pubSubConnection = c.connectPubSub();
        pubSubConnection.addListener(new RedisPubSubListener<Object>() {

            @Override
            public boolean onStatus(PubSubType type, String channel) {
                return true;
            }

            @Override
            public void onMessage(String channel, Object message) {
                String entryId = channel.replace("_ENTRY_STATUS", "");
                Status status = Status.valueOf(String.valueOf(message));
                System.out.println("entryId: " + entryId + "\tstatus: " + status);
                if (status.equals(Status.JOB_FINISHED)) {
                    System.err.println("作业执行完成");
                    latch.countDown();
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
        pubSubConnection.subscribe(StringCodec.INSTANCE, entryId);
        return latch;
    }

    /**
     * 流式化开始节点，以基础的文件流装饰
     *
     * @param startNode 开始节点
     */
    protected void streamingStartNode(Entry startNode) {
        if (!StartExecutor.class.isAssignableFrom(startNode.getExecutorClz())) {
            throw new IllegalArgumentException("目前只支持开始节点的流化");
        }

        // 开始节点的mini run配置
        StartNodeConfig config = new StartNodeConfig();
        // 选择文件输入配置
        config.setRunnableClz(FileInputMiniRunnable.class);
        // mini run 参数
        Map<String, Object> runParam = new TreeMap<>();
        runParam.put("filePath", "/data/eureka/collection_user.txt");
        config.setRunnableParamMap(runParam);

        // 内容流分割处理
        startNode.addArg("startNodeConfig", config);
        startNode.addJsonArg("fileStreamConfig", "{\"path\":\"D:/data/input_stream_data_source_demo.txt\"}");
        startNode.addJsonArg("columnConfig", "{\"ignoreHeader\":false, \"containColumnName\":true}");
        startNode.addJsonArg("commonStreamConfig", "{\"dataDivisible\":true}");
    }

    protected Entry newBaseFileOutputEntry(String path) {
        return newBaseFileOutputEntry("OUTPUT-STREAM", path);
    }

    protected Entry newBaseFileOutputEntry(String nodeId, String path) {
        Entry outputStreamEntry = new Entry();
        outputStreamEntry.setNodeId(nodeId);
        outputStreamEntry.setExecutorClz(FileOutputStreamExecutor.class);
        outputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"" + path + "\"}");
        return outputStreamEntry;
    }

    public String obtainXmlApplicationConfigLocation() {
        return "classpath:spring/eureka-core-service-dubbo-consumer.xml";
    }

    public abstract void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException;
}
