/**
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2016/12/11 14:48
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.io;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.chim.altass.base.utils.AssertUtil;

import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: DefaultFSOperator
 * Create Date: 2016/12/11 14:48
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 默认的文件系统操作器
 * <p>
 * 该文件系统操作器允许用户对本地文件系统以及HDFS进行常见的文件操作
 * 如：目录的创建、删除、移动、文件的创建、删除、移动、重命名等
 */
@SuppressWarnings("JavaDoc")
public class DefaultFSOperator implements IHadoopDistributedFileSystem {

    private Configuration hadoopConfig;                                             // 分布式文件系统的配置
    private FileSystem fileSys;                                                     // 分布式文件系统
    private static final String COPY_TO_LOCAL_PREFIX = "_copyToLocal_";             // 将文件复制到本地的前缀
    private static String CP_SUFFIX = ".etltmp";                                    // 文件复制的后缀（如果需要）

    /**
     * 使用默认的配置获得文件系统
     *
     * @throws IOException
     */
    public DefaultFSOperator() throws IOException {
        hadoopConfig = new Configuration();
        fileSys = FileSystem.get(hadoopConfig);
    }

    /**
     * 使用用户自定以的配置获得文件系统实例
     *
     * @param hadoopConfig Hadoop配置对象实例
     * @throws IOException
     */
    public DefaultFSOperator(Configuration hadoopConfig) throws IOException {
        this.hadoopConfig = hadoopConfig;
        fileSys = FileSystem.get(this.hadoopConfig);
    }

    /**
     * 获得分布式文件系统的配置
     *
     * @return 返回Hadoop Configuration实例
     */
    public Configuration getHadoopConfig() {
        return hadoopConfig;
    }

    /**
     * 设置hadoopConfig
     *
     * @param hadoopConfig hadoop config
     */
    public void setHadoopConfig(Configuration hadoopConfig) {
        this.hadoopConfig = hadoopConfig;
    }

    /**
     * 获得分布式文件系统
     *
     * @return 文件系统实例
     */
    public FileSystem getFileSys() {
        return fileSys;
    }

    /**
     * 设置分布式文件系统
     *
     * @param fileSys 文件系统实例
     */
    public void setFileSys(FileSystem fileSys) {
        this.fileSys = fileSys;
    }

