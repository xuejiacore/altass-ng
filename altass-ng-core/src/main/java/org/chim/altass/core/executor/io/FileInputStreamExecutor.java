package org.chim.altass.core.executor.io;


import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.base.io.UniversalFileSystem;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.executor.config.ColumnConfig;
import org.chim.altass.core.domain.buildin.attr.CommonStreamConfig;
import org.chim.altass.core.domain.buildin.attr.FileStreamConfig;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractPipelineExecutor;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: FileStreamExecutor
 * Create Date: 2017/10/26 17:04
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 文件输入流执行器
 * 持有流式执行器特性：接收前驱流式管道流的输入
 * 持有分布式执行能力：能够汇聚输入入度 >1 的分布式执行汇聚，将多个
 */
@Executable(name = "fileInputStreamExecutor", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING, ExecutorAbility.ABILITY_DISTRIBUTION})
@Resource(name = "FileInput", clazz = FileInputStreamExecutor.class, groupName = "常规", midImage = "res/images/node/pipeline_input_bg.png", pageUrl = "nodeConfigs/core/inputFileNodeConfig.jsp")
public class FileInputStreamExecutor extends AbstractPipelineExecutor {

    // File stream configuration
    @RuntimeAutowired
    private FileStreamConfig fileStreamConfig = null;

    // Column split rule configuration
    @RuntimeAutowired
    private ColumnConfig columnConfig = null;

    // Basic common stream configuration
    @RuntimeAutowired
    private CommonStreamConfig commonStreamConfig = null;

    // Simple reader to read file content
    private BufferedReader reader = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public FileInputStreamExecutor(String executeId) throws ExecuteException {
        super(executeId);
        commonStreamConfig = new CommonStreamConfig();
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    protected boolean onPipelineInit() throws ExecuteException {
        String path = fileStreamConfig.getPath();
        reader = new BufferedReader(new InputStreamReader(UniversalFileSystem.getInputStream(path)));
        return true;
    }

    /**
     * Read content from file with pipeline streaming line by line.
     *
     * @throws ExecuteException -
     */
    @SuppressWarnings("Duplicates")
    @Override
    protected void dataSource() throws ExecuteException {
        String line;
        boolean ignoreHeader = columnConfig != null && columnConfig.isIgnoreHeader();
        boolean dataDivisible = commonStreamConfig.getDataDivisible();
        Map<Integer, String> headerMap = null;
        try {
            Map<String, Object> rowDataMap = null;
            // Target data that will be push with pipeline streaming which could be a row data, json.
            // More data structure will be support in the future.
            Object targetData;

            while ((line = reader.readLine()) != null) {
                EXECUTOR_LOGGER("msg", "Input Stream Data", "rowData", line);

                if (!ignoreHeader) {
                    // process first line of the file, map column name to a map
                    ignoreHeader = true;
                    if (columnConfig != null && columnConfig.isContainColumnName() && dataDivisible) {
                        // Map first line as column name mapping.
                        String textSeparator = commonStreamConfig.getTextSeparator();
                        String[] split = line.split(textSeparator);
                        headerMap = new HashMap<>();
                        for (int i = 0; i < split.length; i++) {
                            headerMap.put(i, split[i]);
                        }
                        // prepare data mapping
                        rowDataMap = new HashMap<>();
                    }
                    continue;
                }

                // Structured data will be assemble when it was configured divisible and map first line.
                if (dataDivisible && headerMap != null) {
                    // clear last operated data mapping
                    rowDataMap.clear();
                    // A structure data will be push that had split row data and mapped to header.
                    String[] columnData = line.split(commonStreamConfig.getTextSeparator());
                    for (int columnIdx = 0; columnIdx < columnData.length; columnIdx++) {
                        rowDataMap.put(headerMap.get(columnIdx), columnData[columnIdx]);
                    }
                    targetData = rowDataMap;
                } else {
                    // Simple row data will be push.
                    targetData = line;
                }

                // execute data pushing
                this.pushData(new StreamData(this.executeId, null, targetData));
            }
        } catch (IOException e) {
            throw new ExecuteException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rollback(StreamData data) {

    }
}
