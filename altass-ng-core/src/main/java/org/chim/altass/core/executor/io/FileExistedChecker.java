package org.chim.altass.core.executor.io;

import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractCheckpointExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.File;

/**
 * Class Name: FileExistedChecker
 * Create Date: 2017/9/11 17:26
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 检测文件是否存在的检查点
 */
@Executable(name = "fileExistedChecker", assemble = true)
@Resource(name = "文件存在性", clazz = FileExistedChecker.class, midImage = "res/images/node/file_existed_bg.png", groupName = "文件")
public class FileExistedChecker extends AbstractCheckpointExecutor {

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public FileExistedChecker(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    protected boolean checkpoint(JobExecutionContext context) {
        System.err.println("定时检查中...");
        File file = new File("C:\\file_existed.txt");
        boolean exists = file.exists();
        if (exists) {
            System.err.println("文件：" + "C:\\file_existed.txt" + " 存在了");
        }
        return exists;
    }

    @Override
    protected JobDataMap initJobData() {
        return null;
    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }
}
