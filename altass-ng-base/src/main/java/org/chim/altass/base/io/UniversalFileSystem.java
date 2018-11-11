/*
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2017/3/6 11:11
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.io;

import org.apache.log4j.Logger;
import org.chim.altass.base.io.fsoper.AbstractFileOperator;
import org.chim.altass.base.io.fsoper.FtpFileOperator;
import org.chim.altass.base.io.fsoper.HDFSOperator;
import org.chim.altass.base.io.fsoper.LocalFileOperator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: FileOperator
 * Create Date: 2017/3/6 11:11
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 泛文件操作助手类
 * <p>
 * 允许在操作的文件路径上通过协议修饰，自动识别并使用对应的文件系统操作器进行文件操作<br/>
 * <font color='yellow'>FTP:</font>ftp://chim:darkkidzxj29@192.168.145.135:21:/home/chim/testbyjava2<br/>
 * <font color='yellow'>SFTP:</font>sftp://chim:darkkidzxj29@192.168.145.135:22:/home/chim/testbyjava2<br/>
 * <font color='yellow'>FILE:</font>/home/chim/testbyjava2<br/>
 * <font color='yellow'>HDFS:</font>hdfs://moplabmaster:9000:/test/chim/foo<br/>
 */
public class UniversalFileSystem {

    private static final Logger log = Logger.getLogger(UniversalFileSystem.class);

    /**
     * 匹配：
     * <font color='yellow'><u>protocol</u></font>://<font color='#0099cc'><u>user:password</u></font>@<font color='blue'><i>host:port</i></font>:/home/foo/bar/demo
     * <p>
     * protocol: file://    ftp://    sftp://
     * user: 使用ftp、sftp可以指定用户名
     * password: 使用ftp、sftp可以指定密码
     * host: 需要操作的主机
     * port: 端口号
     * <p>
     * 剩下的部分为操作路径
     */
    //                                                  协议          用户                  密码                                          IP                                                                                   端口                 路径
    private static Pattern pattern = Pattern.compile("^((\\w+)://)?((\\w+)(:([a-zA-Z0-9!@#$%^&*()_+-=~`]+))?@)?((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))(:(\\d+))?)?(?::)?([a-zA-Z0-9/_\\.]+)?");

    private static Pattern hdfsPattern = Pattern.compile("^(\\w+)://((\\d+\\.\\d+\\.\\d+\\.\\d+)|(\\w+))(:(\\d+))?(?::)?([a-zA-Z0-9/_\\.]+)?");

    /**
     * 创建一个目录
     *
     * @param path 需要创建的路径
     */
    public static boolean createDirectory(String path) {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.createDir(operator.getPureUrl());
    }

    /**
     * 获得对应目录下的文件列表
     *
     * @param path 需要获取的路径
     * @return 文件列表
     */
    public static List<FileInfo> listFiles(String path) {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.listFiles(operator.getPureUrl());
    }

    /**
     * 删除一个目录
     *
     * @param path 需要删除的目录路径
     */
    public static boolean removeDirectory(String path) {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.deleteFile(path);
    }

    /**
     * 重命名一个目录s
     * 该方法能够作为广义目录的copy、传输，即：只要两个参数的文件协议不一致，就可以实现从第一个参数中获得目录对象，同时讲第二个参数指定
     * 的文件系统中传输目录，实现文件目录的跨协议传输转化
     *
     * @param path       需要重命名的目录
     * @param targetName 重命名后的目录
     */
    public static void renameDirectory(String path, String targetName) {
        AbstractFileOperator operator = protocolAnalysis(path);
        operator.renameFile(operator.getPureUrl(), targetName);
    }

    /**
     * 在一个广义的文件路径上创建一个文件对象
     *
     * @param path 需要创建的广义文件的路径
     */
    public static void createFile(String path) {
        AbstractFileOperator operator = protocolAnalysis(path);
    }

    /**
     * 删除一个广义文件
     *
     * @param path 需要删除的广义的文件路径
     */
    public static boolean deleteFile(String path) {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.deleteFile(path);
    }

    /**
     * 重命名广义文件的文件名
     * 该方法能够作为广义文件的copy，即：只要两个参数的文件协议不一样，就可以实现从第一个参数中获得文件对象，同时向第二个参数指定的文件
     * 系统中输出文件，实现文件跨协议的传输转化
     *
     * @param path       需要重命名的文件所在路劲
     * @param targetName 重命名后的文件路径和名称
     */
    public static void renameFile(String path, String targetName) {
        AbstractFileOperator operator = protocolAnalysis(path);
        operator.renameFile(operator.getPureUrl(), targetName);
    }

