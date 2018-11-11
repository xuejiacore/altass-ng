/*
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2017/3/6 16:59
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.io.fsoper;

import org.apache.commons.io.FileUtils;
import org.chim.altass.base.io.FileInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: LocalFileOperator
 * Create Date: 2017/3/6 16:59
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 本地文件操作
 */
public class LocalFileOperator extends AbstractFileOperator {

    public LocalFileOperator(String protocol, String user, String host, int port, String pureUrl) {
        super(protocol, user, host, port, pureUrl);
    }

    public LocalFileOperator() {
    }

    @Override
    public boolean createFile(String path) {
        return createFile(path, false);
    }

    @Override
    public boolean createFile(String path, boolean force) {
        File file = new File(path);
        if (file.exists()) {
            if (force) {
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    return false;
                }
            } else {
                return false;
            }
        }

        try {
            FileUtils.write(file, "");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean createDir(String dir) {
        return createDir(dir, false);
    }

    @Override
    public boolean createDir(String dir, boolean force) {
        File file = new File(dir);
        if (file.exists()) {
            if (force) {
                try {
                    FileUtils.forceMkdir(file);
                } catch (IOException e) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
        try {
            FileUtils.forceMkdir(file);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean copyFile(String src, String target) throws IOException {
        return copyFile(src, target, false);
    }

    @Override
    public boolean copyFile(String src, String target, boolean force) throws IOException {
        File srcFile = new File(src);
        if (srcFile.exists()) {
            if (srcFile.isDirectory()) {
                return false;
            } else {
                File targetFile = new File(target);
                if (targetFile.exists()) {
                    if (force) {
                        if (deleteFile(target)) {
                            return cpFile(srcFile, targetFile);
                        }
                    } else {
                        throw new IOException("File is existed.");
                    }
                } else {
                    return cpFile(srcFile, targetFile);
                }
            }
        } else {
            throw new IOException("Source file not found.");
        }
        return false;
    }

    private boolean cpFile(File srcFile, File targetFile) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return true;
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
    public boolean writeTo(InputStream input, OutputStream output, boolean overwrite) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(input));
            writer = new BufferedWriter(new OutputStreamWriter(output));
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return false;
    }

    @Override
    public boolean writeTo(InputStream input, String target, boolean overwrite) {
        return false;
    }

    @Override
    public boolean deleteFile(String path) {
        return deleteFile(path, false);
    }

    @Override
    public boolean deleteFile(String path, boolean force) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                // 不允许删除目录
                return false;
            } else {
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeDirectory(String path) {
        return removeDirectory(path, false);
    }

    @Override
    public boolean removeDirectory(String path, boolean force) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                // 文件不可删除
                return false;
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length != 0) {
                    // 目录不为空，强制删除则删除
                    if (force) {
                        try {
                            FileUtils.forceDelete(file);
                            return true;
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    // 目录为空，直接删除
                    try {
                        FileUtils.forceDelete(file);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean renameFile(String path, String targetName) {
        File file = new File(path);
        return file.exists() && file.renameTo(new File(file.getParentFile().getAbsolutePath() + File.separatorChar + targetName));
    }

    @Override
    public List<FileInfo> listFiles(String path) {
        File file = new File(path);
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                FileInfo fileInfo;
                for (File f : files) {
                    fileInfo = new FileInfo();
                    fileInfo.setName(f.getName());
                    fileInfo.setModification_time(String.valueOf(f.lastModified()));
                    if (f.isFile())
                        fileInfo.setLength(FileUtils.sizeOf(f));
                    fileInfo.setPath(f.getPath());
                    fileInfoList.add(fileInfo);
                }
            }
        }
        return fileInfoList;
    }

    @Override
    public boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    @Override
    public boolean isDirectory(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    @Override
    public InputStream getInputStream(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OutputStream getOutputStream(String path, boolean overwrite) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return new FileOutputStream(file);
            } else {
                throw new IOException("Is a directory.");
            }
        } else {
            File parentFile = file.getParentFile();
            if (parentFile != null && parentFile.exists() && parentFile.isDirectory()) {
                return new FileOutputStream(file);
            } else {
                if (overwrite) {
                    if (parentFile != null && parentFile.mkdirs() && file.createNewFile()) {
                        return new FileOutputStream(file);
                    }
                }
                throw new IOException("File not exist.");
            }
        }
    }

    @Override
    public boolean append(String path, String content) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
            writer.write(content);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return false;
    }

    @Override
    public boolean append(String path, byte[] buff) {
        return false;
    }
}
