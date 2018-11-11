/**
 * Project: x-framework
 * Package Name: org.ike.core.io
 * Author: Xuejia
 * Date Time: 2016/12/11 14:15
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.io;

/**
 * Class Name: FileInfo
 * Create Date: 2016/12/11 14:15
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 文件实体
 */
public class FileInfo {
    private String protocol;                            // 文件所用的协议
    private String host;                                // 文件主机
    private int port;                                   // 端口
    private String path;                                // 路径
    private String name;                                // 文件名称
    private long length;                                // 文件长度
    private String type;                                // 文集那类型
    private short block_replication;                    // 块数量
    private long blocksize;                             // 块大小
    private String modification_time;                   // 修改时间
    private String access_time;                         // 最后访问时间
    private String permission;                          // 权限
    private String owner;                               // 文件拥有者
    private String group;                               // 文件拥有组

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getBlock_replication() {
        return block_replication;
    }

    public void setBlock_replication(short block_replication) {
        this.block_replication = block_replication;
    }

    public long getBlocksize() {
        return blocksize;
    }

    public void setBlocksize(long blocksize) {
        this.blocksize = blocksize;
    }

    public String getModification_time() {
        return modification_time;
    }

    public void setModification_time(String modification_time) {
        this.modification_time = modification_time;
    }

    public String getAccess_time() {
        return access_time;
    }

    public void setAccess_time(String access_time) {
        this.access_time = access_time;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", type='" + type + '\'' +
                ", block_replication=" + block_replication +
                ", blocksize=" + blocksize +
                ", modification_time='" + modification_time + '\'' +
                ", access_time='" + access_time + '\'' +
                ", permission='" + permission + '\'' +
                ", owner='" + owner + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
