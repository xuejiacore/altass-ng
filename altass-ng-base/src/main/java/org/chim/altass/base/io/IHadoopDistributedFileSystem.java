/**
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2016/12/11 14:14
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Class Name: IHadoopDistributedFileSystem
 * Create Date: 2016/12/11 14:14
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Hadoop Distributed FileInfo System 分布式文件系统接口
 * <p>
 * 该文件系统接口包含了各类的分布式文件系统的常用操作
 */
public interface IHadoopDistributedFileSystem {
    /**
     * 判断文件或者是目录是否存在
     *
     * @param path 需要判断的文件或者是目录
     * @return 如果存在那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean exists(String path) throws IOException;

    /**
     * 判断文件是否在HDFS上
     *
     * @param path 需要判断的文件路径
     * @return 如果在hdfs上，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean existsOnHdfs(String path) throws IOException;

    /**
     * 判断给定的路径是否是一个目录
     *
     * @param path 需要判定的文件路径
     * @return 如果给定的文件是一个目录，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean isDirectory(String path) throws IOException;

    /**
     * 判断目录是否在Hdfs上
     *
     * @param path 需要判定的文件路径
     * @return 如果目录在Hdfs上，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean isDirectoryOnHdfs(String path) throws IOException;

    /**
     * 判断给定的路径是否是一个文件
     *
     * @param path 需要判定的文件路径
     * @return 如果是一个文件，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean isFile(String path) throws IOException;

    /**
     * 判断文件是否在Hdfs上
     *
     * @param path 需要判定的路径
     * @return 如果给定的路径在Hdfs上，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean isFileOnHdfs(String path) throws IOException;

    /**
     * 以给定的路径创建一个目录
     *
     * @param path 需要创建的目录
     * @throws IOException
     */
    void mkdir(String path) throws IOException;

    /**
     * 在Hdfs上创建一个目录
     *
     * @param path 需要创建的文件路径
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean mkdirOnHdfs(String path) throws IOException;

    /**
     * 不覆盖在Hdfs中创建目录
     *
     * @param path 需要创建的路径
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean ignoreMkdirOnHdfs(String path) throws IOException;

    /**
     * 删除给定的文件路径
     *
     * @param path 需要删除的文件路径
     * @throws IOException
     */
    void delete(String path) throws IOException;

    /**
     * 从Hdfs中删除给定的文件路径
     *
     * @param path 需要删除的文件路径
     * @return 如果文件删除成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean deleteOnHdfs(String path) throws IOException;

    /**
     * 将文件从一个位置复制到另外一个位置
     *
     * @param src 文件源
     * @param dst 需要复制的文件目标
     * @throws IOException
     */
    void copy(String src, String dst) throws IOException;

    /**
     * 将Hdfs中的文件从一个位置复制到另外一个位置
     *
     * @param src 文件源
     * @param dst 需要复制的文件目标
     * @throws IOException
     */
    void copyOnHdfs(String src, String dst) throws IOException;

    /**
     * 复制一个文件并将对应的文件重命名添加后缀的形式
     * <p>
     * 该复制方法将会在复制的文件后添加后缀
     *
     * @param src 需要复制的文件
     * @param dst 需要复制到的文件目标
     * @throws IOException
     */
    void copyOnHdfsSuf(String src, String dst) throws IOException;

    /**
     * 将文件从本地复制到Hdfs文件系统中
     *
     * @param src 需要复制的本地文件
     * @param dst 目标路径
     * @throws IOException
     */
    void copyFromLocalToHdfs(String src, String dst) throws IOException;

    /**
     * 将文件从Hdfs文件系统中复制到本地
     *
     * @param src 需要复制的Hdfs文件
     * @param dst 目标路径
     * @throws IOException
     */
    void copyFromHdfsToLocal(String src, String dst) throws IOException;

    /**
     * 将文件从一个位置移动到另外一个路径
     *
     * @param src 需要移动的文件
     * @param dst 移动的目标路径
     * @throws IOException
     */
    void move(String src, String dst) throws IOException;

    /**
     * 在Hdfs文件系统中移动文件
     *
     * @param src 需要移动的源文件
     * @param dst 移动的目标路径
     * @throws IOException
     */
    void moveOnHdfs(String src, String dst) throws IOException;

    /**
     * 在Hdfs文件系统中移动文件，并添加后缀
     *
     * @param src 需要移动的源文件
     * @param dst 移动的目标路径
     * @throws IOException
     */
    void moveOnHdfsSuf(String src, String dst) throws IOException;

    /**
     * 从本地文件系统中移动文件到Hdfs文件系统中
     *
     * @param src 需要移动的本地文件系统中的文件路径
     * @param dst 移动的目标路径
     * @throws IOException
     */
    void moveFromLocalToHdfs(String src, String dst) throws IOException;

