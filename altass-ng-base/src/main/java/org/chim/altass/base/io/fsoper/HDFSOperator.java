/*
 * Project: x-framework
 * Package Name: org.ike.core.io.operator
 * Author: Xuejia
 * Date Time: 2017/3/7 10:50
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.io.fsoper;

import org.apache.hadoop.conf.Configuration;
import org.chim.altass.base.io.DefaultFSOperator;
import org.chim.altass.base.io.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Class Name: HDFSOperator
 * Create Date: 2017/3/7 10:50
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class HDFSOperator extends AbstractFileOperator {

    private DefaultFSOperator fs = null;

    public HDFSOperator(String protocol, String user, String host, int port, String pureUrl) {
        super(protocol, user, host, port, pureUrl);
        try {
            Configuration config = new Configuration();
            config.set("fs.defaultFS", protocol + "://" + host + ":" + port);
            fs = new DefaultFSOperator(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return createDir(dir, false);
    }

    @Override
    public boolean createDir(String dir, boolean force) {
        try {
            if (!fs.existsOnHdfs(dir)) {
                return fs.mkdirOnHdfs(dir);
            } else {
                return force && fs.mkdirOnHdfs(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean copyFile(String src, String target) {
        try {
            fs.copyOnHdfs(src, target);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            fs.writeToHdfs(input, output);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean writeTo(InputStream input, OutputStream output, boolean overwrite) {
        return writeTo(input, output);
    }

    @Override
    public boolean writeTo(InputStream input, String target, boolean overwrite) {
        try {
            fs.writeToHdfs(input, target, overwrite);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteFile(String path) {
        return deleteFile(path, true);
    }

    @Override
    public boolean deleteFile(String path, boolean force) {
        try {
            if (fs.isDirectoryOnHdfs(path)) {
                throw new IOException("Is Directory:" + path);
            } else {
                return fs.deleteOnHdfs(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeDirectory(String path) {
        return removeDirectory(path, true);
    }

    @Override
    public boolean removeDirectory(String path, boolean force) {
        try {
            if (fs.isDirectoryOnHdfs(path)) {
                List<FileInfo> fileInfos = fs.listFiles(path);
                if (fileInfos != null && fileInfos.size() > 0) {
                    if (force) {
                        return fs.deleteOnHdfs(path);
                    } else {
                        throw new IOException("The Dir is Not Empty.");
                    }
                } else {
                    return fs.deleteOnHdfs(path);
                }
            } else {
                throw new IOException("Is File:" + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean renameFile(String path, String targetName) {
        try {
            return fs.existsOnHdfs(path) && fs.renameOnHdfs(path, targetName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<FileInfo> listFiles(String path) {
        try {
            return fs.listFilesOnHdfs(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isFileExist(String path) {
        try {
            return fs.existsOnHdfs(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isDirectory(String path) {
        try {
            return fs.isDirectoryOnHdfs(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public InputStream getInputStream(String path) {
        return fs.getInputStream(path);
    }

    @Override
    public OutputStream getOutputStream(String path, boolean overwrite) {
        return fs.getOutputStream(path, overwrite);
    }

    @Override
    public boolean append(String path, String content) {
        try {
            path = path.startsWith("hdfs") ? path : "hdfs://" + host + ":" + port + path;
            fs.appendStringToHdfs(content, path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean append(String path, byte[] buff) {
        return false;
    }
}
