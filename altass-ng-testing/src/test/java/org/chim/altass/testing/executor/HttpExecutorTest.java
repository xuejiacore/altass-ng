package org.chim.altass.testing.executor;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.executor.config.ColumnConfig;
import org.chim.altass.core.domain.buildin.attr.CommonStreamConfig;
import org.chim.altass.core.domain.buildin.attr.FileStreamConfig;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.io.FileInputStreamExecutor;
import org.chim.altass.core.executor.io.FileOutputStreamExecutor;
import org.chim.altass.executor.HttpExecutor;
import org.chim.altass.testing.base.AbstractTesting;
import org.junit.Test;

/**
 * Class Name: HttpExecutorTest
 * Create Date: 11/3/18 4:10 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class HttpExecutorTest extends AbstractTesting {

    /**
     * 简单Http请求
     */
    @Test
    public void simpleHttp() throws InterruptedException {
        execute("simpleHttp");
    }

    /**
     * 流式Http请求
     */
    @Test
    public void streamHttp() throws InterruptedException {
        execute("streamHttp");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry start, Entry end) throws FlowDescException {
        if ("simpleHttp".equalsIgnoreCase(selector)) {
            generateSimpleHttpJob(job, start, end);
        } else if ("streamHttp".equalsIgnoreCase(selector)) {
            generateStreamHttp(job, start, end);
        }
    }

    private void generateStreamHttp(Job job, Entry start, Entry end) {
        Entry fileInput = new Entry();
        fileInput.setNodeId("FILE-INPUT-STREAM");
        fileInput.setExecutorClz(FileInputStreamExecutor.class);
        fileInput.addJsonArg(FileStreamConfig.class, "{\"path\":\"/data/eureka/test_data_source/mop_conf_api.txt\"}");
        fileInput.addJsonArg(ColumnConfig.class, "{\"ignoreHeader\":false, \"containColumnName\":true}");
        fileInput.addJsonArg(CommonStreamConfig.class, "{\"dataDivisible\":true,\"textSeparator\":\",\"}");
        job.addEntry(fileInput);

        Entry streamHttpEntry = new Entry();
        streamHttpEntry.setNodeId("STREAM-HTTP-TEST");
        streamHttpEntry.setExecutorClz(HttpExecutor.class);

        streamHttpEntry.addArg("test", "testValue");
        streamHttpEntry.addJsonArg("httpConfig", "{\"method\":\"get\",\"url\":\"http://conf.mop.com/config/simple/common\",\"header\":{\"Content-Type\":\"application/json\",\"Cookies\":\"_mc=123g;_mu=123sfas\"},\"params\":{\"conf\":[\"${conf_name}\"]},\"dataType\":\"json\"}");
        job.addEntry(streamHttpEntry);


        Entry outputStreamEntry = new Entry();
        outputStreamEntry.setNodeId("OUTPUT-STREAM");
        outputStreamEntry.setExecutorClz(FileOutputStreamExecutor.class);
        outputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"/data/eureka/output/http_response/mop_conf_resp.txt\"}");
        job.addEntry(outputStreamEntry);

        job.connect(start, fileInput);
        job.connect(fileInput, streamHttpEntry);
        job.connect(streamHttpEntry, outputStreamEntry);
        job.connect(outputStreamEntry, end);
    }

    private void generateSimpleHttpJob(Job job, Entry start, Entry end) {
        Entry entry = new Entry();
        entry.setNodeId("HTTP-TEST");
        entry.setExecutorClz(HttpExecutor.class);

        entry.addArg("test", "testValue");
        entry.addJsonArg("httpConfig", "{\"method\":\"get\",\"url\":\"http://conf.mop.com/novelVersion/v1.0/getCloudConfig\",\"header\":{\"Content-Type\":\"application/json\",\"Cookies\":\"_mc=123g;_mu=123sfas\"},\"params\":{\"mobile\":[\"13621794081\"],\"item2\":[\"itemValue2\"]},\"dataType\":\"json\"}");

        job.addEntry(entry);

        job.connect(start, entry);
        job.connect(entry, end);
    }
}
