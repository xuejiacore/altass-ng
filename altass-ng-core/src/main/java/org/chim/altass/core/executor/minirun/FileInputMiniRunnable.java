package org.chim.altass.core.executor.minirun;

import com.alibaba.fastjson.JSON;
import org.chim.altass.base.io.UniversalFileSystem;
import org.chim.altass.core.exception.ExecuteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: TailMiniRunnable
 * Create Date: 11/10/18 10:10 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FileInputMiniRunnable extends MiniRunnable {

    // Simple reader to read file content
    private BufferedReader reader;

    @SuppressWarnings("Duplicates")
    @Override
    public void run() throws ExecuteException {
        if (dataCallback == null) {
            return;
        }

        String filePath = String.valueOf(runParamMap.get("filePath"));
        reader = new BufferedReader(new InputStreamReader(UniversalFileSystem.getInputStream(filePath)));

        String line;
        boolean ignoreHeader = columnConfig != null && columnConfig.isIgnoreHeader();
        boolean dataDivisible = commonStreamConfig != null && commonStreamConfig.getDataDivisible();
        Map<Integer, String> headerMap = null;
        try {
            Map<String, Object> rowDataMap = null;
            // Target data that will be push with pipeline streaming which could be a row data, json.
            // More data structure will be support in the future.
            Object targetData;

            while ((line = reader.readLine()) != null) {

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
                dataCallback.onDataFlush(JSON.toJSONString(targetData));
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

}
