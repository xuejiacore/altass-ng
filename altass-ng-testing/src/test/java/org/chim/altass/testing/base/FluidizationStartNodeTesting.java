package org.chim.altass.testing.base;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.attr.StartNodeConfig;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.debug.DebugStreamExecutor;
import org.chim.altass.core.executor.minirun.FileInputMiniRunnable;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class Name: FluidizationStartNodeTesting
 * Create Date: 11/10/18 9:09 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FluidizationStartNodeTesting extends AbstractTesting {

    @Test
    public void startNodeBaseTest() throws InterruptedException {
        execute("startNodeBase");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        if ("startNodeBase".equalsIgnoreCase(selector)) {

            StartNodeConfig config = new StartNodeConfig();
            config.setRunnableClz(FileInputMiniRunnable.class);
            Map<String, Object> runParam = new TreeMap<>();
            runParam.put("filePath", "/data/eureka/logs/exe/20181111/20181111160535/StreamNode.log");
            config.setRunnableParamMap(runParam);
            startNode.addArg("startNodeConfig", config);

            Entry streamNode = new Entry();
            streamNode.setNodeId("StreamNode");
            streamNode.setExecutorClz(DebugStreamExecutor.class);
            job.addEntry(streamNode);

            job.connect(startNode, streamNode);
            job.connect(streamNode, endNode);
        }
    }
}
