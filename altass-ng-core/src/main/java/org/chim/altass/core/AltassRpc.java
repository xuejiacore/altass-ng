package org.chim.altass.core;

import org.chim.altass.core.annotation.Version;
import org.chim.altass.toolkit.JDFWrapper;

/**
 * Class Name: IRpcService
 * Create Date: 18-1-8 下午11:56
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Version(version = "1.0.0.0")
public interface AltassRpc {

    /**
     * 线性同层执行任务
     *
     * @param entry     需要执行的节点
     * @param parentId  上一个执行任务id
     * @param contextId 执行上下文id
     */
    void runAsLinear(JDFWrapper entry, String parentId, String contextId);

    /**
     * 子层执行任务
     *
     * @param entry              需要执行的节点
     * @param parentJobId        上一层作业id
     * @param parentJobContextId 上一层context id
     */
    void runAsChild(JDFWrapper entry, String parentJobId, String parentJobContextId);
}
