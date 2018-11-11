/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.cinfig
 * Author: Xuejia
 * Date Time: 2016/12/15 13:27
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.configuration;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: EurekaSiteConfiguration
 * Create Date: 2016/12/15 13:27
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * ETL资源配置实体
 */
@Elem(alias = "eurekacore-site")
public class EurekaSiteConfiguration {
    /**
     * 配置的版本号
     */
    @Attr(alias = "version")
    private String version = null;

    /**
     * 发布的版本模式
     */
    @Attr(alias = "releaseMode")
    private String releaseMode = null;

    /**
     * 节点资源
     */
    @Elem(alias = "node-resources")
    private NodeResources resources = null;

    /**
     * 日志配置
     */
    @Elem(alias = "log-config")
    private LogConfig logConfig = null;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseMode() {
        return releaseMode;
    }

    public void setReleaseMode(String releaseMode) {
        this.releaseMode = releaseMode;
    }

    public NodeResources getResources() {
        return resources;
    }

    public void setResources(NodeResources resources) {
        this.resources = resources;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }
}
