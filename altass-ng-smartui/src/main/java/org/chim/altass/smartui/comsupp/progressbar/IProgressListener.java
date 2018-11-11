/**
 * Project: x-framework
 * Package Name: org.ike.monitor.comsupp.progressbar.face
 * Author: Xuejia
 * Date Time: 2016/10/9 12:53
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.comsupp.progressbar;

/**
 * Class Name: IProgressListener
 * Create Date: 2016/10/9 12:53
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 进度监听器
 */
public interface IProgressListener {
    /**
     * 进度开始
     *
     * @param data 需要处理的数据
     */
    void onBegin(Object data);

    /**
     * 处理过程中
     *
     * @param percentage 当前处理的百分比
     * @param data       当前处理的数据
     * @return 如果返回值为true，那么将继续进度，如果返回值为false，那么将终止继续执行
     */
    boolean onProcess(double percentage, Object data);

    /**
     * 执行完成的回调
     *
     * @param data 当前处理的数据
     * @return 如果返回值为true，说明所有的内容正确处理完毕，如果返回值为false，那么说明处理的数据有异常
     */
    boolean onEnd(Object data);
}
