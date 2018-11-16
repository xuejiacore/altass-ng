package org.chim.altass.testing;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.debug.DebugConfig;
import org.chim.altass.core.executor.debug.DebugExecutor;
import org.chim.altass.core.executor.general.SimpleBlockingExecutor;
import org.chim.altass.testing.base.AbstractTesting;
import org.junit.Test;

/**
 * Class Name: BaseTest
 * Create Date: 11/2/18 7:45 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class BaseTest extends AbstractTesting {

    @Test
    public void simpleTest() throws InterruptedException {
        execute("simpleTest");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry start, Entry end) throws FlowDescException {
        if ("simpleTest".equalsIgnoreCase(selector)) {
            Entry debugEntryA = new Entry("DEBUG_A");
            debugEntryA.setExecutorClz(DebugExecutor.class);
            debugEntryA.addJsonArg(DebugConfig.class, "{\"delay\":\"5\"}");
            job.addEntry(debugEntryA);

            Entry debugEntryB = new Entry("DEBUG_B");
            debugEntryB.setExecutorClz(DebugExecutor.class);
            debugEntryB.addJsonArg(DebugConfig.class, "{\"delay\":\"4\"}");
            job.addEntry(debugEntryB);

            Entry debugEntryB1 = new Entry("DEBUG_B1");
            debugEntryB1.setExecutorClz(DebugExecutor.class);
            debugEntryB1.addJsonArg(DebugConfig.class, "{\"delay\":\"4\"}");
            job.addEntry(debugEntryB1);

            Entry blockA = new Entry("Block");
            blockA.setExecutorClz(SimpleBlockingExecutor.class);
            job.addEntry(blockA);

            Entry debugEntryC = new Entry("DEBUG_C");
            debugEntryC.setExecutorClz(DebugExecutor.class);
            debugEntryB.addJsonArg(DebugConfig.class, "{\"delay\":\"4\"}");
            job.addEntry(debugEntryC);

            job.connect(start, debugEntryA);
            job.connect(start, debugEntryB);
            job.connect(debugEntryB, debugEntryB1);
            job.connect(debugEntryA, blockA);
            job.connect(debugEntryB1, blockA);
            job.connect(blockA, debugEntryC);
            job.connect(debugEntryC, end);
        }
    }
}
