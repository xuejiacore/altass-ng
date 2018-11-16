package org.chim.altass.core.executor.debug;

import com.alibaba.fastjson.JSON;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.io.UnsupportedEncodingException;

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

    @AltassAutowired
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
    public StreamData onStreamProcessing(byte[] data) throws ExecuteException {

        try {
            String dataStr = new String(data, "UTF-8");

            Thread.sleep(debugConfig.getDelay() * 1000);
            StreamData streamData = JSON.parseObject(dataStr, StreamData.class);

            System.err.println("DEBUG-DATA:\t" + Thread.currentThread().getName() + "\t" + this.getExecuteId() + "\t" + streamData.getData());

            return new StreamData(this.getExecuteId(), null, streamData.getData());
        } catch (UnsupportedEncodingException | InterruptedException e) {
            throw new ExecuteException(e);
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
