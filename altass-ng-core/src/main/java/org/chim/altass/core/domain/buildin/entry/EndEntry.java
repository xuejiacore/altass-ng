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
import org.chim.altass.core.executor.general.EndExecutor;

/**
 * Class Name: EndEntry
 * Create Date: 2016/12/15 11:54
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 结束节点
 */
@Elem(alias = "endNode", version = "1.0", interfacing = true)
public final class EndEntry extends Entry {
    private static final long serialVersionUID = 7188347220947537793L;

    {
        nodeType = EntryType.Node.TYPE_END_NODE;
    }

    public EndEntry() {
        this.setNodeId(generateUUID());
        setExecutorClz(EndExecutor.class);
    }

    public EndEntry(Job job) {
        this.setNodeId(generateUUID());
        job.setEndNode(this);
        setExecutorClz(EndExecutor.class);
    }

    public EndEntry(Job job, Integer x, Integer y) {
        super(x, y);
        this.setNodeId(generateUUID());
        job.setEndNode(this);
        setExecutorClz(EndExecutor.class);
    }

    public EndEntry(String nodeId, Job job, Integer x, Integer y) {
        super(x, y);
        this.setNodeId(nodeId);
        job.setEndNode(this);
        setExecutorClz(EndExecutor.class);
    }

    public Meta meta() {
        return new Meta(EndEntry.class);
    }
}
