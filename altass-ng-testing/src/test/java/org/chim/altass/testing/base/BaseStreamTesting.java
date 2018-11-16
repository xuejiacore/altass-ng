package org.chim.altass.testing.base;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.executor.debug.DebugStreamExecutor;
import org.junit.Test;

/**
 * Class Name: BaseStreamTesting
 * Create Date: 11/10/18 12:26 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class BaseStreamTesting extends AbstractStreamTesting {

    @Test
    public void baseStreamTest() throws InterruptedException {
        execute("baseStream");
    }

    @Override
    public void streamExecutorDecorator(String selector, Job job, Entry inputNode, Entry endNode) {
        if ("baseStream".equalsIgnoreCase(selector)) {
            this.generateBaseStream(job, inputNode, endNode);
        }
    }

    private void generateBaseStream(Job job, Entry inputNode, Entry endNode) {
        Entry successorPipeline1 = new Entry();
        successorPipeline1.setNodeId("SUCCESSOR-DEBUGSTREAM-1");
        successorPipeline1.setExecutorClz(DebugStreamExecutor.class);
        job.addEntry(successorPipeline1);

        Entry successorPipeline2 = new Entry();
        successorPipeline2.setNodeId("SUCCESSOR-DEBUGSTREAM-2");
        successorPipeline2.setExecutorClz(DebugStreamExecutor.class);
        job.addEntry(successorPipeline2);

        job.connect(inputNode, successorPipeline1);
        job.connect(inputNode, successorPipeline2);

        job.connect(successorPipeline1, endNode);
        job.connect(successorPipeline2, endNode);
    }
}
