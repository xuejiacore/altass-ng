/*
 * Project: x-framework
 * Package Name: org.ike.core.ftp
 * Author: Xuejia
 * Date Time: 2017/3/7 14:28
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.ftp;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Class Name: IFtp
 * Create Date: 2017/3/7 14:28
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IFtp {
    /**
     * PassiveMode
     */
    void enterLocalPassiveMode();

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * 判断FTP连接是否打开
     *
     * @return 如果连接打开，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean isOpenFTPConnection() throws IOException;

    /**
     * 将文件下载到本地
     *
     * @param localFilePath  本地文件的路径
     * @param remoteFilePath 远程文件的路径
     * @return 如果文件获取成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean downloadFile(String localFilePath, String remoteFilePath) throws IOException;

    /**
     * 将文件上传到HDFS
     *
     * @param localFilePath  本地文件的路径
     * @param remoteFileName 远程文件的路径
     * @return 如果文件上传成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean downFileToHdfs(String localFilePath, String remoteFileName) throws IOException;

    /**
     * 将文件下载到本地
     *
     * @param localFile      本地文件
     * @param remoteFileName 远程文件路径
     * @return 如果文件下载成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean downloadFile(File localFile, String remoteFileName) throws IOException;

    /**
     * 将文件上传到远程目录中
     *
     * @param localFilePath  需要上传的本地文件
     * @param remoteFileName 需要上传至的目标文件
     * @return 如果上传操作成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean uploadFile(String localFilePath, String remoteFileName) throws IOException;

    /**
     * 从HDFS中传输文件到指定的目录
     *
     * @param localFilePath  需要传输的本地文件
     * @param remoteFileName 需要传输到的远程文件
     * @return 如果传输成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean uploadFileFormHdfs(String localFilePath, String remoteFileName) throws IOException;

    /**
     * 上传文件到制定目录
     *
     * @param localFile      本地文件
     * @param remoteFileName 远程文件路径
     * @return 如果上传成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean uploadFile(File localFile, String remoteFileName) throws IOException;

    /**
     * 改变目录
     *
     * @param remoteDir 改变的远程目录
     * @throws IOException
     */
    void changeDir(String remoteDir) throws IOException;

    /**
     * 改变当前的工作目录
     *
     * @param remoteDir 需要更改的工作目录
     * @return 如果目录更改成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean changeWorkingDir(String remoteDir) throws IOException;

    /**
     * 跳转到父层目录中
     *
     * @throws IOException
     */
    void toParentDir() throws IOException;

    /**
     * 获得文件名称列表
     *
     * @return 如果获取成功，那么返回文件名称列表，否则返回值为null
     * @throws IOException
     */
    String[] getListNames() throws IOException;

    /**
     * 获得FTP文件列表
     *
     * @return 如果文件获取成功，那么返回文件列表数组，否则返回值为null
     * @throws IOException
     */
    FTPFile[] getListFiles() throws IOException;

    /**
     * 获得文件列表名称
     *
     * @return 如果获取成功，那么返回文件列表，否则返回值为null
     * @throws IOException
     */
    List<String> getListFileNames() throws IOException;

    /**
     * 根据文件名称查找FTP文件
     *
     * @param name 文件名称
     * @return 如果文件查找到，那么返回该文件对象，否则返回值为null
     * @throws IOException
     */
    FTPFile findFile(String name) throws IOException;

    /**
     * 删除FTP上的文件
     *
     * @param fileName 需要删除的文件的名称
     * @return 如果文件删除成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean deleteFile(String fileName) throws IOException;

    /**
     * 删除一个目录
     *
     * @param dir 需要删除的目录
     * @return 如果删除成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean removeDir(String dir) throws IOException;

    /**
     * 删除FTP上的一批文件
     *
     * @param fileNames 文件名称
     * @return 如果文件删除成功，对应删除结果为true，否则对应删除结果为false
     * @throws IOException
     */
    Map<String, Boolean> deleteFiles(String[] fileNames) throws IOException;

    /**
     * 重命名文件
     *
     * @param oldFileName 旧文件路径
     * @param newFileName 新文件路径和名称
     * @return 如果重命名成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean renameFile(String oldFileName, String newFileName) throws IOException;

    /**
     * @param str
     * @return
     * @throws IOException
     */
    boolean regex(String str) throws IOException;

    /**
     * 获得指定文件的输入流
     *
     * @param fileName 文件名称
     * @return 返回文件的输入流
     * @throws IOException
     */
    InputStream getInputStream(String fileName) throws IOException;

    /**
     * 获得制定文件的输出流
     *
     * @param fileName 文件名
     * @return 获得文件的输出流
     * @throws IOException
     */
    OutputStream getOutputStream(String fileName) throws IOException;

    /**
     * @param minPort
     * @param maxPort
     * @throws IOException
     */
    void setActivePortRange(int minPort, int maxPort) throws IOException;

    /**
     *
     */
    void enterLocalActiveMode();

    /**
     * 登录FTP服务器
     *
     * @param host     登录主机
     * @param port     登录端口
     * @param username 登录用户名
     * @param password 登录密码
     * @param mode     登录模式
     * @return 如果登录成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean login(String host, int port, String username, String password, String mode) throws IOException;

    /**
     * 获得文件列表
     *
     * @return 返回文件列表
     * @throws IOException
     */
    Vector listFiles() throws IOException;

    /**
     * 在sftp服务器中查找文件
     *
     * @param fileName 文件名称
     * @return 返回查找到的文件名称
     * @throws IOException
     */
    String findFileOnSftp(String fileName) throws IOException;

    /**
     * 在FTP上制定路径创建目录
     *
     * @param path 需要创建的路径
     * @return 如果目录创建成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    boolean makeDir(String path) throws IOException;

    /**
     * @return
     * @throws IOException
     */
    boolean completePendingCommand() throws IOException;
}
