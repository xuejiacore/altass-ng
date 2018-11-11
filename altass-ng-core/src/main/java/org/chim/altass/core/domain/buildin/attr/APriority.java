package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;

/**
 * Class Name: APriority
 * Create Date: 18-3-7 下午8:35
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 *
 * <pre>
 * <code>
 * 节点执行的优先级，用于节点在执行的时候，选定适合下一步执行任务的节点，默认使用低CPU低内存的执行
 * 支持的策略类型如下：
 * - CPU_USE_PRIORITY               低CPU使用率优先（默认策略）
 * - CPU_LOAD_AVERAGE_PRIORITY      使用CPU load average 策略值
 * - MEMORY_USE_PRIORITY            低内存使用率优先策略
 * - RANDOM_PRIORITY                随机策略
 * - ROUND_ROBIN_PRIORITY           轮询
 * - LEAST_ACTIVE_PRIORITY          最少活跃调用优先
 * - CONSISTENT_HASH_PRIORITY       一致性Hash，相同参数的发送到统一提供者
 *
 * - MACHINE_PRIORITY               硬件机器组优先策略
 *                                  获取能够执行下一个节点的集群，再根据默认的策略执行下一个任务，
 *                                  适用于只有某些节点提供了某种特殊处理能力的情况
 * - ACCURATE_PRIORITY              精准指明 (ServiceId)
 *                                  用于精确指定下一个任务执行的节点
 * </code>
 * </pre>
 */
@Elem(alias = "priority", version = "1.0")
public class APriority {

    /**
     * 低CPU使用率优先（默认策略）
     */
    public static final String CPU_USE_PRIORITY = "CPU_USED";
    /**
     * 使用CPU load average 策略值
     */
    public static final String CPU_LOAD_AVERAGE_PRIORITY = "CPU_LA";
    /**
     * 低内存使用率优先策略
     */
    public static final String MEMORY_USE_PRIORITY = "MEMORY_USED";
    /**
     * 随机策略
     */
    public static final String RANDOM_PRIORITY = "RANDOM";
    /**
     * 轮询
     */
    public static final String ROUND_ROBIN_PRIORITY = "ROUND_ROBIN";
    /**
     * 最少活跃调用优先
     */
    public static final String LEAST_ACTIVE_PRIORITY = "LEAST_ACTIVE";
    /**
     * 一致性Hash，相同参数的发送到统一提供者
     */
    public static final String CONSISTENT_HASH_PRIORITY = "CONSISTENT_HASH";

    /**
     * 硬件机器组优先策略
     */
    public static final String MACHINE_PRIORITY = "MACHINE";
    /**
     * 精准指明 (ServiceId)
     */
    public static final String ACCURATE_PRIORITY = "ACCURATE";

    /**
     * 使用的优先级类型
     */
    @Attr(alias = "priority")
    private String priority = null;

    /**
     * 如果机器组优先，那么该字段不允许为null，并需要指明目标机器
     */
    @Attr(alias = "machine")
    private String machine = null;

    /**
     * 精确指明服务，该字段不允许为null，并需要指明服务id
     */
    @Attr(alias = "serviceId")
    private String serviceId = null;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
