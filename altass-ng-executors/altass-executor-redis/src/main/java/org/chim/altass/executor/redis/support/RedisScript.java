package org.chim.altass.executor.redis.support;

import org.apache.commons.lang3.StringUtils;
import org.chim.altass.base.script.Script;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: RedisScript
 * Create Date: 11/6/18 5:14 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class RedisScript extends Script {

    private static final String REDIS_SCRIPT_VAR_START = "$[";
    private static final String REDIS_SCRIPT_VAR_END = "]";
    private static final Pattern SCRIPT_PATTERN = Pattern.compile("((?<retval>\\w+) *= *)?(?<cmd>\\w+) *(?<key>\\w+(@\\d+)?) *(?<keyid>\\w+?)?( *(?<params>.*?))?;");

    private JedisCmd cmd = null;

    public RedisScript(String host, int port) {
        this.cmd = new JedisCmd(host, port);
    }

    /**
     * 执行redis批量脚本
     * <p>
     * 外部的运算参数使用 "${"和"}" 包含，redis 的内部变量运算使用 "$["和"]"包含
     * <p>
     * 如果是TTL Key，在key名称后以"@"分割，最后为ttl时间，单位为s
     *
     * @param script 需要执行的脚本内容
     * @param params 外部参数
     * @return 支持返回参数
     *
     * <pre><code>
     * # 注释...<br/>
     * var0 = hget chimtestkey 1key1;<br/>
     * var1 = hset chimtestkey@30 1key1 ${outparam1};<br/>
     * var2 = hget chimtestkey 1key1;<br/>
     * var3 = hset chimtestkey 2$[var2+34]$[(var2+1)*20+var1];<br/>
     * var4 = hget chimtestkey 2key1;<br/>
     * hdel chimtestkey 2$[var2+34];<br/>
     * </code></pre>
     */
    public Map<String, Object> run(String script, Map<String, Object> params) throws Exception {
        return run(parseScript(params, script));
    }

    /**
     * 执行redis批量脚本
     * <p>
     * 外部的运算参数使用 "${"和"}" 包含，redis 的内部变量运算使用 "$["和"]"包含
     * <p>
     * 如果是TTL Key，在key名称后以"@"分割，最后为ttl时间，单位为s
     *
     * @param script 需要执行的脚本内容
     * @return 支持返回参数
     *
     * <pre><code>
     * # 当前行注释...<br/>
     * var0 = hget chimtestkey 1key1;<br/>
     * var1 = hset chimtestkey@30 1key1 ${outparam1};<br/>
     * var2 = hget chimtestkey 1key1;<br/>
     * var3 = hset chimtestkey 2$[var2+34]$[(var2+1)*20+var1];<br/>
     * var4 = hget chimtestkey 2key1;<br/>
     * hdel chimtestkey 2$[var2+34];<br/>
     * </code></pre>
     */
    public Map<String, Object> run(String script) throws Exception {
        // 清除注释和空白行，清除连续多空格
        script = script
                .replaceAll("#.*\n?", "")
                .replaceAll("\\s*\\n", "")
                .replaceAll("\\s+", " ");
        Matcher matcher = SCRIPT_PATTERN.matcher(script);

        Map<String, Object> runtimeContext = new TreeMap<>();

        Object cmdResult;
        while (matcher.find()) {
            // 命令执行的返回值
            String cmdExecRet = matcher.group("retval");
            // 需要执行的redis命令
            String cmd = matcher.group("cmd");
            // 操作的key名字
            String key = matcher.group("key");
            // 操作的key id
            String keyId = matcher.group("keyid");
            // 操作的执行参数
            String params = matcher.group("params");

            // 使用上下文重新计算key
            key = parseScript(key, runtimeContext, REDIS_SCRIPT_VAR_START, REDIS_SCRIPT_VAR_END);
            // 使用上下文重新计算key id
            keyId = parseScript(keyId, runtimeContext, REDIS_SCRIPT_VAR_START, REDIS_SCRIPT_VAR_END);
            // 使用上下文重新计算操作参数
            params = parseScript(params, runtimeContext, REDIS_SCRIPT_VAR_START, REDIS_SCRIPT_VAR_END);

            String[] split = params.split(" ");
            Object[] data = new Object[split.length];
            System.arraycopy(split, 0, data, 0, split.length);

            // 执行根据上下文运算后的命令
            cmdResult = this.cmd.e(cmd, key, keyId, data);

            if (StringUtils.isNotBlank(cmdExecRet)) {
                runtimeContext.put(cmdExecRet, cmdResult == null ? "null" : cmdResult);
            }

        }

        // 所有的命令解析完成后，加分布式锁顺次执行，
        return runtimeContext;
    }
}
