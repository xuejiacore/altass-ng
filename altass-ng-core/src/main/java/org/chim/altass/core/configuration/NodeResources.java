/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.config
 * Author: Xuejia
 * Date Time: 2016/12/15 14:56
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.configuration;


import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.Description;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: NodeResources
 * Create Date: 2016/12/15 14:56
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点资源集
 */
@Elem(alias = "node-resources")
public class NodeResources {

    /**
     * 节点组
     */
    @Elem(alias = "groups", version = "1.0")
    private List<NodeGroup> nodeGroup = null;

    /**
     * 节点资源解释
     */
    @Elem(alias = "description", version = "1.0")
    private Description description = new Description("节点资源配置");


    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<NodeGroup> getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(List<NodeGroup> nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    /**
     * 向节点装配添加分组
     *
     * @param nodeGroup 需要添加的分组
     */
    public void addNodeGroup(NodeGroup nodeGroup) {
        if (this.nodeGroup == null) {
            this.nodeGroup = new ArrayList<NodeGroup>();
        }
        if (nodeGroup.getNodeResources() != null && nodeGroup.getNodeResources().size() != 0) {
            this.nodeGroup.add(nodeGroup);
        }
    }
}
