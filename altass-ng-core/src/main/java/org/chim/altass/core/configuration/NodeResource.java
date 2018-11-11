/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.config
 * Author: Xuejia
 * Date Time: 2016/12/15 13:31
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.configuration;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.base.utils.security.MD5Utils;
import org.chim.altass.core.executor.AbstractExecutor;

/**
 * Class Name: NodeResource
 * Create Date: 2016/12/15 13:31
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点资源信息
 */
@Elem(alias = "node-res", version = "0.0.1")
public class NodeResource {

    /**
     * 节点的资源ID，实质是资源的类的MD5方式
     */
    @Attr(alias = "resId")
    private String resId = null;

    /**
     * 节点对应的类
     */
    @Attr(alias = "clazz")
    private String clazz = null;

    /**
     * 节点资源分组
     */
    @Attr(alias = "groupName")
    private String groupName = null;

    /**
     * 节点的中文名称
     */
    @Attr(alias = "nodeName")
    private String nodeName = null;

    /**
     * 节点的配置页面URL
     */
    @Attr(alias = "configUrl")
    private String configUrl = null;

    /**
     * 节点资源大图
     */
    @Attr(alias = "bigImage")
    private String bigImage = null;

    /**
     * 节点资源中图
     */
    @Attr(alias = "midImage")
    private String midImage = null;

    /**
     * 节点资源小图
     */
    @Attr(alias = "smallImage")
    private String smallImage = null;

    /**
     * 节点能力
     */
    @Attr(alias = "ability")
    private String ability = null;

    public NodeResource() {
    }

    public NodeResource(String nodeName, String bigImage, String midImage, String smallImage, String groupName,
                        Class<? extends AbstractExecutor> clazz) {
        this.nodeName = nodeName;
        this.bigImage = bigImage;
        this.midImage = midImage;
        this.smallImage = smallImage;
        this.groupName = groupName;
        this.clazz = clazz.getName();
    }

    public NodeResource(String nodeName, String groupName, String image, Class<?> clazz) {
        this.nodeName = nodeName;
        this.groupName = groupName;
        this.smallImage = this.midImage = this.bigImage = image;
        this.clazz = clazz.getName();
    }

    public String getResId() {
        this.resId = MD5Utils.toMD5(this.clazz);
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public String getMidImage() {
        return midImage;
    }

    public void setMidImage(String midImage) {
        this.midImage = midImage;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
        this.resId = MD5Utils.toMD5(this.clazz);
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz.getName();
        this.resId = MD5Utils.toMD5(this.clazz);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    @Override
    public String toString() {
        return "NodeResource{" +
                "resId='" + resId + '\'' +
                ", clazz='" + clazz + '\'' +
                ", groupName='" + groupName + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", configUrl='" + configUrl + '\'' +
                ", bigImage='" + bigImage + '\'' +
                ", midImage='" + midImage + '\'' +
                ", smallImage='" + smallImage + '\'' +
                ", ability='" + ability + '\'' +
                '}';
    }
}
