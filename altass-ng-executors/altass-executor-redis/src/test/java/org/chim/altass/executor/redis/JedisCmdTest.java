package org.chim.altass.executor.redis;

import org.chim.altass.executor.redis.constant.KeyType;
import org.chim.altass.executor.redis.support.JedisCmd;
import org.chim.altass.executor.redis.support.RedisKey;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Class Name: JedisCmdTest
 * Create Date: 11/5/18 11:36 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class JedisCmdTest {

    private JedisCmd jedisCmd = null;

    @BeforeMethod
    public void setUp() {
        jedisCmd = new JedisCmd("127.0.0.1", 6379);
    }

    @Test
    public void testGet() {
        RedisKey testKey = new RedisKey(KeyType.STRING, "test", 20);
        boolean isSuccess = jedisCmd.set(testKey, 1, "testvalue");
        Assert.assertTrue(isSuccess);
        String value = jedisCmd.get(testKey, 1);
        Assert.assertEquals(value, "testvalue");
    }

    @Test
    public void testE() {
        RedisKey testKey = new RedisKey("set", "test", 20);
        Object test = jedisCmd.e(testKey, "test", 20);
        System.out.println(test);
        testKey = new RedisKey("get", "test", 20);
        test = jedisCmd.e(testKey, "test", 20);
        System.out.println(test);
    }

    @Test
    public void testHSet() {
        Object e = jedisCmd.e("hset", "hsettest@20", 1, "hkey", "hval2");
        System.out.println(e);
        Object e1 = jedisCmd.e("hget", "hsettest", 1, "hkey");
        System.out.println(e1);

        // hget keyser hk hv
        // hget keyser hk hv
    }

}
