package org.chim.altass.core.domain.buildin.attr;

import com.alibaba.fastjson.JSON;

/**
 * Class Name: FileInputConfig
 * Create Date: 2017/10/26 17:05
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FileStreamConfig {
    public static final String PATTERN_TEXT = "text";
    public static final String PATTERN_JSON = "json";

    private String path = null;                         // 操作文件的路径
    private Integer flushLineCnt = null;                // 需要刷缓存数据到硬盘的行数
    private Long flushBuffSize = null;                  // 需要刷缓存数据到硬盘的字节数
    private String lineBreak = null;                    // 换行符
    private String pattern = null;                      // 数据格式，目前支持文本(text)以及json
    private String textSeparator = null;                // 如果是文本，指定行的列分隔符，默认\t
    private Boolean overwrite = false;                  // 是否允许覆盖

    public FileStreamConfig() {
        this.flushLineCnt = 10;
        this.lineBreak = "\r\n";
        this.pattern = PATTERN_TEXT;
        this.textSeparator = "\t";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getFlushLineCnt() {
        return flushLineCnt;
    }

    public void setFlushLineCnt(Integer flushLineCnt) {
        this.flushLineCnt = flushLineCnt;
    }

    public Long getFlushBuffSize() {
        return flushBuffSize;
    }

    public void setFlushBuffSize(Long flushBuffSize) {
        this.flushBuffSize = flushBuffSize;
    }

    public String getLineBreak() {
        return lineBreak;
    }

    public void setLineBreak(String lineBreak) {
        this.lineBreak = lineBreak;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getTextSeparator() {
        return textSeparator;
    }

    public void setTextSeparator(String textSeparator) {
        this.textSeparator = textSeparator;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
