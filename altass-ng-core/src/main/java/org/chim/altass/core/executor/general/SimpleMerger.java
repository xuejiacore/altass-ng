package org.chim.altass.core.executor.general;

import org.chim.altass.base.utils.type.CollectionUtil;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractConvergentExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: SimpleMerger
 * Create Date: 11/28/18 12:52 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Executable(name = "simpleMerger", assemble = true, ability = ExecutorAbility.ABILITY_STREAMING)
@Resource(
        name = "基础数据合并",
        clazz = SimpleMerger.class,
        midImage = "res/images/executor/simpleMerger_bg.png",
        pageUrl = "nodeConfigs/ext/simpleMergerNodeConfig.jsp",
        readme = "META-INF/build-in/org.chim.altass.core.executor.general.simplemerger.md"
)
public class SimpleMerger extends AbstractConvergentExecutor {

    /**
     * To initialize streaming executor.
     *
     * @param executeId execute id
     */
    public SimpleMerger(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    protected boolean onChildInit() throws ExecuteException {
        // Convergent configuration
        return true;
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    @Override
    public boolean onNodeNormalProcessing() throws ExecuteException {
        return false;
    }

    @Override
    public boolean retryIfFail() throws ExecuteException {
        return false;
    }
}
