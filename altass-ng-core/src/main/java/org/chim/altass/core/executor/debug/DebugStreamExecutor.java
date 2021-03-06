package org.chim.altass.core.executor.debug;

import com.alibaba.fastjson.JSON;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.ansi.AnsiColor;
import org.chim.altass.core.ansi.AnsiOutput;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.constant.StreamEvent;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.Map;

/**
 * Class Name: DebugStreamExecutor
 * Create Date: 2017/10/27 19:18
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Executable(name = "debugExecutor", assemble = true)
@Resource(name = "DebugStream", clazz = DebugStreamExecutor.class, midImage = "res/images/node/debug_bg.png", pageUrl = "nodeConfigs/debug/debugNodeConfig.jsp")
public class DebugStreamExecutor extends AbstractStreamNodeExecutor {

    @AltassAutowired(analyzable = false)
    private DebugConfig debugConfig = null;

    private long beginTime = 0;

    /**
     * To initialized executor
     *
     * @param executeId execute id
     */
    public DebugStreamExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    protected boolean onChildInit() throws ExecuteException {
        beginTime = System.currentTimeMillis();
        if (debugConfig == null) {
            debugConfig = new DebugConfig();
        }
        return true;
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    public void onStreamProcessing(StreamData data) throws ExecuteException {
        try {
            Thread.sleep(debugConfig.getDelay());

            String str = "DEBUG-DATA:\t" + Thread.currentThread().getName() + "\t" + this.getExecuteId() + "\t" +
                    data.getStreamSrc() + "\t" + data.getData() + "\t" + data.getGroupKey() + "\t" + data.getEvent();
            if (data.getData() == null) {
                str = JSON.toJSONString(data);
            }
            str = AnsiOutput.toString(AnsiColor.BLUE, str);
            System.out.println(str);
            Object pushData;
            if (debugConfig.isUseOutputJson()) {
                Object strData = data.getData();
                if (strData == null || "null".equalsIgnoreCase(String.valueOf(strData))) {
                    pushData = debugConfig.getOutputJson();
                } else {
                    Map map = JSON.parseObject(String.valueOf(strData), Map.class);
                    if (map == null || debugConfig.getOutputJson() == null) {
                        pushData = debugConfig.getOutputJson();
                    } else {
                        pushData = scriptParse(map, debugConfig.getOutputJson());
                    }
                }
            } else {
                pushData = data.getData();
            }
            pushData(new StreamData(this.getExecuteId(), StreamEvent.EVENT_DATA, pushData));
        } catch (Exception e) {
            throw new ExecuteException(e);
        } finally {
            postFinished();
        }
    }

    @Override
    protected void onCurrentProcessFinished() throws ExecuteException {
        System.out.println(this.getExecuteId() + "\t---------------->>> 耗时：" + (System.currentTimeMillis() - beginTime) + " ms");
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        return false;
    }

    @Override
    public void onStreamClose(StreamData data) throws ExecuteException {
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }

    public void setDebugConfig(DebugConfig debugConfig) {
        this.debugConfig = debugConfig;
    }
}
