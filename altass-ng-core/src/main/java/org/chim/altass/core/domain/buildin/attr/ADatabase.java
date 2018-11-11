/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain.buildin.attr
 * Author: Xuejia
 * Date Time: 2016/12/28 17:38
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.buildin.attr;


import org.chim.altass.base.parser.xml.annotation.Attribute;
import org.chim.altass.base.parser.xml.annotation.AttributeType;
import org.chim.altass.base.parser.xml.annotation.Element;

/**
 * Class Name: ADatabase
 * Create Date: 2016/12/28 17:38
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 数据库属性
 */
@Element(name = "database", version = "1.0")
public class ADatabase {
    @Attribute(name = "dbType")
    private String databaseType = null;                         // 数据库类型
    @Attribute(name = "connType")
    private String connectType = null;                          // 连接数据库方式
    @Attribute(name = "jdbc.driver")
    private String jdbcDriverClz = null;                        // jdbc连接方式下的驱动类
    @Attribute(name = "jdbc.url")
    private String jdbcUrl = null;                              // jdbc连接方式下的URL
    @Attribute(name = "username")
    private String jdbcUserName = null;                         // jdbc连接方式下的用户名
    @Attribute(name = "password")
    private String jdbcPassword = null;                         // jdbc连接方式下的密码
    @Attribute(name = "connTimeout", type = AttributeType.INT)
    private Integer connTimeout = null;                         // jdbc连接超时时间

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getJdbcDriverClz() {
        return jdbcDriverClz;
    }

    public void setJdbcDriverClz(String jdbcDriverClz) {
        this.jdbcDriverClz = jdbcDriverClz;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUserName() {
        return jdbcUserName;
    }

    public void setJdbcUserName(String jdbcUserName) {
        this.jdbcUserName = jdbcUserName;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public Integer getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(Integer connTimeout) {
        this.connTimeout = connTimeout;
    }
}
