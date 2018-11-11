package org.chim.altass.node.impl;

import org.chim.altass.core.AltassNode;
import org.chim.altass.core.annotation.Version;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.manager.CentersManager;
import org.chim.altass.core.manager.IMissionScheduleCenter;
import org.chim.altass.core.manager.UpdatePkgWrapper;
import org.chim.altass.toolkit.JDFWrapper;
import org.chim.altass.toolkit.JobToolkit;
import org.chim.altass.toolkit.job.EntryOperatorFactory;
import org.chim.altass.toolkit.job.UpdateAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class Name: CoreService
 * Create Date: 18-1-7 下午3:26
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("Duplicates")
@Service
@Version(version = "1.0.0.0")
public class CoreService implements AltassNode {

    @Override
    public String serverEcho(String msg) {
        System.out.println("服务端应答：" + msg);
        return msg + "| Server";
    }

    @Override
    public boolean submitMission(JDFWrapper entry) {
        return false;
    }

    @Override
    public boolean run(JDFWrapper entry) {
        Object restore = entry.restore();
        try {
            return CentersManager.getInstance().getMissionScheduleCenter().executeMissionAsChild((IEntry) restore) > 0;
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean pauseMission(JDFWrapper entry, boolean recursion) {
        IEntry entryToPause = entry.restore();
        return entryToPause.pause(recursion);
    }

    @Override
    public boolean resumeMission(JDFWrapper entry) {
        IEntry entryToResume = entry.restore();
        return entryToResume.resume();
    }

    @Override
    public boolean stopMission(JDFWrapper entry) {
        return false;
    }

    @Override
    public void checkStatus(JDFWrapper entry) {

    }

    @Override
    public boolean updateMission(UpdatePkgWrapper updatePkg) {
        UpdateAnalysis pkg = updatePkg.restore();
        try {
            System.err.println("核心服务接收到作业更新包:\n" + EXmlParser.toXml(pkg));
        } catch (XmlParserException e) {
            e.printStackTrace();
        }

        String jobId = pkg.getJobId();
        // 获得作业对象
        IMissionScheduleCenter missionScheduleCenter = CentersManager.getInstance().getMissionScheduleCenter();

        Job job = (Job) missionScheduleCenter.getExecutorById(jobId);
        if (job != null) {
            System.out.println("获得作业对象：" + job.getEntries().getEntries().size());

            // 目前节点暂不支持变更
            // pkg.getChangedEntries();

            // 获得所有即将删除的或者是新增的节点的前驱或者是后继
            List<IEntry> deleteEntries = pkg.getDeleteEntries();
            for (IEntry deleteEntry : deleteEntries) {

                List<String> precursorIds = deleteEntry.obtainPrecursors();
                System.err.println("获得当前删除操作的节点[" + deleteEntry.getNodeId() + "]的直接前驱, " + precursorIds);

                List<String> successorIds = deleteEntry.obtainSuccessors();
                System.err.println("获得当前删除操作的节点[" + deleteEntry.getNodeId() + "]的直接后继, " + successorIds);


                // 暂停当前操作的节点以及当前操作节点的临近运行前驱或者后继
                JobToolkit.operateClosestPrecursors(job, deleteEntry, EntryOperatorFactory.pauseClosestActiveOperator());
                JobToolkit.operateClosestSuccessor(job, deleteEntry, EntryOperatorFactory.pauseClosestActiveOperator());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        deleteEntry.resume();
                    }
                }).start();

            }

            List<IEntry> newEntries = pkg.getNewEntries();
            for (IEntry newEntry : newEntries) {
                List<String> precursorIds = newEntry.obtainPrecursors();
                System.err.println("获得当前新增操作的节点[" + newEntry.getNodeId() + "]的直接前驱, " + precursorIds);

                List<String> successorIds = newEntry.obtainSuccessors();
                System.err.println("获得当前新增操作的节点[" + newEntry.getNodeId() + "]的直接后继, " + successorIds);

                // 暂停当前操作的节点以及当前操作节点的临近运行前驱或者后继 TODO：此处需要用另外的方法，从更新包中获得当前节点的前驱后继
//                JobToolkit.operateClosestPrecursors(job, newEntry, EntryOperatorFactory.pauseClosestActiveOperator());
//                JobToolkit.operateClosestSuccessor(job, newEntry, EntryOperatorFactory.pauseClosestActiveOperator());
            }
            // TODO: 全部需要操作的节点暂停之后，调用直接前驱的 disconnect 卸载 或者 connect 挂载变更
            for (IEntry deleteEntry : deleteEntries) {

            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    missionScheduleCenter.notifyUpdate(pkg);
                }
            }).start();
            return true;
        } else {
            return false;
        }
    }
}
