/*
 * Project: x-framework
 * Package Name: org.ike.core.ftp
 * Author: Xuejia
 * Date Time: 2017/3/7 14:34
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: FTPImpl
 * Create Date: 2017/3/7 14:34
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * FTP操作实现
 */
public class FTPImpl implements IFtp {
    private FTPClient ftp;

    @Override
    public void enterLocalPassiveMode() {
        this.ftp.enterLocalPassiveMode();
    }

    @Override
    public void close() throws IOException {
        if (this.ftp != null && this.ftp.isConnected()) {
            this.ftp.logout();
            this.ftp.disconnect();
        }
    }

    @Override
    public boolean isOpenFTPConnection() throws IOException {
        return this.ftp != null && this.ftp.isConnected();
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) throws IOException {
        BufferedOutputStream outStream = null;
        boolean success;

        try {
            outStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
            success = this.ftp.retrieveFile(remoteFilePath, outStream);
        } finally {
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
        }

        return success;
    }

    @Override
    public boolean downFileToHdfs(String localFilePath, String remoteFileName) throws IOException {
        InputStream ftpIn = null;
        FSDataOutputStream hadoopOut = null;
        Configuration conf = new Configuration();
        conf.setBoolean("dfs.support.append", true);
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);
        FileSystem fs = FileSystem.get(URI.create(localFilePath), conf);

