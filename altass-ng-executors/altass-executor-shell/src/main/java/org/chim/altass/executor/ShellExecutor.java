package org.chim.altass.executor;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import org.chim.altass.core.annotation.RuntimeAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.domain.buildin.attr.AHost;
import org.chim.altass.core.domain.buildin.attr.ASSH;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractNodeExecutor;
import org.chim.altass.core.executor.RestoreContext;
import org.chim.altass.executor.shell.bean.EurekaOutput;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Name: ShellAbstractNodeExecutor
 * Create Date: 2017/9/6 0:36
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * SSH 操作节点
 * <p>
 * 该节点允许远程执行Linux命令
 * <p>
 * 设置执行SSH命令的主机Host及用户名密码，配置需要执行的Shell内容，允许执行所有的Linux允许的命令
 * <p>
 * 脚本的执行返回值必须遵照如下方式进行：
 * <p>
 * 1、最后脚本执行状态作为当前节点的退出状态，Exist Status = 0 时，节点操作成功，否则失败；
 * 2、节点的最后一行（单行）Json数据作为当前Shell节点接收的输出参数，作为节点操作完成后的输出；
 */
@Executable(name = "shellExecutor", assemble = true)
@Resource(name = "Shell", clazz = ShellExecutor.class, midImage = "res/images/executor/shell_bg.png", pageUrl = "nodeConfigs/ext/shellNodeConfig.jsp")
public class ShellExecutor extends AbstractNodeExecutor {

    /**
     * SSH 操作指令
     */
    @RuntimeAutowired
    private ASSH ssh = null;

    /**
     * 输入参数，用于解析 Shell 命令中变量占位符
     */
    private Map<String, Object> inputParseParams = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public ShellExecutor(String executeId) throws ExecuteException {
        super(executeId);
        this.inputParseParams = new HashMap<String, Object>();
    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }

    /**
     * 执行器初始化
     *
     * @return 如果初始化成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException -
     */
    @Override
    public boolean onInit() throws ExecuteException {
        // 获得输入参数，该输入参数包含了从上一个节点的输出
        InputParam inputParam = ((Entry) entry).getInputParam();
        if (inputParam == null) {
            return true;
        }
        List<MetaData> params = inputParam.getParams();

        String field;
        for (MetaData param : params) {
            field = param.getField();
            this.inputParseParams.put(field.replace("$$", ""), param.getValue());
        }
        return true;
    }

    @Override
    public RestoreContext onRestore() throws ExecuteException {
        return null;
    }

    /**
     * 脚本执行器执行脚本
     *
     * @return 如果执行成功，那么返回值为true，否则返回值为false
     * @throws ExecuteException -
     */
    @Override
    public boolean onNodeProcessing() throws ExecuteException {
        AHost host = null;
        if (this.ssh != null) {
            host = this.ssh.getHost();
        }

        if (host != null) {
            String executeCmd = ssh.getCommand();
            if (this.inputParseParams != null && this.inputParseParams.size() > 0) {
                try {
                    // 解析动态参数
                    executeCmd = this.script.parseScript(this.inputParseParams, executeCmd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            EurekaOutput returnJson = new EurekaOutput();
            String password = host.getPassword();
            host.setPassword(null);
            EXECUTOR_LOGGER("msg", "执行目标", "host", host);
            EXECUTOR_LOGGER("msg", "执行脚本", "shell", executeCmd + "");
            Integer executeResult = this.executeShCmd(host.getHost(), host.getPort(), host.getUser(), password, executeCmd, returnJson);
            EXECUTOR_LOGGER("msg", "脚本执行返回状态", "status", executeResult);
            if (executeResult != null && executeResult == 0) {
                // 获得输出参数
                Map<String, Object> returnMap = returnJson.getEurekaOutput();
                if (returnMap != null && returnMap.size() > 0) {
                    for (String key : returnMap.keySet()) {
                        addOutputParam(new MetaData(key, String.valueOf(returnMap.get(key))));
                    }
                }
                return true;

            } else {
                return false;
            }
        } else {
            throw new ExecuteException("无法找到Host信息");
        }
    }

    /**
     * 远程执行脚本命令
     *
     * @param host         主机Host
     * @param port         主机Port
     * @param user         登录用户
     * @param pass         登录密码
     * @param cmd          运行的 Shell 命令
     * @param eurekaOutput 输出的参数
     * @return 返回执行状态
     * @throws ExecuteException -
     */
    private Integer executeShCmd(String host, int port, String user, String pass, String cmd, EurekaOutput eurekaOutput) throws ExecuteException {
        // 创建连接
        Connection connection = new Connection(host, port);
        Session session = null;
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(user, pass);
            if (!isAuthenticated) {
                EXECUTOR_LOGGER("SSH 授权失败，检查连接Host、用户名、密码及端口");
                // ssh授权失败
                throw new ExecuteException("SSH 授权失败，检查连接Host、用户名、密码及端口");
            } else {
                session = connection.openSession();
                session.execCommand(cmd);
                StreamGobbler stdoutSG = new StreamGobbler(session.getStdout());
                BufferedReader stdout = new BufferedReader(new InputStreamReader(stdoutSG));
                StreamGobbler stderrSG = new StreamGobbler(session.getStderr());
                BufferedReader stderr = new BufferedReader(new InputStreamReader(stderrSG));

                String lastLine = null;
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = stdout.readLine()) != null) {
                    lastLine = line;
                }
                if (lastLine != null) {
                    eurekaOutput.parseJsonResult(lastLine);
                    EXECUTOR_LOGGER("msg", "Shell执行返回值", "eurekaOutput", eurekaOutput);
                }
            }

            return session.getExitStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (session != null) {
            session.close();
        }
        connection.close();
        return 1;
    }

    @Override
    public void onPause() throws ExecuteException {

    }

    public void setASSH(ASSH ssh) {
        this.ssh = ssh;
    }
}