    /**
     * 复制文件到目标路径，可选是否覆盖
     *
     * @param srcPath    需要复制的源文件路径
     * @param targetPath 需要复制的目标文件路径
     * @param overwrite  是否允许覆盖
     * @return 如果文件复制成功，那么返回值为true，否则返回值为false
     */
    public static boolean copyFile(String srcPath, String targetPath, boolean overwrite) throws IOException {
        AbstractFileOperator srcOperator = protocolAnalysis(srcPath);
        AbstractFileOperator targetOperator = protocolAnalysis(targetPath);

        if (srcOperator.getProtocol().equals(targetOperator.getProtocol())) {
            // 文件系统一致，使用src的文件系统进行操作
            try {
                return srcOperator.copyFile(srcOperator.getPureUrl(), targetOperator.getPureUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 文件系统不一致，进行跨文件系统cp实现
            InputStream srcInputStream = srcOperator.getInputStream(srcOperator.getPureUrl());
            OutputStream outputStream = targetOperator.getOutputStream(targetOperator.getPureUrl(), overwrite);
            return targetOperator.writeTo(srcInputStream, outputStream, overwrite);
        }
        return false;
    }

    /**
     * 复制文件到目标路径，不强制覆盖文件
     *
     * @param srcPath    需要复制的源文件路径
     * @param targetPath 需要复制到的目标文件路径
     * @return 如果复制成功，那么返回值为true，否则返回值为false
     */
    public static boolean copyFile(String srcPath, String targetPath) throws IOException {
        return copyFile(srcPath, targetPath, false);
    }

    /**
     * 可选是否覆盖获得文件的输出流
     *
     * @param path      需要获得输出流的文件对象
     * @param overwrite 是否允许覆盖输出流
     * @return 文件输出流对象实例
     */
    public static OutputStream getOutputStream(String path, boolean overwrite) throws IOException {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.getOutputStream(operator.getPureUrl(), overwrite);
    }

    /**
     * 不覆盖获得文件输出流
     *
     * @param path 需要获得输出流的fs路径
     * @return 获得文件的输出流
     */
    public static OutputStream getOutputStream(String path) throws IOException {
        return getOutputStream(path, false);
    }


    /**
     * 删除多个文件
     *
     * @param delFileList 需要删除的广义文件路径列表
     */
    public static void rmFiles(List<String> delFileList) {

    }

    /**
     * 获得文件的输入流对象
     *
     * @param path 需要获取输入流的广义文件路径
     * @return 文件对应的输入流对象
     */
    public static InputStream getInputStream(String path) {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.getInputStream(operator.getPureUrl());
    }

    /**
     * 向一个广义的文件对象末尾追加文本
     *
     * @param path    需要追加的广义文件路径
     * @param content 需要追加的文件内容
     * @return 如果文件追加成功，那么返回值为true，否则返回值为false
     */
    public static boolean append(String path, String content) throws IOException {
        AbstractFileOperator operator = protocolAnalysis(path);
        return operator.append(operator.getPureUrl(), content);
    }

    /**
     * 文件操作协议解析
     * <p>
     * 允许携带用户及IP进行文件写操作
     *
     * @param url 文件路径
     * @return 返回实际的操作实现
     */
    public static AbstractFileOperator protocolAnalysis(String url) {
        String protocol = null;                                         // 操作的协议
        String user = null;                                             // 操作的用户
        String auth = null;                                             // 操作用户对应的密码
        String host = null;                                             // 操作的主机IP
        int port = 22;                                                  // 操作的主机端口
        String pureUrl = null;                                          // 操作的真正文件系统的路径
        if (url.startsWith("hdfs")) {
            Matcher matcher = hdfsPattern.matcher(url);
            if (matcher.find()) {
                protocol = matcher.group(1);
                host = matcher.group(2);
                String tmpPort = matcher.group(6);
                port = tmpPort == null ? 9000 : Integer.parseInt(tmpPort);
                pureUrl = matcher.group(7);
            }
        } else {
            Matcher matcher = pattern.matcher(url);                         // 解析操作的地址
            boolean regexFind = matcher.find();
            // 根据正则匹配的结果取得相关的信息
            if (regexFind) {
                protocol = matcher.group(2);
                user = matcher.group(4);
                auth = matcher.group(6);
                host = matcher.group(7);
                String tmpPort = matcher.group(15);
                port = tmpPort == null ? 22 : Integer.parseInt(tmpPort);
                pureUrl = matcher.group(16);

            } else {
                pureUrl = url;
            }
        }

        if (protocol == null) {
            protocol = "file";
        }
        if (host == null) {
            host = "localhost";
        }
        if (user == null) {
            user = "root";
        }

        AbstractFileOperator operator = null;
        log.debug("protocol:" + protocol + " | user:" + user + " | auth:" + auth + " | host:" + host + " | port:" + port + " | pureUrl:" + pureUrl);

        // 根据操作的协议自动选择对应的文件操作实现
        if ("file".equals(protocol)) {
            // 本地文件系统
            operator = new LocalFileOperator(protocol, user, host, port, pureUrl);

        } else if ("ftp".equals(protocol) || "sftp".equals(protocol)) {
            // FTP或SFTP文件操作
            if (auth != null) {
                operator = new FtpFileOperator(protocol, user, host, port, pureUrl, auth);
            } else {
                throw new IllegalArgumentException("Authorization information must not be null!");
            }

        } else if ("hdfs".equals(protocol)) {
            // HDFS文件系统
            operator = new HDFSOperator(protocol, user, host, port, pureUrl);
        }

        return operator;
    }


}
