package org.chim.altass.executor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.redis.bean.RedisConfig;
import org.chim.altass.executor.redis.bean.Scripts;
import org.chim.altass.executor.redis.support.RedisScript;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: RedisExecutor
 * Create Date: 11/5/18 10:48 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Redis Executor
 */
@Executable(name = "redisExecutor", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING})
@Resource(name = "Redis", clazz = RedisExecutor.class, midImage = "res/images/executor/redis_bg.png", pageUrl = "nodeConfigs/ext/redisNodeConfig.jsp")
public class RedisExecutor extends AbstractStreamNodeExecutor {

    private RedisScript redisScript = null;

    /**
     * redis connection configuration.
     */
    @AltassAutowired
    private RedisConfig redisConfig = null;

    /**
     * script content that will be execute.
     */
    @AltassAutowired(analyzable = false)
    private Scripts scripts = null;

    /**
     * 输入参数，用于解析redis命令中变量占位符
     */
    private Map<String, Object> inputParseParams;

    /**
     * To initialized executor
     *
     * @param executeId execute id
     */
    public RedisExecutor(String executeId) throws ExecuteException {
        super(executeId);
        this.inputParseParams = new HashMap<>();
    }

    @Override
    protected boolean onChildInit() throws ExecuteException {
        redisScript = new RedisScript(redisConfig.getHost(), redisConfig.getPort());

        // 获得输入参数，该输入参数包含了从上一个节点的输出
        InputParam inputParam = entry.getInputParam();
        if (inputParam == null) {
            return true;
        }
        List<MetaData> params = inputParam.getParams();

        String field;
        for (MetaData param : params) {
            field = param.getField();
            this.inputParseParams.put(field.replace("$$", ""), param.getValue());
        }
        return super.onChildInit();
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onStreamProcessing(byte[] data) throws ExecuteException {
        if (this.scripts == null || StringUtils.isBlank(this.scripts.getScript())) {
            throw new IllegalArgumentException("Redis Script Content Not Allowed Null.");
        }

        StreamData streamData = transformData(data);
        Map<String, Object> params = JSON.parseObject(String.valueOf(streamData.getData()), Map.class);

        try {
            Map<String, Object> runContext = redisScript.run(scripts.getScript(), params);
            runContext.putAll(params);
            pushData(new StreamData(this.executeId, null, runContext));
        } catch (Exception e) {
            throw new ExecuteException(e);
        } finally {
            postFinished();
        }
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        if (this.scripts == null || StringUtils.isBlank(this.scripts.getScript())) {
            throw new IllegalArgumentException("Redis Script Content Not Allowed Null.");
        }
        try {
            redisScript.run(scripts.getScript(), inputParseParams);
        } catch (Exception e) {
            throw new ExecuteException(e);
        }
        return true;
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }

}
