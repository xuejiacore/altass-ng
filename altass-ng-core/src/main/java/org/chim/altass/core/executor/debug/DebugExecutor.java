package org.chim.altass.core.executor.debug;

import com.alibaba.fastjson.JSON;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.ansi.AnsiColor;
import org.chim.altass.core.ansi.AnsiOutput;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.List;
import java.util.Map;

/**
 * Class Name: DebugExecutor
 * Create Date: 2017/10/23 16:56
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Executable(name = "debugExecutor", assemble = true)
@Resource(name = "Debug", clazz = DebugExecutor.class, midImage = "res/images/node/debug_bg.png", pageUrl = "nodeConfigs/debug/debugNodeConfig.jsp")
public class DebugExecutor extends AbstractNodeExecutor {

    @AltassAutowired
    private DebugConfig debugConfig = null;

    @Override
    public boolean onInit() throws ExecuteException {
        if (this.debugConfig == null) {
            this.debugConfig = new DebugConfig();
            this.debugConfig.setThrowError(false);
            this.debugConfig.setDelay(2);
        }
        return true;
    }

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public DebugExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {
        try {
            System.out.println("Debug节点[" + entry.getNodeId() + "]，接收到更新包：" + EXmlParser.toXml(analysis));
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() throws ExecuteException {
        super.onPause();
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public boolean onNodeProcessing() throws ExecuteException {
        InputParam inputParam = this.context.getInputParam();
        if (inputParam != null) {
            List<MetaData> params = inputParam.getParams();
            if (params != null && params.size() > 0) {
                System.out.println("~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+ Debug节点[" + entry.getNodeId() + "] 参数数据 +~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~");
                System.out.println("输入参数：");
                for (MetaData param : params) {
                    System.out.println(param.getField() + "\t" + param.getValue());
                }
                System.out.println("~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+ Debug节点[" + entry.getNodeId() + "] 参数数据 +~+~+~+~+~+~+~+~+~+~+~+~+~+~+~+~");
            }
        }
        if (debugConfig.isThrowError()) {
            throw new ExecuteException(this.debugConfig.getExceptionDesc());
        }

        if (debugConfig.getDelay() != 0) {
            long delayTime = debugConfig.getDelay() * 1000;
            try {
                int sec = (int) (delayTime / 1000);
                int i = 0;
                for (; i < sec; i++) {
                    logger.trace(AnsiOutput.toString(AnsiColor.BLUE,
                            "[" + this.entry.getNodeId() + "] - SIMULATION EXECUTIVE\t[" +
                                    (i + 1) + "000/" + debugConfig.getDelay() * 1000 + " ms]"));
                    Thread.sleep(1000);
                    this.statusChecking();
                }
                int remain = (int) (delayTime % 1000);
                if (remain > 0) {
                    logger.trace(AnsiOutput.toString(AnsiColor.BLUE,
                            "[" + this.entry.getNodeId() + "] - SIMULATION EXECUTIVE\t[" +
                                    (i * 1000 + remain) + "/" + debugConfig.getDelay() * 1000 + " ms]"));
                    Thread.sleep(remain);
                }
            } catch (InterruptedException e) {
                throw new ExecuteException(e);
            }
        }
        String outputJson = debugConfig.getOutputJson();
        if (outputJson != null && outputJson.length() > 0) {
            Map map = JSON.parseObject(outputJson, Map.class);
            for (Object o : map.keySet()) {
                MetaData metaData = new MetaData(String.valueOf(o), String.valueOf(map.get(o)));
                addOutputParam(metaData);
            }
        }
        return true;
    }

    // 设置debug配置
    public void setDebugConfig(DebugConfig debugConfig) {
        this.debugConfig = debugConfig;
    }
}