    /**
     * 本地文件的复制
     *
     * @param src 文件源
     * @param dst 需要复制的文件目标
     * @throws IOException
     */
    @Override
    public void copy(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        File srcFile = new File(src);                   // 文件源
        File destFile = new File(dst);                  // 目标文件
        if (srcFile.exists()) {
            if (srcFile.isFile()) {
                FileUtils.copyFile(srcFile, destFile);
                // FileUtils.copyFileToDirectory(srcFile, destFile);
            } else if (srcFile.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(srcFile, destFile);
            } else {
                throw new IOException("Source '" + srcFile + "' can not copy to the Destination '" + destFile + "'");
            }
        } else {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
    }

    /**
     * 将文件从HDFS中复制到本地指定路径中
     *
     * @param src 需要复制的Hdfs文件
     * @param dst 目标路径
     * @throws IOException
     */
    @Override
    public void copyFromHdfsToLocal(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        Path srcPath = new Path(src);
        File destFile = new File(dst);
        FileStatus[] fileStatuses = fileSys.globStatus(srcPath);
        boolean dstIsDir = destFile.isDirectory();

        if (fileStatuses.length > 1 && !dstIsDir) {
            throw new IOException("When copying multiple files, " + "destination should be a directory.");
        }

        for (FileStatus status : fileStatuses) {
            Path path = status.getPath();
            File file = dstIsDir ? new File(destFile, path.getName()) : destFile;
            copyToLocal(fileSys, path, file);
        }

    }

    /**
     * 将文件从HDFS中复制到本地
     *
     * @param fs  文件系统
     * @param src 文件源
     * @param dst 目标文件
     * @throws IOException
     */
    private void copyToLocal(final FileSystem fs, final Path src, final File dst) throws IOException {
        if (!fs.getFileStatus(src).isDir()) {
            if (dst.exists()) {
                // match the error message in FileUtil.checkDest():
                throw new IOException("Target " + dst + " already exists");
            }

            // use absolute name so that tmp file is always created under dest
            // dir
            File tmp = FileUtil.createLocalTempFile(dst.getAbsoluteFile(), COPY_TO_LOCAL_PREFIX, true);
            if (!FileUtil.copy(fs, src, tmp, false, fs.getConf())) {
                throw new IOException("Failed to copy " + src + " to " + dst);
            }

            if (!tmp.renameTo(dst)) {
                throw new IOException("Failed to rename tmp file " + tmp + " to local destination \"" + dst + "\".");
            }
        } else {
            // once FileUtil.copy() supports tmp file, we don't need to
            // mkdirs().
            dst.mkdirs();
            for (FileStatus path : fs.listStatus(src)) {
                copyToLocal(fs, path.getPath(), new File(dst, path.getPath().getName()));
            }
        }
    }

    /**
     * 将文件从本地复制到HDFS中
     *
     * @param src 需要复制的本地文件
     * @param dst 目标路径
     * @throws IOException
     */
    @Override
    public void copyFromLocalToHdfs(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        dstFs.copyFromLocalFile(false, true, srcPath, dstPath);
    }

    /**
     * 在HDFS中复制文件
     *
     * @param src 文件源
     * @param dst 需要复制的文件目标
     * @throws IOException
     */
    @Override
    public void copyOnHdfs(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        FileSystem srcFs = srcPath.getFileSystem(getHadoopConfig());
        Path dstPath = new Path(dst);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        Path[] srcs = FileUtil.stat2Paths(srcFs.globStatus(srcPath), srcPath);
        if (srcs.length > 1 && !dstFs.isDirectory(dstPath)) {
            throw new IOException("When copying multiple files, " + "destination should be a directory.");
        }
        for (Path src1 : srcs) {
            FileUtil.copy(srcFs, src1, dstFs, dstPath, false, getHadoopConfig());
        }
    }

    /**
     * 在HDFS中复制文件，并在复制后的文件中添加固定的后缀
     *
     * @param src 需要复制的文件
     * @param dst 需要复制到的文件目标
     * @throws IOException
     */
    @Override
    public void copyOnHdfsSuf(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        FileSystem srcFs = srcPath.getFileSystem(getHadoopConfig());
        Path tmpPath = new Path(dst + CP_SUFFIX);
        Path dstPath = new Path(dst);
        FileSystem tmpFs = tmpPath.getFileSystem(getHadoopConfig());
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        Path[] paths = FileUtil.stat2Paths(srcFs.globStatus(srcPath), srcPath);
        if (paths.length > 1 && !dstFs.isDirectory(dstPath)) {
            throw new IOException("When copying multiple files, " + "destination should be a directory.");
        }
        for (Path src1 : paths) {
            FileUtil.copy(srcFs, src1, tmpFs, tmpPath, false, getHadoopConfig());
            dstFs.rename(tmpPath, dstPath);
        }
    }

    /**
     * 强制删除本地的文件
     *
     * @param path 需要删除的文件路径
     * @throws IOException
     */
    @Override
    public void delete(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        FileUtils.forceDelete(new File(path));
    }

    /**
     * 强制删除HDFS中的文件
     *
     * @param path 需要删除的文件路径
     * @return 删除成功的时候返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean deleteOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        if (!dstFs.exists(dstPath)) {
            throw new FileNotFoundException("Path '" + path + "' does not exist");
        }
        return dstFs.delete(dstPath, true);
    }

    /**
     * 判断本地文件系统中是否存在指定路径的文件
     *
     * @param path 需要判断的文件或者是目录
     * @return 如果存在返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean exists(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        File file = new File(path);
        return file.exists();
    }

    /**
     * 判断文件是否存在于指定的HDFS中
     *
     * @param path 需要判断的文件路径
     * @return 如果存在，返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean existsOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        return dstFs.exists(dstPath);
    }

    /**
     * 判断路径在本地文件系统中是否是目录
     *
     * @param path 需要判定的文件路径
     * @return 如果是目录，返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean isDirectory(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    /**
     * 判断路径在HDFS中是否是目录
     *
     * @param path 需要判定的文件路径
     * @return 如果是目录，返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean isDirectoryOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        if (!dstFs.exists(dstPath)) {
            throw new FileNotFoundException("Path '" + path + "' does not exist");
        }
        return dstFs.getFileStatus(dstPath).isDir();
    }

    /**
     * 判断路径在本地文件系统中是否是文件
     *
     * @param path 需要判定的文件路径
     * @return 如果是文件，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean isFile(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * 判断路径在HDFS中是否是文件
     *
     * @param path 需要判定的路径
     * @return 如果是文件，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean isFileOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        if (!dstFs.exists(dstPath)) {
            throw new FileNotFoundException("Path '" + path + "' does not exist");
        }
        return dstFs.isFile(dstPath);
    }

    /**
     * 列出本地文件系统中指定目录的文件信息
     *
     * @param path 需要列出文件列表的目录
     * @return 指定目录下本地文件系统中的文件列表信息
     * @throws IOException
     */
    @Override
    public List<FileInfo> listFiles(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        File fileRoot = new File(path);
        File[] files = fileRoot.listFiles();
        if (files != null) {
            List<FileInfo> fileInfoList = new ArrayList<>();
            for (File file : files) {
                FileInfo FileInfo = new FileInfo();
                FileInfo.setPath(file.getAbsolutePath());
                FileInfo.setName(file.getName());
                FileInfo.setLength(file.length());
                FileInfo.setType(file.isDirectory() ? "dir" : "file");
                fileInfoList.add(FileInfo);
            }
            return fileInfoList;
        }
        return null;
    }

    /**
     * 以json形式，文件目录数的形式获取文件目录结构
     * <p>
     * 该方式主要用于构建前端的目录树数据结构
     *
     * @param path 需要列出文件列表的目录
     * @return 返回文件目录树
     * @throws IOException
     */
    @Override
    public List<String> localFileListByPath(String path) throws IOException {
        List<String> list = new ArrayList<String>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                String name = f.getAbsolutePath();
                name = name.replace("\\", "/");
                StringBuilder sb = new StringBuilder();
                if (f.isDirectory()) {
                    sb.append("{\"id\":\"").append(name).append("\",\"text\":\"").append(name).append("\",\"state\":\"closed\",\"attributes\":{\"type\":\"folder\"}}");
                } else {
                    sb.append("{\"id\":\"").append(name).append("\",\"text\":\"").append(name).append("\",\"attributes\":{\"type\":\"leaf\"}}");
                }
                list.add(sb.toString());
            }
        }
        return list;
    }

    /**
     * 列出HDFS中目录对应下的文件列表
     *
     * @param path 需要列出的Hdfs中的文件目录
     * @return HDFS中目录下的文件列表信息
     * @throws IOException
     */
    @Override
    public List<FileInfo> listFilesOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        //AssertUtil.isTrue(dstFs.exists(dstPath), "Path '" + path + "' does not exist");
        if (!dstFs.exists(dstPath)) {
            //此处改掉Assert，因为Assert会抛出IllegalArgumentException，导致方法没有捕获
            return null;
        }

        List<FileInfo> fileInfoList = new ArrayList<>();
        FileStatus files[] = dstFs.listStatus(dstPath);
        for (FileStatus file : files) {
            FileInfo FileInfo = new FileInfo();
            FileInfo.setProtocol(file.getPath().toUri().getScheme());
            FileInfo.setHost(file.getPath().toUri().getHost());
            FileInfo.setPort(file.getPath().toUri().getPort());
            FileInfo.setPath(file.getPath().toUri().getPath());
            FileInfo.setName(file.getPath().getName());
            FileInfo.setLength(file.getLen());
            FileInfo.setType(file.isDir() ? "dir" : "file");
            FileInfo.setBlock_replication(file.getReplication());
            FileInfo.setBlocksize(file.getBlockSize());
            FileInfo.setModification_time((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date((file.getModificationTime()))));
            FileInfo.setAccess_time((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date((file.getAccessTime()))));
            FileInfo.setPermission(file.getPermission().toString());
            FileInfo.setOwner(file.getOwner());
            FileInfo.setGroup(file.getGroup());
            fileInfoList.add(FileInfo);
        }
        return fileInfoList;
    }

    /**
     * 使用指定的路径在本地文件系统中创建一个目录
     *
     * @param path 需要创建的目录
     * @throws IOException
     */
    @Override
    public void mkdir(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        File file = new File(path);
        FileUtils.forceMkdir(file);
    }

    /**
     * 使用指定的路径在HDFS中创建一个目录
     *
     * @param path 需要创建的文件路径
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean mkdirOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        return dstFs.mkdirs(dstPath);
    }

    /**
     * 在HDFS中创建目录，如果目录不存在
     *
     * @param path 需要创建的路径
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean ignoreMkdirOnHdfs(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        return dstFs.exists(dstPath) || dstFs.mkdirs(dstPath);
    }

    /**
     * 移动本地文件
     *
     * @param src 需要移动的文件
     * @param dst 移动的目标路径
     * @throws IOException
     */
    @Override
    public void move(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        File srcFile = new File(src);
        File destFile = new File(dst);
        // FileUtils.moveToDirectory(srcFile, destFile, true);
        FileUtils.moveFile(srcFile, destFile);
    }

    /**
     * 移动HDFS文件到本地
     *
     * @param src 需要移动的Hdfs文件系统中的文件路径
     * @param dst 移动的目标路径
     * @throws IOException
     */
    @Override
    public void moveFromHdfsToLocal(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        dstFs.moveToLocalFile(srcPath, dstPath);
    }

    /**
     * 移动本地文件到HDFS中
     *
     * @param src 需要移动的本地文件系统中的文件路径
     * @param dst 移动的目标路径
     * @throws IOException
     */
    @Override
    public void moveFromLocalToHdfs(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        dstFs.moveFromLocalFile(srcPath, dstPath);
    }

    /**
     * 在HDFS中移动文件
     *
     * @param src 需要移动的源文件
     * @param dst 移动的目标路径
     * @throws IOException
     */
    @Override
    public void moveOnHdfs(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
        dstFs.rename(srcPath, dstPath);
    }

    /**
     * 在HDFS中移动文件，并且在移动文件后重命名文件，追加后缀名
     *
     * @param src 需要移动的源文件
     * @param dst 移动的目标路径
     * @throws IOException
     */
    @Override
    public void moveOnHdfsSuf(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        Path tmpPath = new Path(dst + CP_SUFFIX);
        FileSystem dstFs = tmpPath.getFileSystem(getHadoopConfig());
        if (dstFs.rename(srcPath, tmpPath)) {
            Path dstPath = new Path(dst);
            dstFs = dstPath.getFileSystem(getHadoopConfig());
            dstFs.rename(tmpPath, dstPath);
        }
    }

    /**
     * 重命名本地文件
     *
     * @param src 需要重命名的本地文件路径
     * @param dst 重命名后的文件路径
     * @return 如果重命名成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean rename(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        File srcFile = new File(src);
        File destFile = new File(dst);
        AssertUtil.isTrue(srcFile.exists(), "Source '" + srcFile + "' can not copy to the Destination '" + destFile + "'");
        return srcFile.renameTo(destFile);
    }

    /**
     * 在HDFS中重命名文件
     *
     * @param src 需要重命名的Hdfs文件路径
     * @param dst 重命名后的文件路径
     * @return 如果重命名成功，那么返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean renameOnHdfs(String src, String dst) throws IOException {
        AssertUtil.notNull(src, "Source must not be null");
        AssertUtil.notNull(dst, "Destination must not be null");
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        FileSystem srcFs = srcPath.getFileSystem(getHadoopConfig());
        return srcFs.rename(srcPath, dstPath);
    }

    /**
     * 名称是否正则匹配
     *
     * @param name    需要判定的名称
     * @param formula 正则匹配式
     * @return 如果匹配，那么返回值为true，否则返回值为flash
     * @throws IOException
     */
    @Override
    public boolean matching(String name, String formula) throws IOException {
        return (name != null) && (!"".equals(name.trim())) && (formula != null) && (!"".equals(formula.trim())) && name.matches(formula);
    }

    /**
     * 获得本地文件系统中目录下的文件名称
     *
     * @param localDir 本地文件目录
     * @return 本地文件目录中的文件名称列表
     * @throws IOException
     */
    @Override
    public List<String> getDirFileNames(String localDir) throws IOException {
        File dirFile = new File(localDir);
        File[] files = dirFile.listFiles();
        if (files == null || files.length <= 0) {
            return null;
        }

        List<String> fileList = new ArrayList<String>();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    /**
     * 获得HDFS文件目录下对应的文件名称列表
     *
     * @param path 目录
     * @return HDFS文件中指定目录的文件名称列表
     * @throws IOException
     */
    @Override
    public List<String> getDirFileNamesOnHdfs(String path) throws IOException {
        List<FileInfo> fileInfoNamesOnHdfs = listFilesOnHdfs(path);
        if (fileInfoNamesOnHdfs != null && fileInfoNamesOnHdfs.size() > 0) {
            List<String> files = new ArrayList<>();
            for (FileInfo list : fileInfoNamesOnHdfs) {
                if (list.getType().equals("file")) {
                    files.add(list.getName());
                }
            }
            return files;
        } else {
            return null;
        }
    }

    /**
     * 判断是否满足正则匹配式：#.*#
     *
     * @param str 需要正则匹配的字符串
     * @return 如果满足，返回值为true，否则返回值为false
     * @throws IOException
     */
    @Override
    public boolean regex(String str) throws IOException {
        //String regEx = "[!$%^*()+=|{}':',\\[\\].?\\\\]";
        String regEx = "#.*#";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 获得hdfs上的输入流
     *
     * @param path 路径
     * @return hdfs 文件的输入流
     */
    public InputStream getInputStream(String path) {
        FSDataInputStream dfsDataInputStream = null;
        try {
            dfsDataInputStream = fileSys.open(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dfsDataInputStream;
    }

    /**
     * 读hdfs上文件内容，rows是行数
     *
     * @param fileDir            文件目录
     * @param rows               读取行数
     * @param ignoreFile，是否忽略文件名
     * @return 返回从文件系统中读取的内容
     */
    @Override
    public String readFromHdfs(String fileDir, String rows, boolean ignoreFile) {
        StringBuilder sb = new StringBuilder();
        FSDataInputStream hdfsInStream = null;
        BufferedReader br = null;
        String filePath = fileDir;
        try {
            List<FileInfo> listFileInfos = this.listFilesOnHdfs(fileDir);
            if (listFileInfos != null && listFileInfos.size() > 0) {
                if (ignoreFile) {
                    for (FileInfo ef : listFileInfos) {
                        if (ef.getLength() > 0) {
                            filePath = fileDir + "/" + ef.getName();
                            break;
                        }
                    }
                } else {
                    for (FileInfo ef : listFileInfos) {
                        if (ef.getName().indexOf("00000") > 0) {
                            filePath = fileDir + "/" + ef.getName();
                            break;
                        }
                    }
                }
            }
            hdfsInStream = fileSys.open(new Path(filePath));
            br = new BufferedReader(new InputStreamReader(hdfsInStream));

            String line;
            int index = 0;
            while (null != (line = br.readLine())) {
                if (index >= Integer.parseInt(rows)) {
                    break;
                }
                sb.append(line).append("\n");
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != hdfsInStream) {
                try {
                    hdfsInStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将文件写入到HDFS中
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IOException
     */
    @Override
    public void writeToHdfs(InputStream in, OutputStream out) throws IOException {
        IOUtils.copyBytes(in, out, getHadoopConfig());
    }

    /**
     * 将文件写入到HDFS文件中
     *
     * @param in        输入流
     * @param dst       目标路径
     * @param overwrite 是否覆盖
     * @throws IOException
     */
    @Override
    public void writeToHdfs(InputStream in, String dst, boolean overwrite) throws IOException {
        OutputStream out = null;
        try {
            Path dstPath = new Path(dst);
            FileSystem dstFS = dstPath.getFileSystem(getHadoopConfig());
            out = dstFS.create(dstPath, overwrite);
            writeToHdfs(in, out);
        } catch (IOException e) {
            IOUtils.closeStream(out);
            IOUtils.closeStream(in);
            throw e;
        }
    }

    /**
     * 获得输出流对象实例
     *
     * @param path      需要获得输出流的路径
     * @param overwrite 是否允许覆盖
     */
    public OutputStream getOutputStream(String path, boolean overwrite) {
        Path dstPath = new Path(path);
        FileSystem dstFS;
        try {
            dstFS = dstPath.getFileSystem(getHadoopConfig());
            return dstFS.create(dstPath, overwrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文件写入到HDFS文件中
     *
     * @param in  输入流
     * @param dst 目标路径
     * @throws IOException
     */
    @Override
    public void writeToHdfs(InputStream in, String dst) throws IOException {
        writeToHdfs(in, dst, true);
    }

    /**
     * 将字符串内容数据写入到HDFS目标路径中
     *
     * @param content 需要写入的内容
     * @param dst     需要写入的目标
     * @throws IOException
     */
    @Override
    public void writeToHdfs(String content, String dst) throws IOException {
        AssertUtil.notNull(content, "content must not be null");
        InputStream in = new ByteArrayInputStream(content.getBytes());
        writeToHdfs(in, dst);
    }

    /**
     * 将文件以追加的形式添加到HDFS文件中
     *
     * @param content 需要追加的文件内容
     * @param dst     需要追加的目标文件
     * @throws IOException
     */
    @Override
    public void appendStringToHdfs(String content, String dst) throws IOException {
        AssertUtil.notNull(content, "content must not be null");
        Configuration conf = new Configuration();
        conf.setBoolean("dfs.support.append", true);
        conf.set("fs.defaultFS", this.hadoopConfig.get("fs.defaultFS"));
        conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        conf.setBoolean("fileSys.hdfs.impl.disable.cache", true);
        InputStream in = new ByteArrayInputStream(content.getBytes());
        FileSystem hdfsWrite = FileSystem.get(URI.create(dst), conf);
        OutputStream out = null;
        try {
            if (hdfsWrite.exists(new Path(dst))) {
                hdfsWrite.close();
                hdfsWrite = FileSystem.get(conf);
                out = hdfsWrite.append(new Path(dst));
            } else {
                out = hdfsWrite.create(new Path(dst));
            }
            IOUtils.copyBytes(in, out, 4096, true);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            hdfsWrite.close();
        }
    }

    /**
     * 从HDFS文件中读取字符串
     *
     * @param dst 需要读取的目标文件
     * @return 从指定文件路径中读取的文件内容
     * @throws IOException
     */
    @Override
    public String readStringFromHdfs(String dst) throws IOException {
        AssertUtil.notNull(dst, "Destination must not be null");
        InputStream in = null;
        BufferedReader bufferedReader = null;
        try {
            Path dstPath = new Path(dst);
            FileSystem dstFs = dstPath.getFileSystem(getHadoopConfig());
            if (!dstFs.exists(dstPath) || dstFs.isDirectory(dstPath)) {
                throw new IOException("目标文件：" + dst + "不存在");
            }
            in = dstFs.open(dstPath);
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 列出HDFS文件中的文件名列表
     *
     * @param path 文件路径
     * @return 文件列表
     * @throws IOException
     */
    @Override
    public List<String> listAllFiles(String path) throws IOException {
        AssertUtil.notNull(path, "path must not be null");
        Path dstPath = new Path(path);
        FileSystem dstFs = dstPath.getFileSystem(hadoopConfig);
        List<String> list = new ArrayList<String>();
        if (dstFs.exists(dstPath)) {
            FileStatus files[] = dstFs.listStatus(dstPath);
            for (FileStatus file : files) {
                if (file.isDirectory()) {
                    list.addAll(listAllFiles(file.getPath().toString()));
                } else {
                    list.add(file.getPath().toString());
                }
            }
        }
        return list;
    }

    /**
     * 列出文件的内容
     *
     * @param path  需要列出的文件目录
     * @param limit 列出数量上限
     * @return
     * @throws IOException
     */
    @Override
    public List<String> catAllFiles(String path, int limit) throws IOException {
        List<String> content = new ArrayList<String>();
        int index = 0;
        List<String> files = listAllFiles(path);
        for (String file : files) {
            Path dstPath = new Path(file);
            FileSystem dstFs;
            InputStream datais = null;
            BufferedReader databr = null;
            try {
                dstFs = dstPath.getFileSystem(hadoopConfig);
                datais = dstFs.open(dstPath);
                databr = new BufferedReader(new InputStreamReader(datais));
                String line = null;
                while ((line = databr.readLine()) != null) {
                    content.add(line);
                    if (limit != -1 && ++index == limit) {
                        return content;
                    }
                }
            } finally {
                if (databr != null) {
                    try {
                        databr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (datais != null) {
                    try {
                        datais.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return content;
    }

    /**
     * 解压文件
     *
     * @param source 需要解压的文件路径
     * @param dest   需要解压到的目标文件
     * @return 解压HDFS中的文件到指定的路径
     * @throws IOException
     */
    @Override
    public boolean unZipOnHdfs(String source, String dest) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(source), getHadoopConfig());
        Path inputPath = new Path(source);
        CompressionCodecFactory factory = new CompressionCodecFactory(hadoopConfig);
        CompressionCodec codec = factory.getCodec(inputPath);
        if (codec == null) {
            return false;
        }
        String name = source.substring(source.lastIndexOf("/") + 1);
        String outputUri = CompressionCodecFactory.removeSuffix(dest + "/" + name, codec.getDefaultExtension());

        InputStream in = null;
        OutputStream out = null;

        try {
            in = codec.createInputStream(fs.open(inputPath));
            out = fs.create(new Path(outputUri));
            IOUtils.copyBytes(in, out, hadoopConfig);
            return true;
        } finally {
            IOUtils.closeStream(in);
            IOUtils.closeStream(out);
        }
    }
}
