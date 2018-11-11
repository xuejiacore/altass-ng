package org.chim.altass.testing.executor;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.io.FileInputStreamExecutor;
import org.chim.altass.core.executor.io.FileOutputStreamExecutor;
import org.chim.altass.testing.base.AbstractTesting;
import org.junit.Test;

/**
 * Class Name: FileExecutorTest
 * Create Date: 11/3/18 4:58 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FileExecutorTest extends AbstractTesting {

    @Test
    public void simpleFileTest() throws InterruptedException {
        execute("simpleFile");
    }

    @Test
    public void dataSplit() throws InterruptedException {
        execute("dataSplit");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        if ("simpleFile".equalsIgnoreCase(selector)) {
            generateSimpleFile(job, startNode, endNode);
        } else if ("dataSplit".equalsIgnoreCase(selector)) {
            generateDataSplit(job, startNode, endNode);
        }
    }

    private void generateDataSplit(Job job, Entry startNode, Entry endNode) {
        Entry inputStreamEntry = new Entry();
        inputStreamEntry.setNodeId("INPUT-STREAM");
        inputStreamEntry.setExecutorClz(FileInputStreamExecutor.class);
        inputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"D:/data/input_stream_data_source_demo.txt\"}");
        inputStreamEntry.addJsonArg("columnConfig", "{\"ignoreHeader\":false, \"containColumnName\":true}");
        inputStreamEntry.addJsonArg("commonStreamConfig", "{\"dataDivisible\":true}");

        Entry outputStreamEntry = new Entry();
        outputStreamEntry.setNodeId("OUTPUT-STREAM");
        outputStreamEntry.setExecutorClz(FileOutputStreamExecutor.class);
        outputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"D:/data/output/http_result_data.txt\"}");

        job.addEntry(inputStreamEntry);
        job.addEntry(outputStreamEntry);

        job.addConnector(startNode, inputStreamEntry);
        job.addConnector(outputStreamEntry, endNode);
    }

    private void generateSimpleFile(Job job, Entry start, Entry end) {
        Entry inputStreamEntry = new Entry();
        inputStreamEntry.setNodeId("INPUT-STREAM");
        inputStreamEntry.setExecutorClz(FileInputStreamExecutor.class);
        inputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"/data/eureka/fiction_user.txt\"}");

        Entry outputStreamEntry = new Entry();
        outputStreamEntry.setNodeId("OUTPUT-STREAM");
        outputStreamEntry.setExecutorClz(FileOutputStreamExecutor.class);
        outputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"/data/eureka/output/fiction_user_output3.txt\"}");

        job.addEntry(inputStreamEntry);
        job.addEntry(outputStreamEntry);

        job.addConnector(start, inputStreamEntry);
        job.addConnector(inputStreamEntry, outputStreamEntry);
        job.addConnector(outputStreamEntry, end);
    }
}
