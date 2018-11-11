/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.face
 * Author: Xuejia
 * Date Time: 2016/12/16 11:49
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.face;

/**
 * Class Name: IJobEvent
 * Create Date: 2016/12/16 11:49
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IJobEvent {
    /**
     * 作业成功
     */
    void onSuccess();

    /**
     * 作业异常
     */
    void onJobException(Exception e);
}
