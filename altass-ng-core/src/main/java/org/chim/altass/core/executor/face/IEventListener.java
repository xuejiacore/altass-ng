/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.face
 * Author: Xuejia
 * Date Time: 2016/12/16 11:05
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.face;


import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.ExecuteContext;

/**
 * Class Name: IEventListener
 * Create Date: 2016/12/16 11:05
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 事件接口
 */
public interface IEventListener {
    /**
     * 执行异常事件
     *
     * @param e 异常
     */
    void onException(Exception e);

    /**
     * 跳过执行事件
     *
     * @param reason 跳过原因
     */
    void onSkip(String reason);

    /**
     * 中断执行器执行事件
     *
     * @throws ExecuteException 执行异常
     */
    void onPause() throws ExecuteException;

    /**
     * 继续执行执行器事件
     *
     * @throws ExecuteException 执行异常
     */
    void onResume() throws ExecuteException;

    /**
     * 停止运行执行器事件
     *
     * @throws ExecuteException 执行异常
     */
    void onStop() throws ExecuteException;

    /**
     * 线程唤醒事件，该事件用于监听阻塞行执行器的唤醒，每一次唤醒都会将对应的上下文传入<br/>
     * <font color='yellow'>该唤醒方法没有被具体实现，子类有需求需要Override</font>
     */
    void onWakeup(ExecuteContext fromContext);
}
