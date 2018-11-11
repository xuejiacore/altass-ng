package org.chim.altass.core.constant;

/**
 * Class Name: ExecutorAttr
 * Create Date: 11/2/18 12:46 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ExecutorAttr {
    // 常规属性键定义开始:内建的属性占用数位：[0:15]，扩展的属性，需要使用[16:]以后的键值，否则会抛出异常
    public static final Integer ATTR_EXECUTE_ID = 0x1;                          // 当前节点的执行ID
    public static final Integer ATTR_EXECUTE_PARENT_ID = 0x2;                   // 父节点的执行ID
    public static final Integer ATTR_IS_JOB = 0x3;                              // 是否是作业
    public static final Integer ATTR_NEXT_ENTRY_SIZE = 0x4;                     // 获得下一个元素的数量
    public static final Integer ATTR_NEXT_ENTRY_INFO = 0x5;                     // 获得下一个元素的信息
    public static final Integer ATTR_ENTRY_TYPE = 0x6;                          // 当前节点的节点类型
    public static final Integer ATTR_ENTRY_EXEC_CLZ = 0x7;                      // 当前节点对应的执行器类
    public static final Integer ATTR_SYSTEM_VARIABLES = 0x8;                    // 系统参数
    public static final Integer ATTR_EXE_COST_TIME = 0x9;                       // 执行时间
    public static final Integer ATTR_EXE_BEGIN_TIME = 0xa;                      // 执行开始时间
    public static final Integer ATTR_EXE_END_TIME = 0xb;                        // 执行结束时间
    public static final Integer ATTR_THREAD_ID = 0xc;                           // 线程ID
    public static final Integer ATTR_IS_CHILD_JOB = 0xd;                        // 是否是子作业
    // 常规属性键定义结束

    // 系统参数system variables内建的键名称开始
    public static final String KEY_JOB_SYS_VAR = "job.system.variables";        // 作业系统参数键，能够获取作业的系统参数

    public static final String SYS_VAR_KEY_AUTHOR = "sys_var_key_author";    // 作者
    public static final String SYS_VAR_KEY_CURRENT_JOB_ID = "sys_var_key_current_job_id";        // 当前作业id
    public static final String SYS_VAR_KEY_CURRENT_DATE = "sys_var_key_current_date";            // 当前时间

    // 系统参数system variables内建的键名称结束
}
