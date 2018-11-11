/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.face
 * Author: Xuejia
 * Date Time: 2016/12/16 15:40
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.face;


import org.chim.altass.core.exception.ExecuteException;

/**
 * Class Name: INodeEvent
 * Create Date: 2016/12/16 15:40
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface INodeEvent {
    /**
     * 节点开始
     */
    boolean onNodeStart();

    /**
     * 节点处理中
     */
    boolean onNodeProcessing() throws ExecuteException;

    /**
     * 节点处理成功
     */
    boolean onNodeSuccess();

    /**
     * 节点处理失败
     */
    boolean onNodeFailure();
}