    /**
     * 将文件从Hdfs文件系统中移动到本地文件系统中
     *
     * @param src 需要移动的Hdfs文件系统中的文件路径
     * @param dst 移动的目标路径
     * @throws IOException
     */
    void moveFromHdfsToLocal(String src, String dst) throws IOException;

    /**
     * 重命名本地文件的名称
     *
     * @param src 需要重命名的本地文件路径
     * @param dst 重命名后的文件路径
     * @return 如果重命名成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean rename(String src, String dst) throws IOException;

    /**
     * 重命名Hdfs文件系统中文件的名称
     *
     * @param src 需要重命名的Hdfs文件路径
     * @param dst 重命名后的文件路径
     * @return 如果重命名成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean renameOnHdfs(String src, String dst) throws IOException;

    /**
     * 列出本地文件系统中的文件
     *
     * @param path 需要列出文件列表的目录
     * @return 返回文件
     * @throws IOException
     */
    List<FileInfo> listFiles(String path) throws IOException;

    /**
     * 列出本地文件系统中的文件列表
     *
     * @param path 需要列出文件列表的目录
     * @return 返回文件列表的名称
     * @throws IOException
     */
    List<String> localFileListByPath(String path) throws IOException;

    /**
     * 列出Hdfs上给定目录的文件
     *
     * @param path 需要列出的Hdfs中的文件目录
     * @return 返回文件列表
     * @throws IOException
     */
    List<FileInfo> listFilesOnHdfs(String path) throws IOException;

    /**
     * 判断文件名是否符合某一个正则匹配
     *
     * @param name    需要判定的名称
     * @param formula 正则匹配式
     * @return 如果满足匹配规则，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean matching(String name, String formula) throws IOException;

    /**
     * 列出本地文件系统中的某个文件目录中的文件列表
     *
     * @param localDir 本地文件目录
     * @return 返回文件列表
     * @throws IOException
     */
    List<String> getDirFileNames(String localDir) throws IOException;

    /**
     * 正则匹配#.*#
     *
     * @param str 需要正则匹配的字符串
     * @return 如果匹配，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean regex(String str) throws IOException;

    /**
     * 列出Hdfs文件系统中的文件列表
     *
     * @param localDir Hdfs文件系统的目录
     * @return
     * @throws IOException
     */
    List<String> getDirFileNamesOnHdfs(String localDir) throws IOException;

    /**
     * 从Hdfs中读取指定文件路径的行
     *
     * @param filePath   文件目录
     * @param rows       行数
     * @param ignoreFile 是否忽略文件名
     * @return 返回读取的文件内容
     * @throws IOException
     */
    String readFromHdfs(String filePath, String rows, boolean ignoreFile) throws IOException;

    /**
     * io到Hdfs 文件系统中
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    void writeToHdfs(InputStream in, OutputStream out) throws IOException;

    /**
     * io到Hdfs 文件系统中
     *
     * @param in        输入流
     * @param dst       目标路径
     * @param overwrite 是否覆盖
     * @throws IOException
     */
    void writeToHdfs(InputStream in, String dst, boolean overwrite) throws IOException;

    /**
     * io到Hdfs 文件系统中（覆盖方式）
     *
     * @param in  输入流
     * @param dst 目标路径
     * @throws IOException
     */
    void writeToHdfs(InputStream in, String dst) throws IOException;

    /**
     * 将文本内容io到Hdfs 文件系统中（覆盖方式）
     *
     * @param content 需要写入的内容
     * @param dst     需要写入的目标
     * @throws IOException
     */
    void writeToHdfs(String content, String dst) throws IOException;

    /**
     * 将文本内容追加到Hdfs文件系统中对应的文件后
     *
     * @param content 需要追加的文件内容
     * @param dst     需要追加的目标文件
     * @throws IOException
     */
    void appendStringToHdfs(String content, String dst) throws IOException;

    /**
     * 将Hdfs中的文件以字符串的形式读取出来
     *
     * @param dst 需要读取的目标文件
     * @return 返回Hdfs对应文件中的文件内容
     * @throws IOException
     */
    String readStringFromHdfs(String dst) throws IOException;

    /**
     * 列出给定路径下的所有文件
     *
     * @param path 文件路径
     * @return 文件列表
     * @throws IOException
     */
    List<String> listAllFiles(String path) throws IOException;

    /**
     * 列出Hdfs文件系统中的所有文件列表
     *
     * @param path  需要列出的文件目录
     * @param limit 列出数量上限
     * @return 返回Hdfs文件系统中的文件列表
     * @throws IOException
     */
    List<String> catAllFiles(String path, int limit) throws IOException;

    /**
     * 在Hdfs文件系统中解压文件
     *
     * @param source 需要解压的文件路径
     * @param dest   需要解压到的目标文件
     * @return 如果解压成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean unZipOnHdfs(String source, String dest) throws IOException;
}
