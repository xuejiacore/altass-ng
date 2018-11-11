package org.chim.altass.node.impl;

import org.chim.altass.core.AltassRpc;
import org.chim.altass.core.annotation.Version;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.core.manager.CentersManager;
import org.chim.altass.core.manager.IMissionScheduleCenter;
import org.chim.altass.toolkit.JDFWrapper;
import org.springframework.stereotype.Service;

/**
 * Class Name: RpcService
 * Create Date: 18-1-9 上午12:03
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("Duplicates")
@Service
@Version(version = "1.0.0.0")
public class RpcService implements AltassRpc {

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