        try {
            ftpIn = this.ftp.retrieveFileStream(remoteFileName);
            if (ftpIn != null) {
                hadoopOut = fs.create(new Path(localFilePath));
                int bufferedOutputStreamSize = 1024;
                IOUtils.copyBytes(ftpIn, hadoopOut, bufferedOutputStreamSize);
                return true;
            }

            String e = remoteFileName.substring(0, remoteFileName.lastIndexOf("/"));
            this.ftp.changeWorkingDirectory(e);
            if (this.findFile(remoteFileName) != null) {
                throw new IOException();
            }

            System.out.println(remoteFileName + " is not exist!");
        } catch (Exception e) {
            String dir = remoteFileName.substring(0, remoteFileName.lastIndexOf("/"));
            this.ftp.changeWorkingDirectory(dir);
            if (this.findFile(remoteFileName) == null) {
                System.out.println(remoteFileName + " is not exist!");
                return true;
            }

            throw new IOException();
        } finally {
            if (ftpIn != null) {
                if (this.ftp.completePendingCommand()) {
                    ftpIn.close();
                } else {
                    throw new IOException();
                }
                IOUtils.closeStream(ftpIn);
            }

            if (hadoopOut != null) {
                hadoopOut.flush();
                hadoopOut.close();
            }
        }
        return true;
    }

    @Override
    public boolean downloadFile(File localFile, String remoteFileName) throws IOException {
        BufferedOutputStream outStream = null;
        FileOutputStream outStr = null;
        boolean success;
        try {
            outStr = new FileOutputStream(localFile);
            outStream = new BufferedOutputStream(outStr);
            success = this.ftp.retrieveFile(remoteFileName, outStream);
        } finally {
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
            if (outStr != null) {
                outStr.flush();
                outStr.close();
            }
        }
        return success;
    }

    @Override
    public boolean uploadFile(String localFilePath, String remoteFileName) throws IOException {
        BufferedInputStream inStream = null;
        boolean success;

        try {
            inStream = new BufferedInputStream(new FileInputStream(localFilePath));
            success = this.ftp.storeFile(remoteFileName, inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }

        }

        return success;
    }

    @Override
    public boolean uploadFileFormHdfs(String localFilePath, String remoteFileName) throws IOException {
        Configuration conf = new Configuration();
        Path dstPath = new Path(localFilePath);
        FileSystem dstFs = dstPath.getFileSystem(conf);
        FSDataInputStream hadoopIn = dstFs.open(dstPath);
        return this.ftp.storeFile(remoteFileName, hadoopIn);
    }

    @Override
    public boolean uploadFile(File localFile, String remoteFileName) throws IOException {
        BufferedInputStream inStream = null;
        boolean success;

        try {
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            success = this.ftp.storeFile(remoteFileName, inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }

        }

        return success;
    }

    @Override
    public void changeDir(String remoteDir) throws IOException {
        this.ftp.changeWorkingDirectory(remoteDir);
    }

    @Override
    public boolean changeWorkingDir(String remoteDir) throws IOException {
        return this.ftp.changeWorkingDirectory(remoteDir);
    }

    @Override
    public void toParentDir() throws IOException {
        this.ftp.changeToParentDirectory();
    }

    @Override
    public String[] getListNames() throws IOException {
        return this.ftp.listNames();
    }

    @Override
    public FTPFile[] getListFiles() throws IOException {
        return this.ftp.listFiles();
    }

    @Override
    public List<String> getListFileNames() throws IOException {
        FTPFile[] files = this.getListFiles();
        if (files != null && files.length > 0) {
            ArrayList<String> fileList = new ArrayList<>();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                }
            }
            return fileList;
        } else {
            return null;
        }
    }

    @Override
    public FTPFile findFile(String name) throws IOException {
        int f = name.lastIndexOf("/");
        if (f > 0) {
            name = name.substring(f + 1);
        } else if (f == 0) {
            name = name.replace("/", "");
        }

        FTPFile[] files = this.getListFiles();
        for (FTPFile file : files) {
            if (name.equals(file.getName())) {
                return file;
            }
        }

        return null;
    }

    @Override
    public boolean deleteFile(String fileName) throws IOException {
        return this.ftp.deleteFile(fileName);
    }

    @Override
    public boolean removeDir(String dir) throws IOException {
        return this.ftp.removeDirectory(dir);
    }

    @Override
    public Map<String, Boolean> deleteFiles(String[] fileNames) throws IOException {
        HashMap<String, Boolean> result = new HashMap<>();
        for (String name : fileNames) {
            if (this.ftp.deleteFile(name)) {
                result.put(name, true);
            } else {
                result.put(name, false);
            }
        }

        return result;
    }

    @Override
    public boolean renameFile(String oldFileName, String newFileName) throws IOException {
        return this.ftp.rename(oldFileName, newFileName);
    }

    @Override
    public boolean regex(String str) throws IOException {
        String regEx = "#.*#";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    @Override
    public InputStream getInputStream(String fileName) throws IOException {
        return this.ftp.retrieveFileStream(fileName);
    }

    @Override
    public OutputStream getOutputStream(String fileName) throws IOException {
        return this.ftp.storeFileStream(fileName);
    }

    @Override
    public void setActivePortRange(int minPort, int maxPort) throws IOException {
        this.ftp.setActivePortRange(minPort, maxPort);
    }

    @Override
    public void enterLocalActiveMode() {
        this.ftp.enterLocalActiveMode();
    }

    @Override
    public boolean login(String host, int port, String username, String password, String mode) throws IOException {
        FTPClient ftpClient = new FTPClient();
        FTPClientConfig conf = new FTPClientConfig("UNIX");
        conf.setServerLanguageCode("ZH");
        ftpClient.setConnectTimeout(600000);
        ftpClient.setDataTimeout(600000);
        ftpClient.configure(conf);
        ftpClient.connect(host, port);
        ftpClient.setControlEncoding("GBK");
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            return false;
        } else if (!ftpClient.login(username, password)) {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }

            return false;
        } else {
            ftpClient.setFileType(2);
            this.ftp = ftpClient;
            if ("1".equals(mode)) {
                this.ftp.enterLocalPassiveMode();
            } else {
                this.ftp.enterLocalActiveMode();
            }

            return true;
        }
    }

    @Override
    public Vector listFiles() throws IOException {
        return null;
    }

    @Override
    public String findFileOnSftp(String fileName) throws IOException {
        return null;
    }

    @Override
    public boolean makeDir(String path) throws IOException {
        return this.ftp.makeDirectory(path);
    }

    @Override
    public boolean completePendingCommand() throws IOException {
        return this.ftp.completePendingCommand();
    }
}
