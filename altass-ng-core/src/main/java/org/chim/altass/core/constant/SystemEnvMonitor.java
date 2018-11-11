package org.chim.altass.core.constant;


import org.chim.altass.base.cache.BasicRedisKey;

/**
 * Class Name: TestBasicKey
 * Create Date: 18-3-9 下午4:18
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public enum SystemEnvMonitor implements BasicRedisKey {

    MONICA$SYS_ENV_MACHINE_CPU_RATE(0L, "MACHINE_CPU_RATE"),
    MONICA$SYS_ENV_MACHINE_MEM_TOTAL(0L, "MACHINE_MEM_TOTAL"),
    MONICA$SYS_ENV_MACHINE_MEM_FREE(0L, "MACHINE_MEM_FREE"),
    MONICA$SYS_ENV_MACHINE_MEM_USED(0L, "MACHINE_MEM_USED"),
    MONICA$SYS_ENV_MACHINE_MEM_BUFF_CACHE(0L, "MACHINE_MEM_BUFF_CACHE"),
    MONICA$SYS_ENV_MACHINE_LA1(0L, "MACHINE_LA1"),
    MONICA$SYS_ENV_MACHINE_LA2(0L, "MACHINE_LA2"),
    MONICA$SYS_ENV_MACHINE_LA3(0L, "MACHINE_LA3"),
    MONICA$SYS_ENV_PROCESS_CPU_RATE(0L, "PROCESS_CPU_RATE"),
    MONICA$SYS_ENV_PROCESS_MEMORY_USED(0L, "PROCESS_MEMORY_USED"),
    MONICA$SYS_ENV_PROCESS_MEMORY_PERCENT(0L, "PROCESS_MEMORY_PERCENT"),
    MONICA$SYS_ENV_PROCESS_THREAD_CNT(0L, "PROCESS_THREAD_CNT");

    private Long ttl = null;
    private String desc = null;

    SystemEnvMonitor(Long ttl, String desc) {
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
