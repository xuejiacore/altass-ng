package org.chim.altass.testing.base;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.config.ConvergentConfig;
import org.chim.altass.core.executor.config.DisassemblyConfig;
import org.chim.altass.core.executor.config.SimpleSequenceConfig;
import org.chim.altass.core.executor.debug.DebugConfig;
import org.chim.altass.core.executor.debug.DebugExecutor;
import org.chim.altass.core.executor.debug.DebugStreamExecutor;
import org.chim.altass.core.executor.general.Iterator;
import org.chim.altass.core.executor.general.SimpleMerger;
import org.chim.altass.core.executor.toolkit.GenSequenceExecutor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: DisassemblyConvergentTesting
 * Create Date: 11/27/18 11:51 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DisassemblyConvergentTesting extends AbstractTesting {

    @Test
    public void baseTest() throws InterruptedException {
        execute("baseTest");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        if ("baseTest".equalsIgnoreCase(selector)) {
            Entry debug = new Entry("Debug", DebugExecutor.class);


            SimpleSequenceConfig simpleSequenceConfig = new SimpleSequenceConfig();
            // the start of sequence
            simpleSequenceConfig.setStart(0L);
            // the end of sequence
            simpleSequenceConfig.setEnd(5L);
            // iterator condition
//        simpleSequenceConfig.setExpression("index % 2 == 1");
            // concat iterator val
            simpleSequenceConfig.setTransferExpress("'val=' + val");

            Entry seqEntry = new Entry("SeqNode");
            seqEntry.setExecutorClz(GenSequenceExecutor.class);
            seqEntry.inject("simpleSequenceConfig", simpleSequenceConfig);

            DebugConfig debugConfig = new DebugConfig();
            debugConfig.setOutputJson(TEST_JSON);
            debugConfig.setUseOutputJson(true);
            Entry debugStream = new Entry("DebugStream", DebugStreamExecutor.class);
            debugStream.inject("debugConfig", debugConfig);

            DisassemblyConfig disassemblyConfig = new DisassemblyConfig();
            disassemblyConfig.setExpress("outkey4array");
            disassemblyConfig.setGroupId("outkey1");
            Entry iterator = new Entry("Iterator", Iterator.class);
            iterator.inject("disassemblyConfig", disassemblyConfig);


            // 测试数据源
            Entry debugStream2 = new Entry("DebugStream2", DebugStreamExecutor.class);

            ConvergentConfig convergentConfig = new ConvergentConfig();
            Map<String, String> mergeMap = new HashMap<>();
            mergeMap.put("newkey", "key4arrayitem1key2");
            convergentConfig.setMergeMap(mergeMap);
            Entry simpleMerger = new Entry("SimpleMerger", SimpleMerger.class);
            simpleMerger.inject("convergentConfig", convergentConfig);

            Entry debugStream3 = new Entry("DebugStreamFinal", DebugStreamExecutor.class);

            job.link(startNode, debug, seqEntry, debugStream, iterator, debugStream2, simpleMerger, debugStream3, endNode);
        }
    }

    private static final String TEST_JSON = "{\n" +
            "  \"outkey1\": \"${val}\",\n" +
            "  \"outkey2\": 2,\n" +
            "  \"outkey3array\": [\n" +
            "    \"val1\",\n" +
            "    \"val2\",\n" +
            "    999,\n" +
            "    \"2018-11-24 13:49:20\"\n" +
            "  ],\n" +
            "  \"outkey4array\": [\n" +
            "    {\n" +
            "      \"key4arrayitem1key1\": \"item1\",\n" +
            "      \"key4arrayitem1key2\": ${val}\n" +
            "    },\n" +
            "    {\n" +
            "      \"key4arrayitem1key1\": \"item1\",\n" +
            "      \"key4arrayitem1key2\": 88\n" +
            "    }\n" +
            "  ],\n" +
            "  \"outkey5obj\": {\n" +
            "    \"key5objitem1key1\": \"key5objval1\",\n" +
            "    \"key5objitem1key2\": 5\n" +
            "  },\n" +
            "  \"outkey6objarray\": {\n" +
            "    \"key6objitemkey1\": \"simple\",\n" +
            "    \"key6objitemkey2\": [\n" +
            "      \"val1\",\n" +
            "      \"val2\",\n" +
            "      \"val3\"\n" +
            "    ],\n" +
            "    \"key6objitemkey3\": [\n" +
            "      {\n" +
            "        \"key6objitemkey3inner1\": \"innerval1\",\n" +
            "        \"key6objitemkey3inner2\": 66\n" +
            "      },\n" +
            "      {\n" +
            "        \"key6objitemkey3inner1\": \"innerval2\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

}
