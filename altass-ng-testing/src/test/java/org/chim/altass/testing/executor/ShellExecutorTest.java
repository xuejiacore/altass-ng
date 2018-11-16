package org.chim.altass.testing.executor;

import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.attr.ASSH;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.executor.ShellExecutor;
import org.chim.altass.core.executor.debug.DebugConfig;
import org.chim.altass.core.executor.debug.DebugExecutor;
import org.chim.altass.testing.base.AbstractTesting;
import org.junit.Test;

/**
 * Class Name: HttpExecutorTest
 * Create Date: 11/2/18 9:08 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ShellExecutorTest extends AbstractTesting {

    @Test
    public void shellBasic() throws InterruptedException {
        execute("shellBasic");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry start, Entry end) throws FlowDescException {
        if ("shellBasic".equalsIgnoreCase(selector)) {
            Entry startNode = job.getStartNode();
            Entry endNode = job.getEndNode();
            Entry paramReducer = new Entry();
            paramReducer.setNodeId("DEBUG-SHELL1");
            // 测试获取shell节点的输出参数，添加到参数表达式中进行解析
            paramReducer.addJsonArg(DebugConfig.class, "{\"name\":\"${foo_param}\", \"outputJson\": \"{\\\"outputK\\\":\\\"outputV\\\"}\"}");
            paramReducer.setExecutorClz(DebugExecutor.class);
            job.addEntry(paramReducer);

            Entry shellEntry = new Entry();
            shellEntry.setExecutorClz(ShellExecutor.class);
            shellEntry.addJsonArg(ASSH.class, "{" +
                    "\"host\":{\"host\":\"127.0.0.1\", \"port\": 22, \"user\":\"eureka\", \"password\":\"eureka!@#\"}," +
                    "\"command\":\"cd /home/eureka\nmkdir test2\ncd test2\ntouch ${outputK}.sh\nchmod a+x ${outputK}.sh\"," +
                    "\"expectResult\":\"0\"" +
                    "}");
            job.addEntry(shellEntry);

            Entry debugEntry = new Entry();
            debugEntry.setNodeId("DEBUG-SHELL2");
            // 测试获取shell节点的输出参数，添加到参数表达式中进行解析
            debugEntry.addJsonArg(DebugConfig.class, "{\"name\":\"${foo_param}\"}");
            debugEntry.setExecutorClz(DebugExecutor.class);
            job.addEntry(debugEntry);

            job.connect(startNode, paramReducer);
            job.connect(paramReducer, shellEntry);
            job.connect(shellEntry, debugEntry);
            job.connect(debugEntry, endNode);
        }
    }
}
