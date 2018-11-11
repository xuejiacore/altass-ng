/*
 * Project: x-framework
 * Package Name: org.ike.etl.core.executor
 * Author: Xuejia
 * Date Time: 2016/12/16 11:10
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.core.executor;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.chim.altass.core.AltassRpc;
import org.chim.altass.core.Lifecycle;
import org.chim.altass.core.ansi.AnsiColor;
import org.chim.altass.core.ansi.AnsiOutput;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.base.script.Script;
import org.chim.altass.base.utils.type.DateUtil;
import org.chim.altass.base.utils.type.StringUtil;
import org.chim.altass.core.configuration.EurekaSiteConfiguration;
import org.chim.altass.core.configuration.NodeResource;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.constant.Event;
import org.chim.altass.core.constant.ExecuteStatus;
import org.chim.altass.core.constant.Status;
import org.chim.altass.core.domain.IEntry;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.StatusInfo;
import org.chim.altass.core.domain.buildin.attr.ACommon;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;
import org.chim.altass.core.exception.BrokenExecutorException;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.exception.ExecutorRestoreExeception;
import org.chim.altass.core.executor.face.IEventListener;
import org.chim.altass.core.executor.face.IExecutor;
import org.chim.altass.core.executor.face.IExecutorListener;
import org.chim.altass.core.ext.ContextMap;
import org.chim.altass.core.manager.CentersManager;
import org.chim.altass.core.manager.IMissionScheduleCenter;
import org.chim.altass.core.manager.NodeResourceManager;
import org.chim.altass.toolkit.RedissonToolkit;
import org.chim.altass.toolkit.SysConfigManager;
import org.chim.altass.toolkit.job.UpdateAnalysis;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisPubSubConnection;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.pubsub.PubSubType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.chim.altass.core.constant.ExecutorAttr.*;

/**
 * Class Name: AbstractExecutor
 * Create Date: 2016/12/16 11:10
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 执行器抽象类
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public abstract class AbstractExecutor implements Runnable, Lifecycle, IEventListener, IExecutor {

    protected Logger logger = LogManager.getLogger(super.getClass());
    protected BufferedWriter executorLogger = null;
    private NodeResource nodeResource = NodeResourceManager.getInstance().getResource((Class<? extends AbstractExecutor>) super.getClass());
    private IExecutorListener executorListener = null;
    private StatusInfo status = null;
    protected static final EurekaSiteConfiguration ETL_SITE_CONFIG = SysConfigManager.getInstance().getConfiguration();
    protected static final String EXECUTOR_LOG_DIR = ETL_SITE_CONFIG.getLogConfig().getExecutorLogDir();

    /**
     * 任务调度中心的实例，任务调度中心是所有任务执行器的核心部分，负责任务执行器的执行控制以及执行节点控制
     * 任务调度中心实例由中心管理器配置管理，中心管理器的开销较大，以单例模式存在
     */
    protected IMissionScheduleCenter missionScheduleCenter = CentersManager.getInstance().getMissionScheduleCenter();
    protected AltassRpc rpcService = CentersManager.getInstance().getRpcService();
    private ExecutorPubSubListener<Object> eventListener = new ExecutorPubSubListener<Object>();

    //    // 常量标识定义开始
    public static final Integer STATUS_SUCCESS = 0x1;                           // 执行成功标识
    public static final Integer STATUS_FAILURE = 0x2;                           // 执行失败标识
    /**
     * 当前作业的下一个节点的可执行队列的
     */
    private static int WILL_EXEC_EXECUTOR_QUEUE_SIZE = 200;
    // 常量标识定义结束

    protected String executeId = null;                                          // 执行器的执行ID
    protected String parentExecuteId = null;                                    // 父执行器
    protected HashMap<Object, Object> systemVariables = new HashMap<>();        // 系统变量

    protected ExecuteStatus currentNodeExecuteStatus = ExecuteStatus.FAILURE;   // 执行器的执行状态，默认为失败
    protected IEntry entry = null;                                              // 与当前执行器绑定的节点元素

    protected ExecuteContext context = null;                                    // 节点的上下文信息
    protected RCountDownLatch latch;                                            // 计数锁
    private final RCountDownLatch runningCondition;
    private RCountDownLatch controlCondition;
    protected ArrayBlockingQueue<IEntry> willExecExecutor = new ArrayBlockingQueue<>(WILL_EXEC_EXECUTOR_QUEUE_SIZE);
    private RedisPubSubConnection pubSubConnection = null;

    protected boolean ignoreError = false;                                      // 是否忽略错误
    protected boolean isSkipped = false;                                        // 跳过执行
    protected boolean isInterrupted = false;                                    // 中断
    protected boolean isJob = false;                                            // 当前是否是作业
    protected String jobId = null;                                              // 当前作业id
    protected Script script = null;                                             // 內建脚本解释器
    protected RedissonToolkit distToolkit = null;                               // 分布式处理工具


    /**
     * Initialize an Abstract-Executor with Execute Id
     *
     * @param executeId execute id
     */
    public AbstractExecutor(String executeId) throws ExecuteException {
        this.executeId = executeId;
        // Clear Distribution Cached Object
        this.distToolkit = RedissonToolkit.getInstance();
//        this.clearDistCache();
        this.runningCondition = distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CONDITION, executeId, 1);
        this.controlCondition = distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CTL_CONDITION, executeId, 1);

        this.context = new ExecuteContext(executeId);
        this.context.addAttribute(ATTR_EXECUTE_ID, executeId);                                                                  // 设置执行ID到当前执行器的上下文执行属性中
        this.isJob = AbstractJobExecutor.class.isAssignableFrom(super.getClass());
        this.context.addAttribute(ATTR_IS_JOB, this.isJob);                   // 设置当前是否是作业执行器
        this.context.addAttribute(ATTR_IS_CHILD_JOB, false);                                                            // 默认设置不是子作业
        this.status = new StatusInfo(executeId);
        this.initSystemVariables();                                                                                             // 初始化所有执行器的统一系统参数
        this.context.addAttribute(ATTR_SYSTEM_VARIABLES, this.systemVariables);                                                 // 将系统参数配置到节点执行器的上下中
    }

    /**
     * 初始化内部事件监听器
     */
    private void initializeEventListener() {
        RedisClient redisClient = distToolkit.getRedisClient();
        pubSubConnection = redisClient.connectPubSub();
        pubSubConnection.addListener(eventListener);

        // 监听作业
        String jobId = getCurrentJob().getJobId();
        String channel = Event.PUBLISH_JOB_REARRANGEMENT.redisKey() + "#" + jobId;

        pubSubConnection.subscribe(StringCodec.INSTANCE, channel);
    }

    /**
     * 初始化节点执行器系统参数
     * 该系统参数作为整个节点生命周期中运行的内部系统参数
     * 如果是父层作业节点的系统参数，还能够被子节点共享
     */
    private void initSystemVariables() {
        this.systemVariables.put(SYS_VAR_KEY_AUTHOR, "J·Chim");
        this.systemVariables.put(SYS_VAR_KEY_CURRENT_DATE, DateUtil.format(new Date(), DateUtil.SDFYYYYMMDDHHmmSS));
        customSystemVariables(this.systemVariables);
    }

    /**
     * 自定义的节点执行器系统参数
     *
     * @param systemVariables 系统参数
     */
    protected void customSystemVariables(HashMap<Object, Object> systemVariables) {
        // 子类可以通过重写该方法来继续添加初始化的系统参数，本抽象类中是一个空实现
    }


    /**
     * 执行器运作生命周期执行根流程
     * <p>
     * 所有的执行器都以Executor抽象执行器作为基类，展开Implement 或 Override 相关的生命周期方法，以此来实现执行器的内部
     * 操作逻辑
     * <p>
     * 2017年1月17日13:13:26
     * -- By Xuejia
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public void run() {
        long beginTime = System.currentTimeMillis();
        try {
            context.addAttribute(ATTR_EXE_BEGIN_TIME, beginTime);
        } catch (ExecuteException ignored) {
        }
        this.prepareLogFile();
        boolean isBeforeSuccess;
        boolean isProcessingSuccess = false;
        changeExecutingStatus(Status.RUNNING);
        EXECUTOR_LOGGER("Executor node [" + executeId + "] Thread Id: [" + Thread.currentThread().getId() + "] Running");
        try {

            // 在启动前设置跳过执行
            if (this.isSkipped) {
                this.onSkip("Current executor will be skipped after normal running.");
                EXECUTOR_LOGGER("msg", "Current executor will be skipped after normal running.");
                missionScheduleCenter.remove(this);
                changeExecutingStatus(Status.SKIPPED);
                return;
            }

            changeExecutingStatus(Status.INITIALIZING);
            EXECUTOR_LOGGER("msg", "Executor on initializing.",
                    "function", "onInit()",
                    "status", "START");
            this.statusChecking();
            if (this.beforeInit() && this.onInit()) {
                this.statusChecking();
                EXECUTOR_LOGGER("msg", "Executor had been initialized.",
                        "function", "onInit()",
                        "status", "END");

                changeExecutingStatus(Status.INITIALIZED);

                // 初始化当前执行器的内部事件监听器
                this.initializeEventListener();
                if (hadStopManual()) {
                    EXECUTOR_LOGGER("msg", "Executor had been stop by manual or interrupt by admin, try to restoring now.",
                            "function", "hadStopManual()",
                            "status", "START");

                    if (this.onRestore() != null) {
                        EXECUTOR_LOGGER("msg", "Restore executor from last status successful.",
                                "function", "onRestore()");
                        if (this.clearStopTag()) {
                            EXECUTOR_LOGGER("msg", "The tag of Pause/Interruption had been clear successful.",
                                    "function", "clearStopTag()");
                        } else {
                            EXECUTOR_LOGGER("msg", "The tag of Pause/Interruption clear failure");
                            throw new ExecutorRestoreExeception("The tag of Pause/Interruption clear failure");
                        }
                    } else {
                        EXECUTOR_LOGGER("msg", "Fail to restore current executor.");
                        throw new ExecutorRestoreExeception("Fail to restore current executor.");
                    }

                    EXECUTOR_LOGGER("msg", "作业中断/暂停重启恢复", "hadStopManual()", "status", "END");
                }

                /*
                 * 执行到此处，已经初始化的内容有：
                 * 1、线程锁；2、执行器上下文（阻塞器的上下文在此处已经完成所有初始化）；
                 */
                EXECUTOR_LOGGER("msg", "Obtain input parameters.",
                        "function", "obtainOutputParamAsInput()",
                        "status", "START");

                this.obtainOutputParamAsInput();
                EXECUTOR_LOGGER("msg", "Obtain input parameters.",
                        "function", "obtainOutputParamAsInput()",
                        "status", "END");

                changeExecutingStatus(Status.STARTING);
                // 初始化与启动
                EXECUTOR_LOGGER("msg", "Executor starting.", "function", "onStart()", "status", "START");
                this.statusChecking();
                if (this.onStart()) {
                    this.statusChecking();
                    EXECUTOR_LOGGER("msg", "Executor started.", "function", "onStart()", "status", "END");

                    changeExecutingStatus((Boolean) context.getAttribute(ATTR_IS_JOB) ?
                            Status.JOB_STATED : Status.STARTED);

                    // 预处理
                    EXECUTOR_LOGGER("msg", "Processing before process.", "function", "beforeProcess()",
                            "status", "START");
                    InputParam inputParam = context.getInputParam();
                    EXECUTOR_LOGGER("msg", inputParam == null ? "No any input parameters." :
                            "Input Parameters [" + inputParam.getParams().size() + "]", "inputParameters", inputParam);

                    this.statusChecking();
                    if (isBeforeSuccess = this.beforeProcess()) {
                        EXECUTOR_LOGGER("msg", "Finish to prepare processing before process.",
                                "function", "beforeProcess()", "status", "END");

                        logger.trace(printExecStatus());
                        changeExecutingStatus(Status.FINISHED_BEFORE_PROCESS);
                        // 作业处理核心
                        this.statusChecking();
                        if (isProcessingSuccess = this.processing()) {
                            changeExecutingStatus(Status.FINISHED_PROCESSING);
                            // 预处理并且处理后成功
                            EXECUTOR_LOGGER("msg", "Do something after process successful.",
                                    "function", "afterProcess()", "status", "START");
                            this.statusChecking();
                            this.afterProcess();
                            this.statusChecking();
                            EXECUTOR_LOGGER("msg", "After process had finished.",
                                    "function", "afterProcess()", "status", "END");
                            changeExecutingStatus(Status.FINISHED_AFTER_PROCESS);
                        }
                    }

                    String errInfo = "Pretreatment encountered errors, but exception had been caught or hidden.";
                    if (!isBeforeSuccess) {
                        EXECUTOR_LOGGER();
                        throw new ExecuteException(errInfo);
                    }
                    if (!isProcessingSuccess) {
                        EXECUTOR_LOGGER(errInfo);
                        throw new ExecuteException(errInfo);
                    }
                }
            }
        } catch (Exception e) {
            if (!this.status.getStatusCode().equals(Status.ENTRY_STOPPED)) {
                // 异常处理
                EXECUTOR_LOGGER("msg", "Begin to process exception.",
                        "function", "onException()", "status", "START");
                EXECUTOR_LOGGER("exception", e.getMessage(), "stackTrace", e.getStackTrace());
                this.onException(e);
                logger.error("Executor encountered error: " + e.getMessage());
                EXECUTOR_LOGGER("msg", "Exception processor.", "function", "onException()", "status", "END");
                changeExecutingStatus(Status.EXCEPTION);

                if (ignoreError) {
                    // 忽略异常，继续执行后处理
                    try {
                        EXECUTOR_LOGGER("msg", "Handling after ignoring exception.", "function", "afterProcess()", "status", "START");
                        this.statusChecking();
                        this.afterProcess();
                        this.statusChecking();
                        EXECUTOR_LOGGER("msg", "Finished handling after ignoring exception.", "function", "afterProcess()", "status", "END");
                    } catch (ExecuteException exp) {
                        EXECUTOR_LOGGER("exception", exp.getMessage(), "stackTrace", exp.getStackTrace());
                        exp.printStackTrace();
                    }
                }
            } else {
                EXECUTOR_LOGGER("msg", "Manual stopping.");
            }
        } finally {
            // 必处理
            try {
                this.statusChecking();

                EXECUTOR_LOGGER("msg", "The finally processor.", "function", "onFinally()", "status", "START");
                // 移除当前监听
                pubSubConnection.removeListener(eventListener);
                this.beforeOnFinally();
                this.onFinally();
                EXECUTOR_LOGGER("msg", "Finally process had finished..", "function", "onFinally()", "status", "END");
                changeExecutingStatus(Status.FINISHED_FINALLY);
            } catch (ExecuteException e) {
                EXECUTOR_LOGGER("exception", e.getMessage(), "stackTrace", e.getStackTrace());
                e.printStackTrace();

                if (e instanceof BrokenExecutorException) {
                    IEntry jobAbstractExecutor = getJobExecutor();
                    if (jobAbstractExecutor != null) {
                        Long threadId = (Long) jobAbstractExecutor.obtainContext().getAttribute(ATTR_THREAD_ID);
                        logger.error("An extremely grave mistake that cannot be salvaged [" + e.getMessage() + "], Compulsory termination of operation [" + jobAbstractExecutor.getNodeId() + "]");

/*
                        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
                        ThreadInfo info = tmx.getThreadInfo(threadId);
*/

                        ThreadGroup group = Thread.currentThread().getThreadGroup();
                        while (group != null) {
                            Thread[] threads = new Thread[(int) (group.activeCount() * 1.2)];
                            int count = group.enumerate(threads, true);
                            for (int i = 0; i < count; i++) {
                                if (threadId == threads[i].getId()) {
                                    threads[i].interrupt();
                                    break;
                                }
                            }
                            group = group.getParent();
                        }

                    }
                }

            }
        }

        try {
            if (!Status.EXCEPTION.equals(Status.value(this.entry.getState()))) {
                changeExecutingStatus(Status.FINISHED);
            }

            EXECUTOR_LOGGER("msg", "Processor Over.", "function", "finished()", "status", "START");
            this.finished();
            EXECUTOR_LOGGER("msg", "Processor Over Finished.", "function", "finished()", "status", "END");
        } catch (ExecuteException e) {
            EXECUTOR_LOGGER("exception", e.getMessage(), "stackTrace", e.getStackTrace());
            e.printStackTrace();
        } finally {
            double execCostTime = 0;
            try {
                beforeRemoveFromMissionSchedule();
                // 处理结束
                missionScheduleCenter.remove(this);
                long endTime = System.currentTimeMillis();
                context.addAttribute(ATTR_EXE_END_TIME, endTime);
                execCostTime = (endTime - beginTime) / 1000.0;
                context.addAttribute(ATTR_EXE_COST_TIME, execCostTime);
            } catch (ExecuteException ignored) {
            }
            EXECUTOR_LOGGER("msg", "Job or Executor Running Duration",
                    "duration", execCostTime,
                    "children", this.entry.getOutDegree(),
                    "childrenSize", this.entry.getOutDegree().size(),
                    "executorContext", context);
            try {
                if (executorLogger != null) {
                    executorLogger.flush();
                    executorLogger.close();
                }
            } catch (IOException e) {
                EXECUTOR_LOGGER("exception", e.getMessage(), "stackTrace", e.getStackTrace());
                e.printStackTrace();
            }
        }
        // 根据作业类型响应区分子作业完成以及主作业完成
        if ((Boolean) context.getAttribute(ATTR_IS_CHILD_JOB)) {
            changeExecutingStatus(Status.ENTRY_CHILD_FINISHED);
        } else if ((Boolean) context.getAttribute(ATTR_IS_JOB)) {
            changeExecutingStatus(Status.JOB_FINISHED);
        } else {
            changeExecutingStatus(Status.ENTRY_FINISHED);
        }
        // 清理作业运行中产生的分布式作业缓存锁、条件以及各种系统变量
        this.clearDistCache(executeId);
        this.clear();
    }

    @Override
    public void clear() {

    }

    protected void beforeOnFinally() throws ExecuteException {

    }

    protected void beforeRemoveFromMissionSchedule() throws ExecuteException {
        missionScheduleCenter.releaseBlocking(executeId);
    }

    /**
     * 初始化当前作业节点的运行时日志文件
     */
    private void prepareLogFile() {
        try {
            String yyyyMMdd = DateUtil.format(new Date(), DateUtil.DEFAULT_PATTERN);
            String logDirPath = (EXECUTOR_LOG_DIR.endsWith(File.separator) ?
                    EXECUTOR_LOG_DIR + yyyyMMdd :
                    EXECUTOR_LOG_DIR + File.separator + yyyyMMdd)
                    + File.separator + getCurrentJob().getJobId() + File.separator;

            File file = new File(logDirPath);
            if (!(file.exists() || file.isDirectory())) {
                boolean mkdirsResult = file.mkdirs();
            }
            File logFile = new File(logDirPath + executeId + ".log");
            if (logFile.exists()) {
                int index = 1;
                logFile = new File(logDirPath + executeId + ".log" + "." + index);
                while (logFile.exists()) {
                    logFile = new File(logDirPath + executeId + ".log" + "." + (++index));
                }
            }
            logger.info("[" + executeId + "]日志文件：" + logFile.getAbsolutePath());
            executorLogger = new BufferedWriter(new FileWriter(logFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变当前执行状态
     *
     * @param status 执行状态
     */
    private void changeExecutingStatus(Status status) {
        this.entry.setState(status.getStatusCode());
        this.status.setStatusCode(status.getStatusCode());
        if (executorListener != null)
            executorListener.onStatusChanging(executeId, this.status);
    }

    /**
     * 将上一个执行节点的输出参数作为本节点的输出参数之一<br/>
     *
     * <font color='yellow'>注意：为了防止在参数转化的过程中覆盖用户自定义的入参，因此，在参数转化时，会在原有的参数名称前缀
     * 两个美元符:<u>$$</u></font>
     */
    private void obtainOutputParamAsInput() {
        // 获得上一个节点的输出作为本节点的输入参数的一部分
        ContextMap fluxContext = context.getFluxContext(); // 节点上下文信息
        if (fluxContext != null) {
            // 如果是阻塞等类型，会拥有多个上个节点的上下文信息
            for (ExecuteContext lastContext : fluxContext.values()) {
                OutputParam outputParam = lastContext.getOutputParam();
                if (outputParam != null) {
                    List<MetaData> params = outputParam.getParams();
                    if (params != null) {
                        for (MetaData metaData : params) {
                            metaData.setField("$$" + metaData.getField());
                            addInputParam(metaData);
                        }
                    }
                }
            }
        }
        /*
            在获得输出参数作为当前节点的输入参数的时候，需要将上层节点的系统参数附加到当前节点中，即父节点的系统属性或者
            是变量能够被子节点共享使用
         */
        ExecuteContext topContext = this.getContext().getTopContextByKey((String) this.getAttribute(ATTR_EXECUTE_PARENT_ID));
        if (topContext != null) {
            HashMap<Object, Object> topSysVar = (HashMap<Object, Object>) topContext.getAttribute(ATTR_SYSTEM_VARIABLES);
            this.systemVariables.put("job.system.variables", topSysVar);
        }
    }


    // ----------------------------------------------------------------------------- 执行器公共方法 开始

    /**
     * 设置当前执行节点的上下文信息
     *
     * @param context 上下文信息
     */
    public void setContext(ExecuteContext context) {
        this.context = context;
    }

    /**
     * 获得当前执行器的上下文对象
     *
     * @return 当前执行器的上下文对象
     */
    @Override
    public ExecuteContext getContext() {
        return this.context;
    }

    /**
     * 设置当前执行器的输入参数
     *
     * @param inputParam 当前执行器的输入参数
     */
    @Override
    public void setInputParam(InputParam inputParam) {
        this.context.setInputParam(inputParam);
    }

    /**
     * 添加执行器的输入参数
     *
     * @param param 需要添加的输入参数
     */
    @Override
    public void addInputParam(MetaData param) {
        this.context.addInputParam(param);
    }

    /**
     * 设置当前执行器的输出参数
     *
     * @param outputParam 当前执行器的输出参数
     */
    @Override
    public void setOutputParam(OutputParam outputParam) {
        this.context.setOutputParam(outputParam);
    }

    /**
     * 添加输出参数
     * 输出参数的调用一定是在执行器的内部调用，因此添加输出参数的时候一定往当前处理器的上下文内容中添加，同时往节点中添加输出参数
     *
     * @param param 需要添加的输出参数
     */
    @Override
    public void addOutputParam(MetaData param) {
        // 在当前上下文内容中添加了输出参数之后，下一个执行节点可以从上下文中获得当前节点产生的输出参数
        this.context.addOutputParam(param);
        this.entry.addOutputParameter(param);
    }

    /**
     * 根据键获得当前执行器的执行属性
     *
     * @param key 键
     * @return 返回键对应当前执行器的执行属性
     */
    @Override
    public Object getAttribute(Integer key) {
        return this.context.getAttribute(key);
    }

    /**
     * 向当前的执行器中添加一个执行属性
     *
     * @param key   键
     * @param value 值
     */
    @Override
    public void putAttribute(Integer key, Object value) throws ExecuteException {
        this.context.addAttribute(key, value);
    }

    /**
     * 设置当前执行器的执行状态
     *
     * @param status 执行状态
     */
    public void setCurrentNodeExecuteStatus(ExecuteStatus status) {
        this.currentNodeExecuteStatus = status;
    }

    /**
     * 判断当前执行器的执行状态
     *
     * @param status 需要判断的状态
     * @return 如果状态一致，那么返回值为true，否则返回值为false
     */
    @Override
    public boolean isExecuteStatus(ExecuteStatus status) {
        return this.currentNodeExecuteStatus.equals(status);
    }

    /**
     * 获得当前执行器的执行状态
     *
     * @return 当前执行器的执行状态
     */
    @Override
    public ExecuteStatus obtainExecuteStatus() {
        return this.currentNodeExecuteStatus;
    }

    /**
     * 将一个节点添加到作业的节点队列中
     *
     * @param jobId 作业ID
     * @param node  即将需要执行的节点任务
     * @return 当前作业ID即将执行的节点数量
     */
    protected int addToExecutingQueue(String jobId, IEntry node, AbstractExecutor lastOrParentExec) throws ExecuteException {
        Class<? extends AbstractExecutor> executorClz = node.getExecutorClz();

        if (executorClz == null) {
            throw new BrokenExecutorException("无法从当前ClassLoader中找到执行类：" + ((Entry) node).getExecutorClzName());
        }

        if (AbstractBlockingExecutor.class.isAssignableFrom(executorClz)) {
            validateBlockingAndPut(node, willExecExecutor, lastOrParentExec);
        } else if (AbstractStreamNodeExecutor.class.isAssignableFrom(executorClz)) {
            validateStreamAndPut(node, willExecExecutor, lastOrParentExec);
        } else if (AbstractJobExecutor.class.isAssignableFrom(executorClz) && ((Job) node).getIsObstructive()) {
            validateBlockingAndPut(node, willExecExecutor, lastOrParentExec);
        } else {
            willExecExecutor.add(node);
        }
        return willExecExecutor.size();
    }

    private void validateStreamAndPut(IEntry node, ArrayBlockingQueue<IEntry> willExecExecutor, AbstractExecutor lastOrParentExec) {
        Boolean isBlocking = missionScheduleCenter.isEntryBlocking(node); // 判断当前的阻塞节点池中是否存在某一个阻塞节点（阻塞型在运行时仅能有一个线程）

        if (!isBlocking) {
            willExecExecutor.add(node);
        }
    }

    /**
     * 验证是否是阻塞型的节点，如果是阻塞型的节点，处理如下：
     *
     * @param entry     当前即将加入的节点
     * @param executors 即将执行的节点队列
     */
    private void validateBlockingAndPut(IEntry entry, ArrayBlockingQueue<IEntry> executors, AbstractExecutor lastOrParentExec) {
        Boolean isBlocking = missionScheduleCenter.isEntryBlocking(entry); // 判断当前的阻塞节点池中是否存在某一个阻塞节点（阻塞型在运行时仅能有一个线程）

        if (!isBlocking) {
            executors.add(entry);
        } else {
            // 如果在阻塞中，那么需要延时200ms，调用阻塞节点执行器的wakeup()[countDownLatch.countDown()]，即说明有相同的
            // 阻塞源头完成，当阻塞条件为0的时候，阻塞节点就会开始执行
            try {
                Thread.sleep(200);          // 放置多个任务同时完成，但是阻塞节点尚未完成启动的情况
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            IEntry blockAbstractExecutor = missionScheduleCenter.getExecutorById(entry.getNodeId());
            blockAbstractExecutor.wakeup(lastOrParentExec.getContext());
            long count = blockAbstractExecutor.obtainGlobalLatch().getCount();
            logger.trace("Blocking executor [" + entry.getNodeType() + "_" + entry.getNodeId() + "] Woke up by thread named [" +
                    Thread.currentThread().getName() + "] Remain CountDownCnt [" + count + "]");
            if (count == 0) {
                missionScheduleCenter.releaseBlocking(entry.getNodeId());
                logger.trace("Blocking executor [" + entry.getNodeType() + "_" + entry.getNodeId() + "] Unlock and will running.");
            }
        }
    }

    // ----------------------------------------------------------------------------- 执行器公共方法 结束


    // ----------------------------------------------------------------------------- 锁处理 开始

    public RCountDownLatch getLatch() {
        return latch;
    }

    /**
     * 在线程启动的时候，如果有子节点或者是子作业，那么需要等待其它的子节点或者是子作业结束之后，继续处理其它的事情，
     * 等待子节点或者是子作业完全处理完毕之后，再唤起父线程执行相关的收尾代码
     */
    @Override
    public boolean await() throws InterruptedException {
        // 优先考虑响应中断，而不是获取锁；使用此方式获的锁，允许在等待时由其他线程的通过Thread.interrupt()方法来中断等待线程而直接返回
        try {
            this.runningCondition.await();
        } catch (InterruptedException e) {
            Thread thread = Thread.currentThread();
            logger.debug("当前线程 " + thread.getName() + " Status: " + thread.getState().name() + " Interrupted");
            this.runningCondition.countDown();
            throw e;
        }
        return true;
    }

    /**
     * 唤醒一个执行器
     */
    @Override
    public boolean wakeup() {
        this.runningCondition.countDown();
        return true;
    }

    @Override
    public boolean wakeup(ExecuteContext abstractExecutor) {
        // Empty Implementation
        logger.trace("调用无具体实现的方法:AbstractExecutor.wakeup(AbstractExecutor)");
        return true;
    }

    /**
     * 获得锁
     *
     * @return -
     */
    public RCountDownLatch getLock() {
        return this.runningCondition;
    }

    /**
     * 获得条件锁
     *
     * @return -
     */
    public RCountDownLatch getRunningCondition() {
        return this.runningCondition;
    }


    // ----------------------------------------------------------------------------- 锁处理 结束

    /**
     * 判断是否跳过执行
     *
     * @return 如果是跳过，那么返回值为true，否则返回值为false
     */
    public boolean isSkipped() {
        return isSkipped;
    }

    /**
     * 设置当前执行器的跳过状态
     *
     * @param skipped 如果跳过，设置为true，否则设置为false
     */
    public void setSkipped(boolean skipped) {
        isSkipped = skipped;
    }

    /**
     * 获得当前执行器的执行ID
     *
     * @return 当前执行器的执行ID
     */
    public String getExecuteId() {
        return executeId;
    }

    /**
     * 获得当前执行器的父执行ID
     *
     * @return 当前执行器的父执行ID
     */
    public String getParentExecuteId() {
        return parentExecuteId;
    }

    /**
     * 获得当前执行器所在的作业
     *
     * @return 当前执行器所在的作业
     */
    protected Job getCurrentJob() {
        IEntry jobEntry = missionScheduleCenter.getExecutorById(parentExecuteId);
        return jobEntry == null ? null : (Job) jobEntry;
    }

    /**
     * 获得当前节点所在作业的作业id
     *
     * @return 如果获取失败，返回值为null，否则返回作业id
     */
    protected String getCurrentJobId() {
        Job currentJob = getCurrentJob();
        if (currentJob != null) {
            return currentJob.getJobId();
        } else {
            return null;
        }
    }

    /**
     * 获得作业执行器
     *
     * @return 返回当前作业执行器
     */
    protected IEntry getJobExecutor() {
        return missionScheduleCenter.getExecutorById(parentExecuteId);
    }

    /**
     * 设置当前执行器的父执行ID
     *
     * @param parentExecuteId 父执行ID
     */
    public void setParentExecuteId(String parentExecuteId) throws ExecuteException {
        // 在设置当前执行器的成员变量外，在上下文信息中同时加入
        this.parentExecuteId = parentExecuteId;
        this.context.addAttribute(ATTR_EXECUTE_PARENT_ID, parentExecuteId);
        IEntry jobEntry = missionScheduleCenter.getExecutorById(parentExecuteId);
        if (jobEntry != null) {
            this.jobId = ((Job) jobEntry).getJobId();
            this.systemVariables.put(SYS_VAR_KEY_CURRENT_JOB_ID, this.jobId);
        } else if (isJob) {
            this.jobId = executeId;
        }
    }

    /**
     * 设置当前执行作业对应的资源信息
     *
     * @param resource 节点资源信息
     */
    public void setNodeResource(NodeResource resource) {
        this.nodeResource = resource;
    }

    /**
     * 获得当前执行器绑定的节点元素
     *
     * @return 当前执行器绑定的节点元素
     */
    public IEntry getEntry() {
        return entry;
    }

    /**
     * 通过键表达式获取通用属性中的配置
     * <p>
     * 该方式获取属性值的使用场景通常在直取值的情况，不建议频繁使用该方式获取值
     * <p>
     * 由于使用的是字符键表达式，在更改对应的实体后，会出现没有对应更改导致的数据不正确，但是比较适用于另外一种场景：如果
     * 在执行器Executor执行过程中，支持的配置数据简单，那么可以直接将对应的值设置为common中的属性，而无需在Executor中新定
     * 义属性来存储配置值
     *
     * @param keyExpr 键表达式
     * @return 获取键表达式对应的属性值
     */
    protected Object getCommonAttr(String keyExpr) {
        ACommon common = ((Entry) this.entry).getCommon();
        return common != null ? common.getAttr(keyExpr, false) : null;
    }

    /**
     * 绑定当前执行器的执行元素
     *
     * @param entry 当前执行器绑定的执行元素
     */
    public void setEntry(IEntry entry) throws ExecuteException {
        this.entry = entry;

        // 在设置当前执行器的执行元素时，将执行元素上的入参作为当前节点执行器的上下文
        InputParam inputParam = entry.getInputParam();
        if (inputParam != null) {
            for (MetaData metaData : entry.getInputParam().getParams()) {
                this.addInputParam(metaData);
            }
        }
        this.context.addAttribute(ATTR_ENTRY_TYPE, entry.getNodeType());
        this.context.addAttribute(ATTR_ENTRY_EXEC_CLZ, entry.getExecutorClzName());
        this.status.setEntry(entry);
    }

    /**
     * 获得节点资源配置
     *
     * @return 节点资源
     */
    public NodeResource getNodeResource() {
        return this.nodeResource;
    }

    /**
     * 校验执行器执行的合法性
     *
     * @return 校验执行器的合法性，如果校验通过，那么返回值为true，否则返回值为false
     */
    public abstract boolean validate();

    /**
     * 得到停止的命令后，断开前后的关联关系
     */
    protected abstract boolean onDisconnect();

    protected abstract void onArrange(UpdateAnalysis analysis);

    /**
     * 內建的接收到作业更新包的处理
     *
     * @param analysis 作业更新包
     */
    private void arrangement(UpdateAnalysis analysis) {
        RLock lock = distToolkit.getLock(EurekaSystemRedisKey.EUREKA$LOCKER_UPDATE_ANALYSIS, jobId);
        lock.lock(10, TimeUnit.MINUTES);
        System.err.println(entry.getNodeId() + "\t上锁处理中...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (StringUtil.isEmpty(jobId)) {

        }
        //


        // TODO:由子类实现之前的调用
        onArrange(analysis);


        // TODO:由子类实现之后的调用
        // 1、如果更新包的删除列表中包含当前节点，那么对当前节点下达停止指令，否则标记当前节点已经更新就绪

        lock.unlock();
    }

    /**
     * 执行器内部事件订阅监听器
     *
     * @param <T>
     */
    class ExecutorPubSubListener<T> implements RedisPubSubListener<T> {

        @Override
        public boolean onStatus(PubSubType type, String channel) {
            return true;
        }

        @Override
        public void onPatternMessage(String pattern, String channel, T message) {

        }

        @Override
        public void onMessage(String channel, T msg) {
            // 过滤选择内部时间类型，如果是重新编排作业，那么进行事件回调，由对应的实现进行处理
            if (channel.contains(Event.PUBLISH_JOB_REARRANGEMENT.channel())) {
                try {
                    UpdateAnalysis analysis = EXmlParser.fromXml((String) msg, UpdateAnalysis.class);
                    arrangement(analysis);
                } catch (XmlParserException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 打印执行器基本信息
     *
     * @return 执行器基本信息
     */
    public String printExecStatus() {
        String nodeInfo = "\n-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·" +
                "\n↓线程：" + Thread.currentThread().getName() + "\tID：" + Thread.currentThread().getId() +
                "\n↓节点ID：" + this.entry.getNodeId() + "\t\t节点类型：" + entry.getNodeType() + "\t\t归属作业：" + parentExecuteId +
                "\n↓下一个节点数：(" + context.getAttribute(ATTR_NEXT_ENTRY_SIZE) + ")：" + context.getAttribute(ATTR_NEXT_ENTRY_INFO) +
                "\n↓开始时间：" + DateUtil.format(new Date(), DateUtil.SDFYYYYMMDDHHmmSS) +
                "\n-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·\n";
        return AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, nodeInfo, AnsiColor.DEFAULT);
    }

    /**
     * 记录节点运行日志
     * <p>
     * 日志的输出结构为标准Json，日志数据参数以一维数组的方式入参，奇数下标为日志输出的属性key，
     * 偶数下标为日志输出key对应的数据，允许Object类型
     *
     * @param logEntries 需要输出的日志数据
     */
    protected void EXECUTOR_LOGGER(Object... logEntries) {
        EXECUTOR_LOGGER(false, logEntries);
    }

    protected void EXECUTOR_LOGGER_DEBUG(Object... logEntries) {
        EXECUTOR_LOGGER(true, logEntries);
    }

    private void EXECUTOR_LOGGER(boolean isDebug, Object... logEntries) {
        try {
            if (logEntries.length == 0) {
                return;
            }
            Map<String, Object> logData = new HashMap<String, Object>();
            if (logEntries.length % 2 != 0) {
                logData.put("msg", logEntries[0]);
            } else {
                String key = null;
                for (int index = 0; index < logEntries.length; index++) {
                    if (index % 2 == 0) {
                        key = String.valueOf(logEntries[index]);
                    } else {
                        logData.put(key, logEntries[index]);
                    }
                }
            }
            String logStr = JSON.toJSONString(logData);
            if (isDebug) {
                logger.debug(AnsiOutput.toString(AnsiColor.MAGENTA,
                        logStr, AnsiColor.DEFAULT));
            } else {
                logger.trace(AnsiOutput.toString(AnsiColor.MAGENTA,
                        logStr, AnsiColor.DEFAULT));
            }
            executorLogger.write("[" + DateUtil.format(new Date(), DateUtil.SDFYYYYMMDDHHmmSS) + "]\t" + logStr);
            executorLogger.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自带基础表达式解析
     *
     * @param context 上下文内容
     * @param content 解析内容
     * @return 解析后字符串
     * @throws Exception -
     */
    public String scriptParse(Map<String, Object> context, String content) throws Exception {
        if (this.script == null) {
            this.script = new Script();
        }
        return this.script.parseScript(context, content);
    }

    /**
     * 检查当前的状态位是否是暂停状态
     */
    protected synchronized void statusChecking() throws ExecuteException {
        Status currentStatus = Status.value(this.status.getStatusCode());
        if (Status.ENTRY_PAUSING.equals(currentStatus)) {
            try {
                // 暂停，由子类重写暂停需要保存的状态信息
                this.onPause();
                changeExecutingStatus(Status.ENTRY_PAUSED);
                this.controlCondition.await();

                // 从暂停中恢复，由子类中重写暂停需要恢复的状态信息
                this.onResume();
                changeExecutingStatus(Status.RUNNING);
                // 重新设置上
                this.controlCondition.trySetCount(1);
            } catch (InterruptedException | ExecuteException e) {
                e.printStackTrace();
            }
        } else if (Status.ENTRY_STOPPING.equals(currentStatus)) {
            // TODO:只有作业节点能够被停止
            // 停止一个作业的时候，需要对所有的子任务下达停止事件

            // 停止节点，由子类实现相关状态的保存
            onStop();

            changeExecutingStatus(Status.ENTRY_STOPPED);
            // 停止执行完成后，退出当前线程
            Thread.currentThread().interrupt();
        }
    }

    protected synchronized void stopByStatus() {
        if (Status.RUNNING.equals(Status.value(this.status.getStatusCode()))) {
            // TODO：如果是运行状态，允许进行停止
        }
    }

    /**
     * 线程暂停
     *
     * @throws ExecuteException 线程暂停执行异常
     */
    @Override
    public void onPause() throws ExecuteException {
        // TODO：线程暂停
        System.out.println("任务[" + executeId + "]暂停---------------->>>");
    }

    /**
     * 继续执行
     *
     * @throws ExecuteException 执行异常
     */
    @Override
    public void onResume() throws ExecuteException {
        // TODO：线程恢复
        System.err.println("任务恢复[" + executeId + "]---------------->>>");
    }

    /**
     * 任务停止
     *
     * @throws ExecuteException 线程停止执行异常
     */
    @Override
    public void onStop() throws ExecuteException {
        // TODO:线程停止（不可恢复）
        EXECUTOR_LOGGER_DEBUG("msg", "Mission On Stopping [" + executeId + "]---------------->>>");
        if (this.onDisconnect()) {
            EXECUTOR_LOGGER_DEBUG("msg", "Disconnect precursors and successors [successful].");
            // TODO:前驱后继的关联关系已经解除，停止当前线程
        } else {
            EXECUTOR_LOGGER_DEBUG("msg", "Disconnect precursors and successors [failure].");
        }
    }

    /**
     * 执行器唤醒
     */
    @Override
    public void onWakeup(ExecuteContext fromContext) {
        // Empty Implementation
        logger.warn("调用无具体实现的方法:AbstractExecutor.onWakeup");
    }

    /**
     * 绑定作业执行监听器
     *
     * @param listener 作业执行监听器
     */
    public void bindingExecListener(IExecutorListener listener) {
        this.executorListener = listener;
    }

    /**
     * 清除分布式运行缓存
     */
    protected void clearCurrentNodeCache() {
        this.clearDistCache(this.executeId);
    }

    /**
     * 清除当前节点的运行时缓存
     */
    protected void clearCache() {
        recursiveCacheClear(this.entry);
    }

    /**
     * 递归清除分布式运行时缓存
     *
     * @param entry 需要递归清楚运行状态的节点
     */
    private void recursiveCacheClear(IEntry entry) {
        List<IEntry> entries = null;
        entries = getCurrentJob().getEntries(entry.obtainPrecursors());

        if (entries == null)
            return;
        for (IEntry e : entries) {
            Class<? extends AbstractExecutor> executorClz = e.getExecutorClz();
            if (executorClz == null) {
                continue;
            }
            boolean haveMoreLastInDegree = e.getInDegree().size() != 0;
            if (!AbstractBlockingExecutor.class.isAssignableFrom(executorClz)
                    && !AbstractStreamNodeExecutor.class.isAssignableFrom(executorClz)
                    && (!(AbstractJobExecutor.class.isAssignableFrom(executorClz) && ((Job) e).getIsObstructive()))
                    && haveMoreLastInDegree) {
                recursiveCacheClear(e);
                clearDistCache(e.getNodeId());
            }
        }
    }

    /**
     * 清除分布式系统锁对象实例以及各种条件、队列数据
     */
    protected void clearDistCache(String executeId) {
        RCountDownLatch countDownLatch = this.distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CDL, executeId);
        if (countDownLatch != null) {
            countDownLatch.delete();
        }

        countDownLatch = this.distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CONDITION, executeId);
        if (countDownLatch != null) {
            countDownLatch.delete();
        }

        countDownLatch = this.distToolkit.getCountDownLatch(EurekaSystemRedisKey.EUREKA$SYSTEM_CTL_CONDITION, executeId);
        if (countDownLatch != null) {
            countDownLatch.delete();
        }

        RBucket<?> rBucket = this.distToolkit.getRBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_TOP_CONTEXT, executeId);
        if (rBucket != null) {
            rBucket.delete();
        }

        rBucket = this.distToolkit.getRBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_FLUX_CONTEXT, executeId);
        if (rBucket != null) {
            rBucket.delete();
        }

        rBucket = this.distToolkit.getRBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_STATUS, executeId);
        if (rBucket != null) {
            rBucket.delete();
        }

        RMap map = this.distToolkit.getMap(EurekaSystemRedisKey.EUREKA$SYSTEM_CONTEXT_ATTR, executeId);
        if (map != null) {
            map.delete();
        }

        rBucket = this.distToolkit.getRBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_IN_PARAM, executeId);
        if (rBucket != null) {
            rBucket.delete();
        }

        rBucket = this.distToolkit.getRBucket(EurekaSystemRedisKey.EUREKA$SYSTEM_OUT_PARAM, executeId);
        if (rBucket != null) {
            rBucket.delete();
        }

        map = this.distToolkit.getMap(EurekaSystemRedisKey.EUREKA$GLOBAL_CTX_CACHE, 0);
        map.remove(executeId);
    }
}
