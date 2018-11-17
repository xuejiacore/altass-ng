package org.chim.altass.testing.executor;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.executor.RedisExecutor;
import org.chim.altass.executor.redis.bean.RedisConfig;
import org.chim.altass.executor.redis.bean.Scripts;
import org.chim.altass.testing.base.AbstractStreamTesting;
import org.junit.Test;

/**
 * Class Name: RedisExecutorTest
 * Create Date: 11/10/18 11:21 AM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedisExecutorStreamTest extends AbstractStreamTesting {

    private static final RedisConfig redisConfig = new RedisConfig("127.0.0.1", 6379);

    @Test
    public void redisBaseTest() throws InterruptedException {
        execute("redisBase");
    }

    @Test
    public void transmitNextTest() throws InterruptedException {
        execute("transmitNext");
    }

    @Override
    public void streamExecutorDecorator(String selector, Job job, Entry inputNode, Entry endNode) {
        if ("redisBase".equalsIgnoreCase(selector)) {
            this.generateRedisBase(job, inputNode, endNode);
        } else if ("transmitNext".equalsIgnoreCase(selector)) {
            this.generateTransmitNext(job, inputNode, endNode);
        }
    }

    private void generateTransmitNext(Job job, Entry inputNode, Entry endNode) {
        Scripts scripts = new Scripts();
        String scriptContent = "var0 = hget chimtestkey 1 key1; " +
                "var1 = hset chimtestkey 1 ${column2} ${column1}; " +
                "var2 = hget chimtestkey 1 ${column2};" +
                "var3 = hset chimtestkey 2 $[var2] $[var2+var1];" +
                "var4 = hget chimtestkey 2 key1;" +
                "hdel chimtestkey 2 $[var2+34];";
        scripts.setScript(scriptContent);

        Entry redis = new Entry("Redis");
        redis.setExecutorClz(RedisExecutor.class);
        redis.inject("redisConfig", redisConfig);
        redis.inject("scripts", scripts);
        job.addEntry(redis);

        Entry fileOutput = newBaseFileOutputEntry("/data/altass/executor/redis/output.txt");
        job.addEntry(fileOutput);

        job.connect(inputNode, redis);
        job.connect(redis, fileOutput);

        job.connect(fileOutput, endNode);
    }

    private void generateRedisBase(Job job, Entry inputNode, Entry endNode) {
        Scripts scripts = new Scripts();
        String scriptContent = "var0 = hget chimtestkey 1 key1; " +
                "var1 = hset chimtestkey 1 ${column2} ${column1}; " +
                "var2 = hget chimtestkey 1 ${column2};" +
                "var3 = hset chimtestkey 2 $[var2] $[var2+var1];" +
                "var4 = hget chimtestkey 2 key1;" +
                "hdel chimtestkey 2 $[var2+34];";
        scripts.setScript(scriptContent);

        Entry redis = new Entry("Redis");
        redis.setExecutorClz(RedisExecutor.class);
        redis.inject("redisConfig", redisConfig);
        redis.inject("scripts", scripts);
        job.addEntry(redis);

        job.connect(inputNode, redis);

        job.connect(redis, endNode);
    }

}
