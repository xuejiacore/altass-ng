package org.chim.altass.testing.base;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.config.SimpleSequenceConfig;
import org.chim.altass.core.executor.debug.DebugExecutor;
import org.chim.altass.core.executor.debug.DebugStreamExecutor;
import org.chim.altass.core.executor.toolkit.GenSequenceExecutor;
import org.junit.Test;

/**
 * Class Name: SequenceExecutorTesting
 * Create Date: 11/16/18 8:41 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class SequenceExecutorTesting extends AbstractTesting {

    @Test
    public void baseTest() throws InterruptedException {
        execute("baseTest");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        if ("baseTest".equalsIgnoreCase(selector)) {
            this.generateBaseTest(job, startNode, endNode);
        }
    }

    private void generateBaseTest(Job job, Entry startNode, Entry endNode) {
        Entry debug = new Entry("DebugNode");
        debug.setExecutorClz(DebugExecutor.class);

        SimpleSequenceConfig simpleSequenceConfig = new SimpleSequenceConfig();
        // the start of sequence
        simpleSequenceConfig.setStart(0L);
        // the end of sequence
        simpleSequenceConfig.setEnd(10L);
        // iterator condition
        simpleSequenceConfig.setExpression("index % 2 == 1");
        // concat iterator val
        simpleSequenceConfig.setOutputExpression("'val=' + val");

        Entry seqEntry = new Entry("SeqNode");
        seqEntry.setExecutorClz(GenSequenceExecutor.class);
        seqEntry.addArg("simpleSequenceConfig", simpleSequenceConfig);

        Entry debugStream = new Entry("DebugStream");
        debugStream.setExecutorClz(DebugStreamExecutor.class);

        job.link(startNode, debug, seqEntry, debugStream, endNode);
    }
}
