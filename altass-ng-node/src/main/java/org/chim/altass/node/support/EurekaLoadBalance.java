package org.chim.altass.node.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.chim.altass.core.constant.SystemEnvMonitor;
import org.chim.altass.core.domain.buildin.attr.APriority;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.toolkit.JDFWrapper;
import org.chim.altass.toolkit.JedisToolkit;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Class Name: EurekaLoadBalance
 * Create Date: 18-3-6 下午7:42
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class EurekaLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "eureka";

    private final Random random = new Random();

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // 支持 AltassNode 负载以及节点选择策略，在节点定义中，配置制定优先级
        Object[] arguments = invocation.getArguments();
        APriority priority = null;

        // 迭代参数，如果是作业描述参数JDF，那么对参数进行恢复后，获得对应的优先级配置，如果有就跳出
        for (Object argument : arguments) {
            if (argument instanceof JDFWrapper) {
                Entry entry = ((JDFWrapper) argument).restore();
                priority = entry.getPriority();
                if (priority != null) {
                    break;
                }
            }
        }

        Set<Tuple> range = JedisToolkit.getInstance().zrange(SystemEnvMonitor.MONICA$SYS_ENV_PROCESS_MEMORY_USED, 0, 0, 100);
        for (Invoker<T> invoker : invokers) {
            Map<String, String> parameters = invoker.getUrl().getParameters();
            String serviceId = parameters.get("application");
            // 获得所有的提供者列表
        }

        if (priority != null) {
            // 已经配置指明了运行的策略

            // TODO:此处进行节点选择的优先级配置
            return getRandomInvoker(invokers, invocation);
        } else {

            // 没有配置指明策略，那么默认会综合cpu和内存使用量选择较低的节点
            return getRandomInvoker(invokers, invocation);
        }
    }

    private <T> Invoker<T> getDefaultInvoker(List<Invoker<T>> invokers, Invocation invocation) {
        // 获取分布式redis缓存中的实时 TopN CPU 以及 内存，选取最优的节点运行，如果

        return null;
    }

    private <T> Invoker<T> getRandomInvoker(List<Invoker<T>> invokers, Invocation invocation) {
        int length = invokers.size(); // Number of invokers
        int totalWeight = 0; // The sum of weights
        boolean sameWeight = true; // Every invoker has the same weight?
        for (int i = 0; i < length; i++) {
            int weight = getWeight(invokers.get(i), invocation);
            totalWeight += weight; // Sum
            if (sameWeight && i > 0
                    && weight != getWeight(invokers.get(i - 1), invocation)) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // If (not every invoker has the same weight & at least one invoker's weight>0), select randomly based on totalWeight.
            int offset = random.nextInt(totalWeight);
            // Return a invoker based on the random value.
            for (Invoker<T> invoker : invokers) {
                offset -= getWeight(invoker, invocation);
                if (offset < 0) {
                    return invoker;
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return invokers.get(random.nextInt(length));
    }
}
