package org.chim.altass.core.executor.general;

import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractDisassemblyExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

/**
 * Class Name: Iterator
 * Create Date: 11/23/18 12:38 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Executable(name = "iterator", assemble = true, ability = ExecutorAbility.ABILITY_STREAMING)
@Resource(
        name = "迭代器",
        clazz = Iterator.class,
        midImage = "res/images/executor/iterator_bg.png",
        pageUrl = "nodeConfigs/ext/iteratorNodeConfig.jsp",
        readme = "META-INF/build-in/org.chim.altass.core.executor.general.Iterator.md"
)
public class Iterator extends AbstractDisassemblyExecutor {

    /**
     * To initialize streaming executor.
     *
     * @param executeId execute id
     */
    public Iterator(String executeId) throws ExecuteException {
        super(executeId);
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
