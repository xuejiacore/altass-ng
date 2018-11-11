/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.distribution
 * Author: Xuejia
 * Date Time: 2017/1/18 17:47
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.manager;

import org.apache.log4j.Logger;
import org.chim.altass.core.executor.face.IExecutorListener;
import org.chim.altass.core.AltassRpc;
import org.chim.altass.core.configuration.RedissonConfig;
import org.chim.altass.core.constant.SystemEnvironment;
import org.chim.altass.core.manager.monitor.DefaultHealthMonitor;
import org.chim.altass.core.manager.monitor.HealthMonitor;
import org.chim.altass.toolkit.RedissonToolkit;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Class Name: CentersManager
 * Create Date: 2017/1/18 17:47
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 所有中央服务的管理中心
 */
@Component("centersManager")
public class CentersManager implements ApplicationContextAware {

    protected Logger logger = Logger.getLogger(super.getClass());

    private static final String RUNNING_MODE_NATIVE = "native";                 // 本地单机运行模式
    private static final String RUNNING_MODE_DISTRIBUTION = "distribution";     // 集群分布式运行模式

    private static CentersManager centersManager = null;                        // 中央服务器的管理中心
    private static String runningMode = RUNNING_MODE_NATIVE;                    // 系统的运行模式

    private static RedissonConfig redissonConfig = null;                        // RedissonConfig
    private static IExecutorListener executorListener = null;                   // 执行器的监听回调

    private static AssembleCenter assembleCenter = null;                        // 装配中心
    private static HealthMonitor healthMonitor = null;

    /**
     * 任务调度中心
     */
    @Autowired(required = false)
    private IMissionScheduleCenter missionScheduleCenter = null;

    private ApplicationContext applicationContext = null;

    private AltassRpc coreService = null;

    /**
     * 初始化中心管理器
     */
    private CentersManager() {
        centersManager = this;
        missionScheduleCenter = new MissionScheduleCenter();      // 在没有指定运行模式的时候，将会默认使用一个本地运行的作业调度中心
        assembleCenter = new AssembleCenter();                      // 在没有制定运行模式的时候，也会加载装配中心的装配数据
        healthMonitor = new DefaultHealthMonitor();

    }

    /**
     * 获得中心管理服务的实例
     *
     * @return 中心管理服务的实例
     */
    public static CentersManager getInstance() {
        if (centersManager == null) {
            synchronized (CentersManager.class) {
                if (centersManager == null) {
                    centersManager = new CentersManager();
                }
            }
        }
        return centersManager;
    }

    /**
     * 获得任务调度中心实例
     *
     * @return 任务调度中心实例
     */
    public IMissionScheduleCenter getMissionScheduleCenter() {
        return missionScheduleCenter;
    }

    /**
     * 设置任务调度中心
     *
     * @param missionScheduleCenter 任务调度中心实例
     */
    public void setMissionScheduleCenter(IMissionScheduleCenter missionScheduleCenter) {
        this.missionScheduleCenter = missionScheduleCenter;
    }

    public void setLaunchService(boolean launch) {
        if (launch) {
            healthMonitor.launchService();
        }
    }

    /**
     * 获得运行模式
     *
     * @return 运行模式（native本地、distribution分布式）
     */
    public String getRunningMode() {
        return runningMode;
    }

    /**
     * 设置运行模式
     *
     * @param runningMode 运行模式
     */
    public void setRunningMode(String runningMode) {
        if (runningMode != null) {
            CentersManager.runningMode = runningMode.toLowerCase();
            logger.trace("CentersManager running mode is :" + CentersManager.runningMode);
            if (RUNNING_MODE_NATIVE.equals(runningMode)) {
                // 本地方式启动
                if (missionScheduleCenter == null) {
                    missionScheduleCenter = new MissionScheduleCenter();
                }
                SystemEnvironment.RUNNING_MODE = SystemEnvironment.RunningMode.NATIVE;
            } else if (RUNNING_MODE_DISTRIBUTION.equals(runningMode)) {
                // 分布式方式启动
                if (missionScheduleCenter == null) {
                    missionScheduleCenter = new DistMissionScheduleCenter();
                }
                SystemEnvironment.RUNNING_MODE = SystemEnvironment.RunningMode.DISTRIBUTION;
                throw new UnsupportedOperationException("DistMissionScheduleCenter had deprecated. Please use native mode.");
            } else {
                throw new IllegalArgumentException("Unknown running mode. Please specify native or distribution.");
            }
            Assert.notNull(missionScheduleCenter);
            missionScheduleCenter.bindingExecutingListener(executorListener);
        }
    }

    /**
     * 获得Redisson配置
     *
     * @return Redisson配置
     */
    public RedissonConfig getRedissonConfig() {
        return redissonConfig;
    }

    /**
     * 设置分布式 redis 客户端支持配置
     *
     * @param redissonConfig 配置
     */
    public void setRedissonConfig(RedissonConfig redissonConfig) {
        CentersManager.redissonConfig = redissonConfig;
        SystemEnvironment.redissonClient = redissonConfig.create();
        RedissonToolkit.getInstance(SystemEnvironment.redissonClient, redissonConfig.getRedisClient());
    }

    /**
     * 获得执行器监听器
     *
     * @return 执行器回调监听
     */
    public IExecutorListener getExecutorListener() {
        return executorListener;
    }

    /**
     * 设置执行器回调监听器
     *
     * @param executorListener 执行器监听器
     */
    public void setExecutorListener(IExecutorListener executorListener) {
        CentersManager.executorListener = executorListener;
        missionScheduleCenter.bindingExecutingListener(executorListener);
    }

    public AssembleCenter getAssembleCenter() {
        return assembleCenter;
    }

    public void setAssembleCenter(AssembleCenter assembleCenter) {
        CentersManager.assembleCenter = assembleCenter;
    }

    public HealthMonitor getHealthMonitor() {
        return healthMonitor;
    }

    public void setHealthMonitor(HealthMonitor healthMonitor) {
        CentersManager.healthMonitor = healthMonitor;
    }

    /**
     * 获得rpc调用服务
     *
     * @return rpc service
     */
    public AltassRpc getRpcService() {
        if (this.coreService == null) {
            try {
                this.coreService = (AltassRpc) applicationContext.getBean("_eurekaRpcService");
            } catch (NoSuchBeanDefinitionException e) {
                logger.warn("WARNING: USING NATIVE EUREKA CORE SERVICE.");
                this.coreService = new NativeEurekaCoreService();
            }
        }
        return this.coreService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
