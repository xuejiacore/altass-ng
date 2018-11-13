package org.chim.altass.core.executor.config;

import java.util.List;

/**
 * Class Name: ColumnConfig
 * Create Date: 2017/10/26 19:40
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ColumnConfig {
    private boolean ignoreHeader = true;
    private boolean containColumnName = false;
    private List<String> columnList = null;

    public boolean isIgnoreHeader() {
        return ignoreHeader;
    }

    public void setIgnoreHeader(boolean ignoreHeader) {
        this.ignoreHeader = ignoreHeader;
    }

    public boolean isContainColumnName() {
        return containColumnName;
    }

    public void setContainColumnName(boolean containColumnName) {
        this.containColumnName = containColumnName;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }
}
