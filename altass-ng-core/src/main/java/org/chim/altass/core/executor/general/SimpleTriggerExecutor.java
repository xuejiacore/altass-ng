package org.chim.altass.core.executor.general;


import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractTriggerExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: SimpleTriggerExecutor
 * Create Date: 2017/9/15 12:49
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Executable(name = "simpleTriggerExecutor", assemble = true)
@Resource(name = "触发事件", clazz = SimpleTriggerExecutor.class, midImage = "res/images/node/trigger_bg.png")
public class SimpleTriggerExecutor extends AbstractTriggerExecutor {

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public SimpleTriggerExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }
}
