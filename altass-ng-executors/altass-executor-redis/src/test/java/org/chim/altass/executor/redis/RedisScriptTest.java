package org.chim.altass.executor.redis;

import org.chim.altass.executor.redis.support.RedisScript;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: RedisScriptTest
 * Create Date: 11/9/18 1:21 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedisScriptTest {

    private RedisScript redisScript = null;

    @BeforeMethod
    public void setUp() {
        redisScript = new RedisScript("127.0.0.1", 6379);
    }

    @Test
    public void testScript() {
        /*
        String script = "   var1  =hset testkey@20 1  ${hk}    ${hv}  ;\n\n\n\n" +
                "     ret2 =     hget   testkey    1   hk;" +
                " ret3 = ttl testkey ;不显示\n" +
                "\n" +
                "   # 这是注释行，不显示--> retx = hget testkey hk <--不显示; \n\n\n" +
                "ret5 = set testkey@21 csa ttval;";
        */

        String script = "var0 = hget chimtestkey 1 key1; " +
                "var1 = hset chimtestkey@30 1 key1 ${outparam1}; " +
                "var2 = hget chimtestkey 1 key1;" +
                "var3 = hset chimtestkey 2 $[var2+34] $[(var2+1)*20+var1];" +
                "var4 = hget chimtestkey 2 key1;" +
                "hdel chimtestkey 2 $[var2+34];";


        Map<String, Object> outParams = new HashMap<>();
        outParams.put("outparam1", 9);
        try {
            Map<String, Object> runContext = redisScript.run(script, outParams);
            System.out.println("执行结果：" + runContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
