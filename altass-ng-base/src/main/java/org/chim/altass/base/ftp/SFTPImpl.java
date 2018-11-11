/*
 * Project: x-framework
 * Package Name: org.ike.core.ftp
 * Author: Xuejia
 * Date Time: 2017/3/7 14:42
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.ftp;

import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: SFTPImpl
 * Create Date: 2017/3/7 14:42
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * SFTP实现
 */
public class SFTPImpl implements IFtp {

    private Session session = null;
    private ChannelSftp channel = null;
    private int bufferedOutputStreamSize = 1024;

    public SFTPImpl() {
    }

    @Override
    public void enterLocalPassiveMode() {

    }

    @Override
    public void close() throws IOException {
        if (this.channel != null) {
            this.channel.disconnect();
        }

        if (this.session != null) {
            this.session.disconnect();
        }
    }

    @Override
    public boolean isOpenFTPConnection() throws IOException {
        return this.channel != null && !this.channel.isClosed();
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) throws IOException {
        BufferedOutputStream outStream;
        outStream = new BufferedOutputStream(new FileOutputStream(localFilePath));

        try {
            this.channel.get(remoteFilePath, outStream);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            outStream.flush();
            outStream.close();
        }

        return true;
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
            hadoopOut = fs.create(new Path(localFilePath));
            ftpIn = this.channel.get(remoteFileName);
            IOUtils.copyBytes(ftpIn, hadoopOut, this.bufferedOutputStreamSize);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            if (ftpIn != null) {
                ftpIn.close();
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

        try {
            outStr = new FileOutputStream(localFile);
            outStream = new BufferedOutputStream(outStr);
            this.channel.get(remoteFileName, outStream);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
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
        return true;
    }

    @Override
    public boolean uploadFile(String localFilePath, String remoteFileName) throws IOException {
        BufferedInputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(localFilePath));
            this.channel.put(inStream, remoteFileName);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return true;
    }

    @Override
    public boolean uploadFileFormHdfs(String localFilePath, String remoteFileName) throws IOException {
        FSDataInputStream hadoopIn = null;

        try {
            Configuration e = new Configuration();
            Path dstPath = new Path(localFilePath);
            FileSystem dstFs = dstPath.getFileSystem(e);
            hadoopIn = dstFs.open(dstPath);
            this.channel.put(hadoopIn, remoteFileName);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            if (hadoopIn != null) {
                hadoopIn.close();
            }
        }

        return true;
    }

    @Override
    public boolean uploadFile(File localFile, String remoteFileName) throws IOException {
        BufferedInputStream inStream = null;

        try {
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            this.channel.put(inStream, remoteFileName);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
        return true;
    }

    @Override
    public void changeDir(String remoteDir) throws IOException {
        try {
            this.channel.cd(remoteDir);
        } catch (SftpException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public boolean changeWorkingDir(String remoteDir) throws IOException {
        try {
            this.channel.cd(remoteDir);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }

    @Override
    public void toParentDir() throws IOException {

    }

    @Override
    public String[] getListNames() throws IOException {
        return null;
    }

    @Override
    public FTPFile[] getListFiles() throws IOException {
        return null;
    }

    @Override
    public List<String> getListFileNames() throws IOException {
        Vector files;
        try {
            files = this.channel.ls("*");
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        if (files != null && files.size() > 0) {
            ArrayList<String> fileList = new ArrayList<>();

            for (int index = 0; index < files.size(); ++index) {
                Object obj = files.elementAt(index);
                if (obj instanceof ChannelSftp.LsEntry) {
                    String name = ((ChannelSftp.LsEntry) obj).getFilename();
                    fileList.add(name);
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
            return null;
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
        try {
            this.channel.rm(fileName);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public boolean removeDir(String dir) throws IOException {
        try {
            this.channel.rmdir(dir);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public Map<String, Boolean> deleteFiles(String[] fileNames) throws IOException {
        HashMap<String, Boolean> result = new HashMap<>();
        for (String name : fileNames) {
            if (this.deleteFile(name)) {
                result.put(name, true);
            } else {
                result.put(name, false);
            }
        }
        return result;
    }

    @Override
    public boolean renameFile(String oldFileName, String newFileName) throws IOException {
        try {
            this.channel.rename(oldFileName, newFileName);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
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
        try {
            return this.channel.get(fileName);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public OutputStream getOutputStream(String fileName) throws IOException {
        try {
            return this.channel.put(fileName);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void setActivePortRange(int minPort, int maxPort) throws IOException {

    }

    @Override
    public void enterLocalActiveMode() {
        try {
            System.out.println("this.channel.getHome()=" + this.channel.getHome() + ",this.channel.pwd()=" + this.channel.pwd());
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean login(String host, int port, String username, String password, String mode) throws IOException {
        return this.login(host, username, password, port);
    }

    @Override
    public Vector listFiles() throws IOException {
        Vector files;

        try {
            files = this.channel.ls("*");
        } catch (SftpException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

        return files != null && files.size() > 0 ? files : null;
    }

    @Override
    public String findFileOnSftp(String fileName) throws IOException {
        int f = fileName.lastIndexOf("/");
        if (f > 0) {
            fileName = fileName.substring(f + 1);
        } else if (f == 0) {
            return null;
        }

        List<String> files = this.getListFileNames();
        if (files == null) {
            return null;
        } else {

            for (String file : files) {
                if (fileName.equals(file)) {
                    return file;
                }
            }

            return null;
        }
    }

    @Override
    public boolean makeDir(String path) throws IOException {
        try {
            this.channel.mkdir(path);
        } catch (SftpException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean completePendingCommand() throws IOException {
        return true;
    }

    public boolean login(String host, String user, String password, int port) throws IOException {
        try {
            JSch e = new JSch();
            this.session = e.getSession(user, host, port);
            this.session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            this.session.setConfig(config);
            this.session.connect();
            Channel chl = this.session.openChannel("sftp");
            chl.connect();
            this.channel = (ChannelSftp) chl;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }
}
