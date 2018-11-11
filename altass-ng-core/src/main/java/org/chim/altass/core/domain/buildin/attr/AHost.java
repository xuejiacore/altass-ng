/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.domain.buildin.attr
 * Author: Xuejia
 * Date Time: 2017/3/6 9:42
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: AHost
 * Create Date: 2017/3/6 9:42
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "host", version = "1.0")
public class AHost {

    @Attr(alias = "hostName")
    private String hostName = null;                                                 // 主机名称
    @Attr(alias = "host")
    private String host = null;                                                     // 主机IP地址
    @Attr(alias = "port")
    private Integer port = null;                                                    // 主机端口号
    @Attr(alias = "user")
    private String user = null;                                                     // 访问用户
    @Attr(alias = "password")
    private String password = null;                                                 // 访问密码
    @Attr(alias = "basePath")
    private String basePath = null;                                                 // 访问根目录
    @Attr(alias = "osType")
    private String osType = null;                                                   // 操作系统类型
    @Attr(alias = "language")
    private String language = null;                                                 // 操作系统语言

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "AHost{" +
                "hostName='" + hostName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", basePath='" + basePath + '\'' +
                ", osType='" + osType + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
