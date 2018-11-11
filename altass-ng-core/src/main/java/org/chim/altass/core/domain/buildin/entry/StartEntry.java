/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.node.domain.Node
 * Author: Xuejia
 * Date Time: 2016/12/15 11:54
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.buildin.entry;


import org.chim.altass.base.parser.exml.Meta;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.EntryType;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.executor.general.StartExecutor;

/**
 * Class Name: StartEntry
 * Create Date: 2016/12/15 11:54
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 开始节点
 */
@Elem(alias = "startNode", version = "1.0", interfacing = true)
public final class StartEntry extends Entry {

    private static final long serialVersionUID = -144950656557981474L;

    {
        nodeType = EntryType.Node.TYPE_START_NODE;
    }

    public StartEntry() {
        this.setNodeId(generateUUID());
    }

    public StartEntry(Job job) {
        this.setNodeId(generateUUID());
        job.setStartNode(this);
        setExecutorClz(StartExecutor.class);
    }

    public StartEntry(Job job, Integer x, Integer y) {
        super(x, y);
        this.setNodeId(generateUUID());
        job.setStartNode(this);
        setExecutorClz(StartExecutor.class);
    }

    public StartEntry(String nodeId, Job job, Integer x, Integer y) {
        super(x, y);
        this.setNodeId(nodeId);
        job.setStartNode(this);
        setExecutorClz(StartExecutor.class);
    }

    public Meta meta() {
        return new Meta(StartEntry.class);
    }
}
