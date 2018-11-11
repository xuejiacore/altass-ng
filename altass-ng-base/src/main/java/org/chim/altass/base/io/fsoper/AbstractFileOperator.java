/*
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2017/3/6 14:13
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.io.fsoper;


import org.chim.altass.base.io.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Class Name: AbstractFileOperator
 * Create Date: 2017/3/6 14:13
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractFileOperator {

    protected String protocol = null;
    protected String user = null;
    protected String host = null;
    protected int port = 22;
    protected String pureUrl = null;

    public AbstractFileOperator(String protocol, String user, String host, int port, String pureUrl) {
        this.protocol = protocol;
        this.user = user;
        this.host = host;
        this.pureUrl = pureUrl;
        this.port = port;
    }

    public AbstractFileOperator() {
    }

    /**
     * 获得文件系统协议
     *
     * @return 文件系统协议
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * 设置文件系统协议
     *
     * @param protocol 文件系统协议
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * 获得文件系统协议的操作用户
     *
     * @return 文件系统协议用户
     */
    public String getUser() {
        return user;
    }

    /**
     * 设置文件系统协议的操作用户
     *
     * @param user 需要设置的文件系统协议用户
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 获得文件系统的host
     *
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置文件系统的host
     *
     * @param host host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获得剥离文件协议以及用户名、端口等信息后的纯文件路径
     *
     * @return 文件路径
     */
    public String getPureUrl() {
        return pureUrl;
    }

    /**
     * 设置剥离文件协议以及用户名、端口等信息后的纯文件路径
     *
     * @param pureUrl 文件路径
     */
    public void setPureUrl(String pureUrl) {
        this.pureUrl = pureUrl;
    }

    /**
     * 获得操作的文件系统的端口
     *
     * @return 端口号
     */
    public int getPort() {
        return port;
    }

    /**
     * 设置操作的文件系统的端口
     *
     * @param port 端口号
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 创建文件
     *
     * @param path 文件的全路径
     * @return 如果文件创建成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean createFile(String path);

    /**
     * 创建文件
     *
     * @param path  文件的全路径
     * @param force 是否强制创建，如果为true，那么当文件存在的时候将会重新创建，如果为false，那么不覆盖
     * @return 如果文件创建成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean createFile(String path, boolean force);

    /**
     * 创建一个目录
     *
     * @param dir 需要创建的目录
     * @return 如果目录创建成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean createDir(String dir);

    /**
     * 创建一个目录
     *
     * @param dir   需要创建的目录
     * @param force 是否强制创建
     * @return 如果目录创建成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean createDir(String dir, boolean force);

    /**
     * 将文件从一个位置复制到另外一个位置
     *
     * @param src    被复制的源文件
     * @param target 复制的目标位置
     * @return 如果复制成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean copyFile(String src, String target) throws IOException;

    /**
     * 将文件从一个位置复制到另外一个位置
     *
     * @param src    被复制的源文件
     * @param target 复制的目标位置
     * @param force  是否进行强制复制
     * @return 如果复制成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean copyFile(String src, String target, boolean force) throws IOException;

    /**
     * 将一个目录从一个位置复制到另外一个位置
     *
     * @param src    被复制的源文件
     * @param target 复制的目标目录
     * @return 如果复制成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean copyDir(String src, String target);

    /**
     * 将一个目录从一个位置复制到另外一个位置
     *
     * @param src    被复制的源文件
     * @param target 复制的目标目录
     * @param force  是否强制复制
     * @return 如果复制成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean copyDir(String src, String target, boolean force);

    /**
     * 将一个输入流中的数据写入到输出流中
     *
     * @param input  输入流
     * @param output 输出流
     * @return 如果操作成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean writeTo(InputStream input, OutputStream output);

    /**
     * 将一个输入流中的数据写入到输出流中
     *
     * @param input     输入流
     * @param output    输出流
     * @param overwrite 是否允许覆盖目标文件
     * @return 如果输出流写成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean writeTo(InputStream input, OutputStream output, boolean overwrite) throws IOException;

    /**
     * 将一个输入流中的数据写入到目标路径中
     *
     * @param input     输入流
     * @param target    输出流
     * @param overwrite 是否允许覆盖目标文件
     * @return 如果输出流写成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean writeTo(InputStream input, String target, boolean overwrite);

    /**
     * 删除文件
     *
     * @param path 需要删除的文件路径
     * @return 如果文件删除成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean deleteFile(String path);

    /**
     * 删除文件
     *
     * @param path  需要删除的文件路径
     * @param force 是否进行强制删除
     * @return 如果删除成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean deleteFile(String path, boolean force);

    /**
     * 删除一个目录
     *
     * @param path 需要删除的目录的路径
     * @return 如果删除成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean removeDirectory(String path);

    /**
     * 删除一个目录
     *
     * @param path  需要删除的目录的路径
     * @param force 如果为true，那么为强制删除（包含目录中的文件都会对应被删除）
     * @return 如果删除成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean removeDirectory(String path, boolean force);

    /**
     * 重命名文件
     *
     * @param path       原文件全路径
     * @param targetName 新的文件名称
     * @return 如果重命名成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean renameFile(String path, String targetName);

    /**
     * 列出文件指定路径下的文件列表
     *
     * @param path 需要列出的文件路径
     * @return 返回文件列表
     */
    public abstract List<FileInfo> listFiles(String path);

    /**
     * 判断文件是否存在
     *
     * @param path 需要判断的文件全路径
     * @return 如果文件存在，那么返回值为true，否则返回值为false
     */
    public abstract boolean isFileExist(String path);

    /**
     * 判断是否是目录
     *
     * @param path 需要判断的文件全路径
     * @return 如果指定文件是目录，那么返回值为true，否则返回值为false
     */
    public abstract boolean isDirectory(String path);

    /**
     * 获得文件的输入流
     *
     * @param path 文件的路径
     * @return 文件的输入流
     */
    public abstract InputStream getInputStream(String path);

    /**
     * 获得文件的输出流
     *
     * @param path      文件的路径
     * @param overwrite 输出流是否允许覆盖
     * @return 文件的输出流
     */
    public abstract OutputStream getOutputStream(String path, boolean overwrite) throws IOException;

    /**
     * 向一个文件中追加文本内容
     *
     * @param path    需要追加的广义文件路径
     * @param content 需要追加的文件的内容
     * @return 如果内容追加成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean append(String path, String content) throws IOException;

    /**
     * 向一个文件中追加字节内容
     *
     * @param path 需要追加的广义文件路径
     * @param buff 需要追加的文件的字节流
     * @return 如果字节追加成功，那么返回值为true，否则返回值为false
     */
    public abstract boolean append(String path, byte[] buff);
}
