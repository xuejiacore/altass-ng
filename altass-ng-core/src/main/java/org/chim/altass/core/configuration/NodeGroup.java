/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.config
 * Author: Xuejia
 * Date Time: 2016/12/15 15:28
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.configuration;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: NodeGroup
 * Create Date: 2016/12/15 15:28
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点分组
 */
@Elem(alias = "group", version = "1.0")
public class NodeGroup {

    /**
     * 分组名称
     */
    @Attr(alias = "groupName")
    private String groupName = null;

    /**
     * 节点资源配置信息
     */
    @Elem(alias = "node-res", version = "1.0")
    private List<NodeResource> nodeResources = null;

    public NodeGroup() {
    }

    public NodeGroup(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<NodeResource> getNodeResources() {
        return nodeResources;
    }

    public void setNodeResources(List<NodeResource> nodeResources) {
        this.nodeResources = nodeResources;
    }

    /**
     * 添加节点资源
     *
     * @param resource 节点资源
     */
    public void addNodeResource(NodeResource resource) {
        if (this.nodeResources == null) {
            this.nodeResources = new ArrayList<NodeResource>();
        }
        resource.setGroupName(this.groupName);
        this.nodeResources.add(resource);
    }
}
