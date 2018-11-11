package org.chim.altass.toolkit.job;

import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.connector.Connector;

/**
 * Class Name: SearchCallback
 * Create Date: 18-4-4 上午8:48
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface SearchCallback {
    /**
     * 搜索到节点回调
     *
     * @param entry     回调的节点
     * @param isLeaf    是否是叶子节点
     * @param hierarchy 节点的层级
     */
    void node(IEntry entry, boolean isLeaf, Integer hierarchy);

    /**
     * 搜索到连接器回调
     *
     * @param connector 连接器
     */
    void connector(Connector connector);
}
