/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain
 * Author: Xuejia
 * Date Time: 2016/12/27 11:24
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.buildin.entry;

import org.chim.altass.base.parser.exml.Meta;
import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.EntryStatus;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.Node;
import org.chim.altass.core.domain.buildin.attr.*;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractExecutor;
import org.chim.altass.core.executor.ExecuteContext;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RCountDownLatch;

import java.util.List;

import static org.chim.altass.core.constant.ExecutorAttr.*;

/**
 * Class Name: Entry
 * Create Date: 2016/12/27 11:24
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 节点元素
 * <p>
 * 节点元素作为所有节点的共通性，所有的节点都拥有类似文件、数据库、HDFS、FTP等节点属性，这些属性都是通过元素属性定义，以下是
 * 对节点元素属性的列表清单：
 * ·节点的基本属性（生成ID、节点描述）；<br/>·坐标特性；<br/>·入参、出参；<br/>·入度、出度；<br/>·状态；<br/>·事件；<br/>·获得前驱后继；
 * <p>
 * 附加的操作特性，如：文件、数据库、HDFS等信息
 */
@Elem(alias = "entry", interfacing = true)
public class Entry extends Node implements IEntry {

    private static final long serialVersionUID = -488130814634913830L;

    /**
     * 节点分类
     */
    private Integer type = null;

    /**
     * 对应的执行器执行类
     */
    private Class<? extends AbstractExecutor> executorClz = null;

    private Boolean isChild = false;

    public Entry() {
        setNodeId(generateUUID());
    }

    public Entry(String nodeId) {
        setNodeId(nodeId);
    }

    public Entry(Integer x, Integer y) {
        super(x, y);
        setNodeId(generateUUID());
    }


    // =================================================================================================================
    // 节点公用属性定义  开始
    // =================================================================================================================
    @Attr(alias = "execClz")
    protected String executorClzName = null;

    /**
     * 时间属性
     */
    @Elem(alias = "time")
    protected ATime time = null;

    /**
     * 文件属性
     */
    @Elem(alias = "file")
    protected AFile file = null;

    /**
     * 主机
     */
    @Elem(alias = "host")
    protected AHost host = null;

    /**
     * 组
     */
    @Elem(alias = "region")
    protected ARegion region = null;

    /**
     * 脚本属性
     */

    /**
     * 数据库属性
     */
    @Elem(alias = "common")
    protected ACommon common = null;

    /**
     * 事件
     */
    @Elem(alias = "event")
    protected AEvent event = null;

    /**
     * 决策优先级，用于负载均衡以及定向路由
     */
    @Elem(alias = "priority")
    protected APriority priority = null;

    // =================================================================================================================
    // 节点公用属性定义  结束
    // =================================================================================================================

    /**
     * 获得执行元素的类型
     *
     * @return 执行器的类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置执行器的类型
     *
     * @param type 执行器的类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获得执行器的类型
     *
     * @return 执行器的类型
     */
    @Override
    public Class<? extends AbstractExecutor> getExecutorClz() {
        return executorClz;
    }

