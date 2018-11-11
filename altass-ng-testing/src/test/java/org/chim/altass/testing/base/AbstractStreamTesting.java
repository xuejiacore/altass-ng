package org.chim.altass.testing.base;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.io.FileInputStreamExecutor;

/**
 * Class Name: AbstractStreamTesting
 * Create Date: 11/10/18 11:11 AM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractStreamTesting extends AbstractTesting {

    private String TEST_DATA_SRC_PATH = "/data/eureka/collection_user.txt";

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        Entry inputStreamEntry = new Entry();
        inputStreamEntry.setNodeId("File-Input");
        inputStreamEntry.setExecutorClz(FileInputStreamExecutor.class);
        inputStreamEntry.addJsonArg("fileStreamConfig", "{\"path\":\"" + getDataSrcPath() + "\"}");
        inputStreamEntry.addJsonArg("columnConfig", getColumnConfig());
        inputStreamEntry.addJsonArg("commonStreamConfig", getCommonStreamConfig());
        job.addEntry(inputStreamEntry);
        job.addConnector(startNode, inputStreamEntry);
        this.streamExecutorDecorator(selector, job, inputStreamEntry, endNode);
        System.out.println("FileInputStream Initialized.");
    }


    public abstract void streamExecutorDecorator(String selector, Job job, Entry inputNode, Entry endNode);

    protected String getDataSrcPath() {
        return TEST_DATA_SRC_PATH;
    }

    protected String getCommonStreamConfig() {
        return "{\"dataDivisible\":true}";
    }

    protected String getColumnConfig() {
        return "{\"ignoreHeader\":false, \"containColumnName\":true}";
    }
}
