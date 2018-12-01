package org.chim.altass.core.executor.io;

import org.chim.altass.base.io.UniversalFileSystem;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.domain.buildin.attr.CommonStreamConfig;
import org.chim.altass.core.domain.buildin.attr.FileStreamConfig;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractStreamNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.core.executor.config.ColumnConfig;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * Class Name: FileOutputStreamExecutor
 * Create Date: 2017/10/26 17:09
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 文件输出执行器
 * <p>
 * 持有流式执行能力，可以承接上一个持有流式管道特性的数据，根据执行器的输出配置输出到目标位置
 */
@SuppressWarnings("FieldCanBeLocal")
@Executable(name = "fileOutputStreamExecutor", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING})
@Resource(name = "FileOutput", clazz = FileOutputStreamExecutor.class, groupName = "常规", midImage = "res/images/node/pipeline_output_bg.png", pageUrl = "nodeConfigs/core/outputFileNodeConfig.jsp")
public class FileOutputStreamExecutor extends AbstractStreamNodeExecutor {

    // File stream configuration
    @AltassAutowired
    private FileStreamConfig fileStreamConfig = null;

    // Basic common stream configuration
    @AltassAutowired
    private CommonStreamConfig commonStreamConfig = null;

    // Column split rule configuration
    @AltassAutowired
    private ColumnConfig columnConfig = null;

    // BufferedWriter
    private BufferedWriter writer = null;

    // writer buffer
    private Integer flushLineCnt = null;
    // indicate whether is first line
    private boolean firstLine = true;

    // column name list
    private List<String> columnList = null;
    // tag common stream config is not null
    private boolean commonStreamConfigNotNull = false;
    // tag column configuration is not null
    private boolean columnConfigNotNull = false;
    // tag file stream configuration is not null
    private boolean fileStreamConfigNotNull = false;
    // tag true if structuring and parse
    private boolean structuringAndWillParse = false;
    // tag true if is json data structuring
    private boolean jsonDataStruct = false;
    private boolean willParseJsonToRowData = false;

    private int currentWriteLine = 0;

    /**
     * To initialized executor
     *
     * @param executeId execute id
     */
    public FileOutputStreamExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected boolean onChildInit() throws ExecuteException {
        fileStreamConfigNotNull = fileStreamConfig != null;
        if (fileStreamConfigNotNull) {
            String path = fileStreamConfig.getPath();
            this.flushLineCnt = fileStreamConfig.getFlushLineCnt();
            try {

                // Use FileOperator to obtain Universal File Operator and get OutputStream

                OutputStream outputStream = UniversalFileSystem.getOutputStream(path, true);
                writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            } catch (IOException e) {
                throw new ExecuteException(e);
            }

            commonStreamConfigNotNull = commonStreamConfig != null;
            columnConfigNotNull = columnConfig != null;
            structuringAndWillParse = commonStreamConfigNotNull && commonStreamConfig.isStructuring();
            jsonDataStruct = commonStreamConfigNotNull
                    && commonStreamConfig.getDataStruct() == CommonStreamConfig.DATA_STRUCT_JSON;
            willParseJsonToRowData = structuringAndWillParse && jsonDataStruct && columnConfigNotNull;
            columnList = columnConfigNotNull ? columnConfig.getColumnList() : null;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    public void onStreamOpen(StreamData data) throws ExecuteException {
    }

    @Override
    protected void onCurrentProcessFinished() throws ExecuteException {
        try {
            writer.flush();
            writer.close();
            postFinished();
        } catch (IOException e) {
            throw new ExecuteException(e);
        }
    }

    @Override
    public void onStreamProcessing(StreamData data) throws ExecuteException {
        try {
            // 如果开启了数据解析抽取，进行数据行解析
            if (willParseJsonToRowData) {
                if (columnList != null) {
                    parseStructureToRowData(data, columnList);
                }
            } else {
                writer.write(String.valueOf(data.getData()));
            }

            writer.write(fileStreamConfig.getLineBreak());
            if (++currentWriteLine % flushLineCnt == 0) {
                writer.flush();
            }
        } catch (IOException e) {
            throw new ExecuteException(e);
        } finally {
            if (writer != null) {
                postFinished();
            }
        }
    }

    /**
     * 解析数据为一个行数据
     *
     * @param streamData 解析的数据源
     * @param columnList 列名
     * @throws IOException -
     */
    private void parseStructureToRowData(StreamData streamData, List<String> columnList) throws IOException {
        int size = columnList.size();
        if (firstLine) {
            for (int i = 0; i < size; i++) {
                writer.write(columnList.get(i));
                if (i < size - 1) {
                    writer.write(commonStreamConfig.getTextSeparator());
                }
            }
            firstLine = false;
            writer.write(fileStreamConfig.getLineBreak());
        }
        for (int i = 0; i < size; i++) {
            Map rowMap = (Map) streamData.getData();
            writer.write(String.valueOf(rowMap.get(columnList.get(i))));
            if (i < size - 1)
                writer.write(commonStreamConfig.getTextSeparator());
        }
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        throw new ExecuteException("Only support streaming execute.");
    }

    @Override
    public boolean isStreamingProcessing() throws ExecuteException {
        // 只支持流式处理
        return true;
    }

    @Override
    public void onStreamClose(StreamData data) throws ExecuteException {
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }

    public void setFileStreamConfig(FileStreamConfig fileStreamConfig) {
        this.fileStreamConfig = fileStreamConfig;
    }

    public void setCommonStreamConfig(CommonStreamConfig commonStreamConfig) {
        this.commonStreamConfig = commonStreamConfig;
    }

    public void setColumnConfig(ColumnConfig columnConfig) {
        this.columnConfig = columnConfig;
    }
}