    /**
     * 暂停当前节点的运行
     * 只有运行状态中的节点能够被暂停
     *
     * @return 如果暂停成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean pause(boolean recursion) {
        Integer currentStatus = (Integer) RedissonToolkit.getInstance().getBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, nodeId);
        // 如果无法获得界定啊的运行状态或者节点状态不正确，那么不允许暂停节点
        if (currentStatus == null || EntryStatus.ENTRY_PAUSING.equals(currentStatus)) {
            return false;
        }

        // TODO:此处暂停需要判断是否是作业节点，如果是作业节点的暂停，需要同时暂停所有子任务，此处需要追加判断当前节点是否能够进行暂停操作
        if (recursion && (Boolean) this.obtainContext().getAttribute(ATTR_IS_JOB)) {
            Entries entries = ((Job) this).getEntries();
            List<IEntry> entries1 = entries.getEntries();
            for (IEntry iEntry : entries1) {
                iEntry.pause(true);
            }
        }
        RedissonToolkit.getInstance().setBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, nodeId, EntryStatus.ENTRY_PAUSING);
        return true;
    }

    @Override
    public boolean pauseSync() {
        // 只有等暂停都结束后，才会执行后续方法，恢复时，必须等待
        return false;
    }

    /**
     * 重新恢复已经被暂停运行的节点
     * 只有已暂停状态中的节点能够被恢复
     *
     * @return 如果恢复成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean resume() {
        Integer currentStatus = (Integer) RedissonToolkit.getInstance().getBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, nodeId);
        // 如果当期那节点状态不存在或者状态不正确，那么返回值为false
        if (currentStatus == null || EntryStatus.ENTRY_RESUMING.equals(currentStatus)) {
            return false;
        }
        // TODO:此处暂停需要判断是否是作业节点，如果是作业节点的恢复，需要同时恢复所有子任务，此处需要追加判断当前节点是否能够进行恢复操作
        if ((Boolean) this.obtainContext().getAttribute(ATTR_IS_JOB)) {
            Entries entries = ((Job) this).getEntries();
            List<IEntry> entries1 = entries.getEntries();
            for (IEntry iEntry : entries1) {
                iEntry.resume();
            }
        }
        RCountDownLatch controlCondition = RedissonToolkit.getInstance().getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CTL_CONDITION, nodeId, 1);
        controlCondition.countDown();
        return true;
    }

    @Override
    public boolean stop() throws ExecuteException {
        throw new ExecuteException(new UnsupportedOperationException());
    }

    /**
     * 更新作业
     *
     * @return 如果作业更新成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean immediateReloading() {
        throw new UnsupportedOperationException();
    }

    /**
     * 唤醒节点
     *
     * @param context 节点上下文
     * @return 如果唤醒成功，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean wakeup(ExecuteContext context) {
        RCountDownLatch countDownLatch = RedissonToolkit.getInstance().getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CDL, nodeId);
        countDownLatch.countDown();
        ExecuteContext ctx = obtainContext();
        ctx.addFluxContext((String) context.getAttribute(ATTR_EXECUTE_ID), context);
        return true;
    }

    /**
     * 获得count down latch
     *
     * @return 分布式count down latch
     */
    @Override
    public RCountDownLatch obtainGlobalLatch() {
        return RedissonToolkit.getInstance().getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CDL, nodeId);
    }

    /**
     * 获得执行上下文
     *
     * @return 执行器上下文
     */
    @Override
    public ExecuteContext obtainContext() {
        return new ExecuteContext(nodeId);
    }


    @Override
    public void setState(Integer state) {
        RedissonToolkit.getInstance().setBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, nodeId, EntryStatus.ENTRY_STOPPING);
        super.setState(state);
    }

    @Override
    public void wakeup() {
        RCountDownLatch countDownLatch = RedissonToolkit.getInstance().getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CONDITION, nodeId);
        countDownLatch.countDown();
    }

    @Override
    public void exitGracefully() {
        // TODO: 使用非kill -9 命令强行停止的时候，进行优雅停机

        // 标记当前节点的各种状态，用于恢复判定使用
    }

    /**
     * 根据执行器的执行类指定的执行器
     *
     * @param executorClz 执行器对应的类
     */
    public void setExecutorClz(Class<? extends AbstractExecutor> executorClz) {
        this.executorClz = executorClz;
        this.executorClzName = executorClz.getName();
    }

    public String getExecutorClzName() {
        return executorClzName;
    }

    /**
     * 根据执行器的类名称设置对应的执行器
     *
     * @param executorClzName 执行器的类名称
     */
    @SuppressWarnings("unchecked")
    public void setExecutorClzName(String executorClzName) {
        this.executorClzName = executorClzName;
        if (this.executorClzName != null) {
            try {
                this.executorClz = (Class<? extends AbstractExecutor>) Class.forName(executorClzName);
            } catch (ClassNotFoundException e) {
                System.err.println("无法找到节点元素执行器!" + executorClzName);
                e.printStackTrace();
            }
        }
    }

    public void inject(String key, Object val) {
        if (this.common == null) {
            this.common = new ACommon();
        }
        this.common.addAttr(key, val);
    }

    public void inject(Class<?> keyType, String json) {
        if (this.common == null) {
            this.common = new ACommon();
        }
        this.common.addAttr(keyType, json);
    }

    public void addJsonArg(String key, String json) {
        if (this.common == null) {
            this.common = new ACommon();
        }
        this.common.addJsonAttr(key, json);
    }

    public void addJsonArg(Class<?> keyType, String json) {
        if (this.common == null) {
            this.common = new ACommon();
        }
        this.common.addJsonAttr(keyType, json);
    }

    public APriority getPriority() {
        return priority;
    }

    public void setPriority(APriority priority) {
        this.priority = priority;
    }

    public AFile getFile() {
        return file;
    }

    public void setFile(AFile file) {
        this.file = file;
    }

    public ATime getTime() {
        return time;
    }

    public void setTime(ATime time) {
        this.time = time;
    }

    public AHost getHost() {
        return host;
    }

    public void setHost(AHost host) {
        this.host = host;
    }

    public ACommon getCommon() {
        return common;
    }

    public void setCommon(ACommon common) {
        this.common = common;
    }

    public AEvent getEvent() {
        return event;
    }

    public void setEvent(AEvent event) {
        this.event = event;
    }

    public ARegion getRegion() {
        return region;
    }

    public void setRegion(ARegion region) {
        this.region = region;
    }

    /**
     * 判断是否作为子作业进行
     *
     * @return 如果是子作业，那么返回值为true，否则返回值为false
     */
    public Boolean getIsChild() {
        return isChild;
    }

    /**
     * 标记一个作业是否是一个子作业
     *
     * @param isChild
     */
    public void setChild(Boolean isChild) {
        this.isChild = isChild;
    }

    public boolean getChild() {
        return this.isChild;
    }

    public void reset() {
        if (this.common != null)
            this.common.setExtAttr(this.common.getExtAttr());
    }

    @Override
    public String toString() {
        return "Entry{" +
                "type=" + type +
                ", executorClz=" + executorClz +
                ", isChild=" + isChild +
                ", executorClzName='" + executorClzName + '\'' +
                ", time=" + time +
                ", file=" + file +
                ", host=" + host +
                ", common=" + common +
                '}';
    }

    public Meta meta() {
        return new Meta(Entry.class);
    }
}
