/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.face
 * Author: Xuejia
 * Date Time: 2016/12/19 13:40
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.face;


import org.chim.altass.core.constant.ExecuteStatus;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.ExecuteContext;

/**
 * Class Name: IExecutor
 * Create Date: 2016/12/19 13:40
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 执行器的通用接口
 */
public interface IExecutor {

    /**
     * 获得当前执行器的上下文
     *
     * @return 当前执行器的上下文
     */
    ExecuteContext getContext();

    /**
     * 设置当前执行器的输入参数
     *
     * @param inputParam 当前执行器的输入参数
     */
    void setInputParam(InputParam inputParam);

    /**
     * 添加输入参数
     *
     * @param param 需要添加的输入参数
     */
    void addInputParam(MetaData param);

    /**
     * 设置当前执行器的输出参数
     *
     * @param outputParam 当前执行器的输出参数
     */
    void setOutputParam(OutputParam outputParam);

    /**
     * 添加输出参数
     *
     * @param param 需要添加的输出参数
     */
    void addOutputParam(MetaData param);

    /**
     * 根据键获取执行器上下文属性
     *
     * @param key 键
     * @return 返回键对应的上下文属性值
     */
    Object getAttribute(Integer key);

    /**
     * 设置属性
     *
     * @param key   键
     * @param value 值
     */
    void putAttribute(Integer key, Object value) throws ExecuteException;

    /**
     * 设置当前的执行器执行状态
     *
     * @param status 执行状态
     */
    void setCurrentNodeExecuteStatus(ExecuteStatus status);

    /**
     * 判断当前执行器是否是某一个状态
     *
     * @param status 需要判断的状态
     * @return 如果是对应的状态，那么返回值为true，否则返回值为false
     */
    boolean isExecuteStatus(ExecuteStatus status);

    /**
     * 获得当前执行器的执行状态
     *
     * @return 当前执行器的执行状态
     */
    ExecuteStatus obtainExecuteStatus();

    /**
     * 线程进入等待状态
     *
     * @return 如果操作成功，那么返回值为true，否则返回值为false
     */
    boolean await() throws InterruptedException;

    /**
     * 唤醒线程
     * 如果为普通的作业线程，那么wakeup操作一次之后即可恢复线程运行状态，如果是阻塞型的线程，那么调用wakeup的次数与
     * 阻塞因子相关。
     *
     * @return 如果唤醒成功，那么返回值为true，否则返回值为false
     */
    boolean wakeup();

    /**
     * 唤醒线程，携带唤醒执行器信息<br/>
     * <font color='yellow'>该唤醒方法没有被具体实现，子类有需求需要Override</font>
     *
     * @param abstractExecutor 唤醒当前执行器的执行器
     * @return 如果唤醒成功，返回值为true，否则返回值为false
     */
    boolean wakeup(ExecuteContext abstractExecutor);
}
