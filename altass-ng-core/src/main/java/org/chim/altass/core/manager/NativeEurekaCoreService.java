package org.chim.altass.core.manager;


import org.chim.altass.core.AltassRpc;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.toolkit.JDFWrapper;

/**
 * Class Name: NativeEurekaCoreService
 * Create Date: 18-1-8 下午3:31
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * 本地调用rpc接口时，需要使用当前本地实现rpc方法
 */
@SuppressWarnings("Duplicates")
public class NativeEurekaCoreService implements AltassRpc {

    /**
     * 同层次执行任务节点
     *
     * @param entry     需要执行的节点
     * @param parentId  上一个执行任务id
     * @param contextId 执行上下文id
     */
    @Override
    public void runAsLinear(JDFWrapper entry, String parentId, String contextId) {
        try {
            IMissionScheduleCenter missionScheduleCenter = CentersManager.getInstance().getMissionScheduleCenter();
            AbstractExecutor abstractExecutor = missionScheduleCenter.generateExecutor(entry.restore(), parentId);
            if (abstractExecutor.validate()) {
                missionScheduleCenter.executeMissionLinear(abstractExecutor, new ExecuteContext(contextId));
            }
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子层执行任务节点
     *
     * @param entry     需要执行的节点
     * @param parentId  父节点id
     * @param contextId 上下文id
     */
    @Override
    public void runAsChild(JDFWrapper entry, String parentId, String contextId) {
        IMissionScheduleCenter missionScheduleCenter = CentersManager.getInstance().getMissionScheduleCenter();
        AbstractExecutor executor;
        try {
            executor = missionScheduleCenter.generateExecutor(entry.restore(), parentId);
            if (executor.validate()) {
                missionScheduleCenter.executeMissionAsChild(executor, new ExecuteContext(contextId));
            }
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
    }
}
