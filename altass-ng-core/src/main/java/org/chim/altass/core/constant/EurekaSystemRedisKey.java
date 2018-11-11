package org.chim.altass.core.constant;


import org.chim.altass.base.cache.BasicRedisKey;

/**
 * Class Name: EurekaSystemRedisKey
 * Create Date: 18-1-2 下午9:09
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public enum EurekaSystemRedisKey implements BasicRedisKey {

    EUREKA$SYSTEM_CDL(0L, "CountDownLatch"),                            // 全局阻塞银子
    EUREKA$SYSTEM_LCK(0L, "Lock"),                                      // 锁
    EUREKA$SYSTEM_CONDITION(0L, "condition"),                           // Condition
    EUREKA$SYSTEM_CTL_CONDITION(0L, "control_condition"),               // ControlCondition
    EUREKA$SYSTEM_CONTEXT(0L, "context"),                               // 运行上下文
    EUREKA$SYSTEM_CONTEXT_ATTR(0L, "context"),                          // 运行上下文
    EUREKA$SYSTEM_TOP_CONTEXT(0L, "context"),                           // 上级上下文
    EUREKA$SYSTEM_FLUX_CONTEXT(0L, "context"),                          // 同级上下文
    EUREKA$SYSTEM_IN_PARAM(0L, "InputParams"),                          // 输入参数
    EUREKA$SYSTEM_OUT_PARAM(0L, "OutputParams"),                        // 输出参数
    EUREKA$SYSTEM_STATUS(0L, "运行状态"),                                // 节点的运行状态

    EUREKA$GLOBAL_EXES(0L, "全局执行器映射表"),
    EUREKA$GLOBAL_EXES_SUB(0L, "全局执行器映射辅助表"),
    EUREKA$GLOBAL_EXES_LOCK(0L, "全局执行器映射表操作锁"),
    EUREKA$GLOBAL_EXES_BLKS(0L, "阻塞映射表"),
    EUREKA$GLOBAL_PRE_CTD(0L, "全局前置计算阻塞因子列表"),
    EUREKA$GLOBAL_CTX_CACHE(0L, "全局上下文缓存池"),
    EUREKA$GLOBAL_BLOCKS(0L, "全局执行器阻塞表"),


    EUREKA$LOCKER_PER_CDL(0L, "preCountDown"),
    EUREKA$LOCKER_BLK_MAP(0L, "blockMap"),
    EUREKA$LOCKER_WILL_EXE(0L, "willExecuteMap"),

    EUREKA$LOCKER_UPDATE_ANALYSIS(0L, "作业更新锁，组合作业id作为整个作业的更新锁"),


    //
    ALTASS$STREAM_TRANSACTION(0L, "事务"),
    ALTASS$STREAM_TRANSACTION_ROLLBACK(0L, "事务回滚")
    ;

    private Long ttl = null;
    private String desc = null;

    EurekaSystemRedisKey(Long ttl, String desc) {
        this.ttl = ttl;
        this.desc = desc;
    }


    @Override
    public String redisKey() {
        return this.name();
    }

    @Override
    public String desc() {
        return this.desc;
    }

    @Override
    public Long ttl() {
        return this.ttl;
    }

}
