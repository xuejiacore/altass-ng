package org.chim.altass.testing.executor;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.debug.DebugStreamExecutor;
import org.chim.altass.executor.RedisExecutor;
import org.chim.altass.executor.redis.bean.RedisConfig;
import org.chim.altass.executor.redis.bean.Scripts;
import org.chim.altass.testing.base.AbstractTesting;
import org.junit.Test;

/**
 * Class Name: RedisExecutorTest
 * Create Date: 11/12/18 9:06 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedisExecutorTest extends AbstractTesting {

    private static final RedisConfig redisConfig = new RedisConfig("127.0.0.1", 6379);

    @Test
    public void baseTest() throws InterruptedException {
        execute("baseTest");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        if ("baseTest".equalsIgnoreCase(selector)) {
            // 流化开始节点
            this.streamingStartNode(startNode);

            Scripts scripts = new Scripts();
            String scriptContent = "var0 = hget chimtestkey 1 key1; " +
                    "var1 = hset chimtestkey 1 ${column2} ${column1}; " +
                    "var2 = hget chimtestkey 1 ${column2};" +
                    "var3 = hset chimtestkey 2 $[var2] $[var2+var1];" +
                    "var4 = hget chimtestkey 2 key1;" +
                    "hdel chimtestkey 2 $[var2+34];";
            scripts.setScript(scriptContent);

            Entry redis = new Entry("RedisNode");
            redis.setExecutorClz(RedisExecutor.class);
            redis.inject("redisConfig", redisConfig);
            redis.inject("scripts", scripts);
            job.addEntry(redis);

            Entry streamDebug = new Entry("StreamDebug");
            streamDebug.setExecutorClz(DebugStreamExecutor.class);
            job.addEntry(streamDebug);

            job.connect(startNode, redis);
            job.connect(redis, streamDebug);
            job.connect(streamDebug, endNode);
        }
    }

}
