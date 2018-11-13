package org.chim.altass.core.executor.io;

import com.jcraft.jsch.JSchException;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.domain.buildin.attr.AHost;
import org.chim.altass.core.domain.buildin.attr.ASSH;
import org.chim.altass.core.executor.config.ColumnConfig;
import org.chim.altass.core.domain.buildin.attr.CommonStreamConfig;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractPipelineExecutor;
import org.chim.altass.toolkit.job.UpdateAnalysis;
import org.chim.altass.toolkit.ssh.SSHTerminal;
import org.chim.altass.toolkit.ssh.SessionOptional;


import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: TaildirStreamExecutor
 * Create Date: 5/13/18 1:56 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * Ability:
 * - Streaming
 * - Distribution
 * <p>
 * Use tail command to collect data and push to suffix.
 */
@Executable(name = "fileInputStreamExecutor", assemble = true, ability = {ExecutorAbility.ABILITY_STREAMING, ExecutorAbility.ABILITY_DISTRIBUTION})
@Resource(name = "Tail流", clazz = TaildirStreamExecutor.class, groupName = "常规", midImage = "res/images/node/flume_bg.png",
        pageUrl = "nodeConfigs/core/tailInputStreamNodeConfig.jsp")
public class TaildirStreamExecutor extends AbstractPipelineExecutor implements SSHTerminal.TerminalListener {

    @RuntimeAutowired
    private ASSH ssh = null;

    private SSHTerminal sshTerminal = null;

    // Column split rule configuration
    @RuntimeAutowired
    private ColumnConfig columnConfig = null;

    // Basic common stream configuration
    @RuntimeAutowired
    private CommonStreamConfig commonStreamConfig = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public TaildirStreamExecutor(String executeId) throws ExecuteException {
        super(executeId);
        sshTerminal = new SSHTerminal();
    }

    @Override
    protected void dataSource() throws ExecuteException {
        sshTerminal.execCmd("tail -f " + ssh.getCommand());
        try {
            Thread.sleep(1000);
            sshTerminal.sendSignal("SIGINT");
        } catch (Exception e) {
            throw new ExecuteException(e);
        }
    }

    @Override
    protected boolean onPipelineInit() throws ExecuteException {

        AHost host = ssh.getHost();

        try {
            sshTerminal.confSession(host.getHost(), host.getPort(), host.getUser(), host.getPassword())
                    .configSession(SessionOptional.K_STRICT_HOST_KEY_CHECKING,
                            SessionOptional.V_DEFAULT_STRICT_HOST_KEY_CHECKING.toString())
                    .connectSession();
        } catch (JSchException e) {
            throw new ExecuteException(e);
        }

        sshTerminal.bindingTerminalListener(this);
        return true;
    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }

    @Override
    public void rollback(StreamData data) {

    }

    public void setASSH(ASSH ssh) {
        this.ssh = ssh;
    }

    @Override
    public void onStreamShow(String line) {
        boolean dataDivisible = commonStreamConfig.getDataDivisible();
        Map<Integer, String> headerMap = null;
        try {
            Map<String, Object> rowDataMap = new HashMap<String, Object>();
            // Target data that will be push with pipeline streaming which could be a row data, json.
            // More data structure will be support in the future.
            Object targetData;

            if (line != null) {
                EXECUTOR_LOGGER("msg", "Input Stream Data", "rowData", line);
                // Structured data will be assemble when it was configured divisible and map first line.
                if (dataDivisible) {
                    // A structure data will be push that had split row data and mapped to header.
                    String[] columnData = line.split(commonStreamConfig.getTextSeparator());
                    for (int columnIdx = 0; columnIdx < columnData.length; columnIdx++) {
                        rowDataMap.put(String.valueOf(columnIdx), columnData[columnIdx]);
                    }
                    targetData = rowDataMap;
                } else {
                    // Simple row data will be push.
                    targetData = line.trim();
                }

                // execute data pushing
                this.pushData(new StreamData(this.executeId, null, targetData));
            }
        } catch (ExecuteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferShow(String buffer) {

    }

    // To be autowired
    public void setCommonStreamConfig(CommonStreamConfig commonStreamConfig) {
        this.commonStreamConfig = commonStreamConfig;
    }

    // To be autowired
    public void setColumnConfig(ColumnConfig columnConfig) {
        this.columnConfig = columnConfig;
    }
}
