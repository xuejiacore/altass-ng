/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.executor.node.file
 * Author: Xuejia
 * Date Time: 2016/12/26 18:47
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.executor.io;



import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.domain.buildin.attr.AFile;
import org.chim.altass.core.domain.buildin.attr.AHost;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.List;

/**
 * Class Name: FileCreateAbstractNodeExecutor
 * Create Date: 2016/12/26 18:47
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * <font color="#0099cc">[文件创建执行器]</font>
 */
@Executable(name = "folderCreator", assemble = true)
@Resource(name = "创建文件夹", clazz = FileCreateExecutor.class, midImage = "res/images/node/createFolder_bg.png", groupName = "文件")
public class FileCreateExecutor extends AbstractNodeExecutor {

    private List<MetaData> inputParams = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public FileCreateExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    public void onArrange(UpdateAnalysis analysis) {

    }

    /**
     * 创建一个目录或者是文件
     *
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException
     */
    @Override
    public boolean onNodeProcessing() throws ExecuteException {
        // 获得文件属性
        AHost hostArrr = ((Entry) this.entry).getHost();
        AFile fileAttr = ((Entry) this.entry).getFile();

        return true;
    }

    @Override
    public boolean onNodeSuccess() {
        logger.info("文件创建节点处理成功");
        return true;
    }

    @Override
    public void onPause() throws ExecuteException {

    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }
}
