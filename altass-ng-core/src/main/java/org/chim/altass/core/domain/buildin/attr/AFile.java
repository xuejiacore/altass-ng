/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain.node
 * Author: Xuejia
 * Date Time: 2016/12/27 11:03
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: AFile
 * Create Date: 2016/12/27 11:03
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 文件属性
 */
@Elem(alias = "file", version = "1.0")
public class AFile {
    @Attr(alias = "operationMode")
    private String operationMode = null;                                        // 操作模式
    @Attr(alias = "sourceDirectory")
    private String sourceDirectory = null;                                      // 源目录
    @Attr(alias = "source")
    private String source = null;                                               // 源
    @Attr(alias = "sourceCharset")
    private String sourceCharset = "GBK";                                       // 源文件字符集
    @Attr(alias = "sourceSeparator")
    private String sourceSeparator = ",";                                       // 源文件列分隔符
    @Attr(alias = "sourceCompression")
    private String sourceCompression = "none";                                  // 源文件压缩格式
    @Attr(alias = "sourceMaxSize")
    private String sourceMaxSize = "1073741824";                                // 源文件大小限制，默认为1GB
    @Attr(alias = "destinationDirectory")
    private String destinationDirectory = null;                                 // 目标目录
    @Attr(alias = "destination")
    private String destination = null;                                          // 目标
    @Attr(alias = "destinationCharset")
    private String destinationCharset = "GBK";                                  // 目标文件字符集
    @Attr(alias = "destinationSeparator")
    private String destinationSeparator = ",";                                  // 目标文件列分隔符
    @Attr(alias = "destinationMaxSize")
    private String destinationMaxSize = "1073741824";                           // 目标文件大小限制，默认为1GB
    @Attr(alias = "failIfExists")
    private String failIfExists = null;                                         // 如果存在则失败
    @Attr(alias = "failIfNotExists")
    private String failIfNotExists = null;                                      // 如果不存在则失败
    @Attr(alias = "whetherIncludeSubfolders")
    private String whetherIncludeSubfolders = null;                             // 是否包含子目录
    @Attr(alias = "whetherRemove")
    private String whetherRemove = null;                                        // 是否删除
    @Attr(alias = "whetherOverWrite")
    private String whetherOverWrite = null;                                     // 是否覆盖
    @Attr(alias = "successOn")
    private String successOn = null;                                            // 成功标识
    @Attr(alias = "whetherMerge")
    private String whetherMerge = null;                                         // 是否合并
    @Attr(alias = "mergeName")
    private String mergeName = null;                                            // 合并名字
    @Attr(alias = "bufferSize")
    private String bufferSize = null;                                           // 文件读写缓冲区大小
    @Attr(alias = "readThreadNumber")
    private String readThreadNumber = null;                                     // 读线程数量
    @Attr(alias = "writeThreadNumber")
    private String writeThreadNumber = null;                                    // 写线程数量

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceCharset() {
        return sourceCharset;
    }

    public void setSourceCharset(String sourceCharset) {
        this.sourceCharset = sourceCharset;
    }

    public String getSourceSeparator() {
        return sourceSeparator;
    }

    public void setSourceSeparator(String sourceSeparator) {
        this.sourceSeparator = sourceSeparator;
    }

    public String getSourceCompression() {
        return sourceCompression;
    }

    public void setSourceCompression(String sourceCompression) {
        this.sourceCompression = sourceCompression;
    }

    public String getSourceMaxSize() {
        return sourceMaxSize;
    }

    public void setSourceMaxSize(String sourceMaxSize) {
        this.sourceMaxSize = sourceMaxSize;
    }

    public String getDestinationDirectory() {
        return destinationDirectory;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCharset() {
        return destinationCharset;
    }

    public void setDestinationCharset(String destinationCharset) {
        this.destinationCharset = destinationCharset;
    }

    public String getDestinationSeparator() {
        return destinationSeparator;
    }

    public void setDestinationSeparator(String destinationSeparator) {
        this.destinationSeparator = destinationSeparator;
    }

    public String getDestinationMaxSize() {
        return destinationMaxSize;
    }

    public void setDestinationMaxSize(String destinationMaxSize) {
        this.destinationMaxSize = destinationMaxSize;
    }

    public String getFailIfExists() {
        return failIfExists;
    }

    public void setFailIfExists(String failIfExists) {
        this.failIfExists = failIfExists;
    }

    public String getFailIfNotExists() {
        return failIfNotExists;
    }

    public void setFailIfNotExists(String failIfNotExists) {
        this.failIfNotExists = failIfNotExists;
    }

    public String getWhetherIncludeSubfolders() {
        return whetherIncludeSubfolders;
    }

    public void setWhetherIncludeSubfolders(String whetherIncludeSubfolders) {
        this.whetherIncludeSubfolders = whetherIncludeSubfolders;
    }

    public String getWhetherRemove() {
        return whetherRemove;
    }

    public void setWhetherRemove(String whetherRemove) {
        this.whetherRemove = whetherRemove;
    }

    public String getWhetherOverWrite() {
        return whetherOverWrite;
    }

    public void setWhetherOverWrite(String whetherOverWrite) {
        this.whetherOverWrite = whetherOverWrite;
    }

    public String getSuccessOn() {
        return successOn;
    }

    public void setSuccessOn(String successOn) {
        this.successOn = successOn;
    }

    public String getWhetherMerge() {
        return whetherMerge;
    }

    public void setWhetherMerge(String whetherMerge) {
        this.whetherMerge = whetherMerge;
    }

    public String getMergeName() {
        return mergeName;
    }

    public void setMergeName(String mergeName) {
        this.mergeName = mergeName;
    }

    public String getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(String bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getReadThreadNumber() {
        return readThreadNumber;
    }

    public void setReadThreadNumber(String readThreadNumber) {
        this.readThreadNumber = readThreadNumber;
    }

    public String getWriteThreadNumber() {
        return writeThreadNumber;
    }

    public void setWriteThreadNumber(String writeThreadNumber) {
        this.writeThreadNumber = writeThreadNumber;
    }
}
