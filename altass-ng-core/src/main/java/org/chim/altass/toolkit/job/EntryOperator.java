package org.chim.altass.toolkit.job;

import org.chim.altass.core.domain.IEntry;

/**
 * Class Name: EntryOperator
 * Create Date: 18-4-4 上午8:47
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface EntryOperator {

    boolean onCondition(IEntry entry);

    /**
     * 操作一个节点，并且允许递归往前操作
     *
     * @param entry 需要操作的界定啊
     * @return 如果返回值为true，那么说明需要递归往前操作，如果返回值为false，则说明只操作一次
     */
    boolean operate(IEntry entry, IEntry root);

}
