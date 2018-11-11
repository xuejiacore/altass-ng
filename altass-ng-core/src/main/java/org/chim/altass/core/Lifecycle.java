/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.face
 * Author: Xuejia
 * Date Time: 2016/12/16 11:04
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core;


import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.RestoreContext;

/**
 * Class Name: ILifecycle
 * Create Date: 2016/12/16 11:04
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 生命周期接口
 */
public interface Lifecycle {

    /**
     * 初始化前
     */
    boolean beforeInit() throws ExecuteException;

    /**
     * 初始化
     */
    boolean onInit() throws ExecuteException;

    /**
     * 用于子类实现是否被手动停止过的判定
     *
     * @return 如果已经被手动停止过
     * @throws ExecuteException 执行异常
     */
    boolean hadStopManual() throws ExecuteException;

    /**
     * 清除手动中断的标记
     *
     * @return 如果手动中断成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException 执行异常
     */
    boolean clearStopTag() throws ExecuteException;

    /**
     * 用于暂停/终止后的作业恢复处理逻辑，由子类来实现响应的恢复逻辑
     *
     * @return 如果恢复成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException 执行异常
     */
    RestoreContext onRestore() throws ExecuteException;

    /**
     * 开始
     */
    boolean onStart() throws ExecuteException;

    /**
     * 预处理
     */
    boolean beforeProcess() throws ExecuteException;

    /**
     * 处理中
     */
    boolean processing() throws ExecuteException;

    /**
     * 处理后
     */
    void afterProcess() throws ExecuteException;

    void onFinally() throws ExecuteException;

    /**
     * 完成结束
     */
    void finished() throws ExecuteException;

    void clear();
}
