package org.chim.altass.core;

import org.chim.altass.core.annotation.Version;
import org.chim.altass.core.manager.UpdatePkgWrapper;
import org.chim.altass.toolkit.JDFWrapper;

/**
 * Class Name: IEurekaCoreService
 * Create Date: 18-1-7 下午3:25
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Eureka的核心服务，用于提交、启动、运行、变更 作业和任务
 */
@Version(version = "1.0.0.0")
public interface AltassNode {

    String serverEcho(String msg);

    /**
     * 提交一个任务到任务中心，但不执行
     *
     * @param entry 需要提交的任务
     */
    boolean submitMission(JDFWrapper entry);

    /**
     * 运行一个任务
     *
     * @param entry 需要执行的任务
     */
    boolean run(JDFWrapper entry);

    /**
     * 暂停一个任务
     *
     * @param entry     需要暂停的任务
     * @param recursion 是否递归暂停所有子作业任务
     * @return 如果暂停成功，那么返回值为true，否则返回值为false
     */
    boolean pauseMission(JDFWrapper entry, boolean recursion);

    /**
     * 恢复一个已经暂停的任务
     *
     * @param entry 需要暂停的任务wrapper
     * @return 如果恢复暂停成功，那么返回值为true，否则返回值为false
     */
    boolean resumeMission(JDFWrapper entry);

    /**
     * 停止一个任务
     *
     * @param entry 需要停止的任务
     */
    boolean stopMission(JDFWrapper entry);

    /**
     * 检查任务的执行状态
     *
     * @param entry
     */
    void checkStatus(JDFWrapper entry);

    /**
     * 更新一个任务
     *
     * @param updatePkg 需要更新的任务包数据
     */
    boolean updateMission(UpdatePkgWrapper updatePkg);

}
