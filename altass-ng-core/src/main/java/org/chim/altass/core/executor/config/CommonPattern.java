package org.chim.altass.core.executor.config;

/**
 * Class Name: DataPattern
 * Create Date: 11/13/18 12:57 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 通用基础数据配置，用于处理行数据的数据切分
 */
public class CommonPattern {

    public static final int DATA_STRUCT_TEXT = 0x2;
    public static final int DATA_STRUCT_JSON = 0x4;

    /**
     * 默认的文本分隔符
     */
    public static final String DEFAULT_TEXT_DATA_STRUCT_SEPARATOR = "\t";
    /**
     * 文本分隔符，默认为制表符 \t
     */
    private String textSeparator = DEFAULT_TEXT_DATA_STRUCT_SEPARATOR;
    /**
     * 列分割配置
     */
    private ColumnConfig columnPattern = null;

    /**
     * 数据结构，默认为文本
     */
    private int dataStruct = DATA_STRUCT_TEXT;
    /**
     * 是否进行数据分割
     */
    private boolean dataDivisible = false;
    /**
     * 是否结构化数据
     */
    private boolean structuring = false;
    /**
     * 批量数量
     */
    private Long batchSize = 0L;

    public ColumnConfig getColumnPattern() {
        return columnPattern;
    }

    public void setColumnPattern(ColumnConfig columnPattern) {
        this.columnPattern = columnPattern;
    }

    public int getDataStruct() {
        return dataStruct;
    }

    public void setDataStruct(int dataStruct) {
        this.dataStruct = dataStruct;
    }

    public String getTextSeparator() {
        return textSeparator;
    }

    public void setTextSeparator(String textSeparator) {
        this.textSeparator = textSeparator;
    }

    public boolean isDataDivisible() {
        return dataDivisible;
    }

    public void setDataDivisible(boolean dataDivisible) {
        this.dataDivisible = dataDivisible;
    }

    public boolean isStructuring() {
        return structuring;
    }

    public void setStructuring(boolean structuring) {
        this.structuring = structuring;
    }

    public Long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Long batchSize) {
        this.batchSize = batchSize;
    }
}
