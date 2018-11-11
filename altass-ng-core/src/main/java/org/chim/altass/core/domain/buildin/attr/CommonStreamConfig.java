package org.chim.altass.core.domain.buildin.attr;

/**
 * Class Name: CommonStreamConfig
 * Create Date: 2017/10/26 19:34
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 常用流处理配置
 */
public class CommonStreamConfig {
    public static final int DATA_STRUCT_TEXT = 0x2;
    public static final int DATA_STRUCT_JSON = 0x4;
    public static final String DEFAULT_TEXT_DATA_STRUCT_SEPARATOR = "\t";

    private int dataStruct = DATA_STRUCT_TEXT;
    private String textSeparator = DEFAULT_TEXT_DATA_STRUCT_SEPARATOR;

    private boolean dataDivisible = false;
    private boolean structuring = false;

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

    public boolean getDataDivisible() {
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
}
