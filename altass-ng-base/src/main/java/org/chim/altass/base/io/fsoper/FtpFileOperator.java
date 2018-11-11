/*
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2017/3/7 10:23
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.io.fsoper;


import org.chim.altass.base.ftp.FtpFactory;
import org.chim.altass.base.ftp.IFtp;
import org.chim.altass.base.io.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Class Name: FtpFileOperator
 * Create Date: 2017/3/7 10:23
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class FtpFileOperator extends AbstractFileOperator {

    private IFtp ftp = null;

    /**
     * FTP文件操作器
     *
     * @param protocol FTP协议类型
     * @param user     FTP用户名
     * @param host     FTP主机
     * @param port     FTP端口
     * @param pureUrl  协议解析后的纯粹操作文件PATH
     * @param auth     FTP授权认证
     */
    public FtpFileOperator(String protocol, String user, String host, int port, String pureUrl, String auth) {
        super(protocol, user, host, port, pureUrl);

        String pro = protocol.toLowerCase();
        try {
            ftp = FtpFactory.get(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ftpLogin(user, auth);
    }

    /**
     * 登陆连接FTP
     *
     * @param user FTP用户名
     * @param pwd  登陆FTP的密码
     */
    private void ftpLogin(String user, String pwd) {
        try {
            ftp.login(host, port, user, pwd, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FtpFileOperator() {
    }

    @Override
    public boolean createFile(String path) {
        return false;
    }

    @Override
    public boolean createFile(String path, boolean force) {
        return false;
    }

    @Override
    public boolean createDir(String dir) {
        try {
            boolean result = ftp.makeDir(dir);
            ftp.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createDir(String dir, boolean force) {
        return false;
    }

    @Override
    public boolean copyFile(String src, String target) {
        return false;
    }

    @Override
    public boolean copyFile(String src, String target, boolean force) {
        return false;
    }

    @Override
    public boolean copyDir(String src, String target) {
        return false;
    }

    @Override
    public boolean copyDir(String src, String target, boolean force) {
        return false;
    }

    @Override
    public boolean writeTo(InputStream input, OutputStream output) {
        return false;
    }

    @Override
    public boolean writeTo(InputStream input, OutputStream output, boolean overwrite) {
        return false;
    }

    @Override
    public boolean writeTo(InputStream input, String target, boolean overwrite) {
        return false;
    }

    @Override
    public boolean deleteFile(String path) {
        return false;
    }

    @Override
    public boolean deleteFile(String path, boolean force) {
        return false;
    }

    @Override
    public boolean removeDirectory(String path) {
        return false;
    }

    @Override
    public boolean removeDirectory(String path, boolean force) {
        return false;
    }

    @Override
    public boolean renameFile(String path, String targetName) {
        return false;
    }

    @Override
    public List<FileInfo> listFiles(String path) {
        return null;
    }

    @Override
    public boolean isFileExist(String path) {
        return false;
    }

    @Override
    public boolean isDirectory(String path) {
        return false;
    }

    @Override
    public InputStream getInputStream(String path) {
        return null;
    }

    @Override
    public OutputStream getOutputStream(String path, boolean overwrite) {
        return null;
    }

    @Override
    public boolean append(String path, String content) {
        return false;
    }

    @Override
    public boolean append(String path, byte[] buff) {
        return false;
    }

}
