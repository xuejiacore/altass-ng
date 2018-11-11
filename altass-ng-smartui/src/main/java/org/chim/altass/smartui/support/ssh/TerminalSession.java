/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.terminal
 * Author: Xuejia
 * Date Time: 2016/10/11 14:54
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.support.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class Name: TerminalSession
 * Create Date: 2016/10/11 14:54
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 终端会话，一个终端会话代表一个终端操作的窗口
 */
public class TerminalSession {

    private SessionConfig sessionCfg = null;                    // 会话配置
    private SSHTerminal sshTerminal = null;                     // ssh连接交互器

    /**
     * 终端会话的相关属性配置
     */
    public class SessionConfig {
        private int sessionId = -1;                             // 会话id
        private String sessionAlias = null;                     // 会话别名
        private long sessionTimeOut = 1800000;                  // 默认的会话超时时间为1800s

        private String host = null;
        private int port = 22;
        private String userName = null;
        private String pwd = null;
    }

    public static void main(String[] args) throws JSchException {
        TerminalSession terminalSession = new TerminalSession("115.28.40.152", "root", "ikeTech&0528");
        terminalSession.bindingInteraction(new SSHTerminal.TerminalListener() {
            @Override
            public void onStreamShow(String buffer) {
                System.err.print(buffer);
            }

            @Override
            public void onBufferShow(String buffer) {
                System.err.print(buffer);
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        terminalSession.cmdRunner("ls");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        terminalSession.cmdRunner("ls");
    }

    /**
     * 初始化一个ssh终端会话
     *
     * @param host     主机地址
     * @param userName 用户名
     * @param pwd      用户密码
     * @throws JSchException
     */
    public TerminalSession(String host, String userName, String pwd) throws JSchException {
        this.sessionCfg = new SessionConfig();
        this.sessionCfg.host = host;
        this.sessionCfg.userName = userName;
        this.sessionCfg.pwd = pwd;
        this.sshTerminal = new SSHTerminal();
        this.sshTerminal.confSession(sessionCfg.host, sessionCfg.userName, sessionCfg.pwd)
                .configSession(SessionOptional.K_STRICT_HOST_KEY_CHECKING, SessionOptional.V_DEFAULT_STRICT_HOST_KEY_CHECKING.toString())
                .connectSession();
    }

    public TerminalSession(String host, String userName, UserInfo userInfo) throws JSchException {
        this.sessionCfg = new SessionConfig();
        this.sessionCfg.host = host;
        this.sessionCfg.userName = userName;
        this.sessionCfg.pwd = userInfo.getPassword();
        this.sshTerminal = new SSHTerminal();
        this.sshTerminal.confSession(sessionCfg.host, sessionCfg.userName, sessionCfg.pwd)
                .configSession(SessionOptional.K_STRICT_HOST_KEY_CHECKING, SessionOptional.V_DEFAULT_STRICT_HOST_KEY_CHECKING.toString())
                .connectSession();
    }

    /**
     * 初始化一个ssh 终端会话
     *
     * @param config 会话的配置
     */
    public TerminalSession(SessionConfig config) throws JSchException {
        this.sessionCfg = config;
        this.sshTerminal = new SSHTerminal();
        this.sshTerminal.confSession(sessionCfg.host, sessionCfg.userName, sessionCfg.pwd)
                .configSession(SessionOptional.K_STRICT_HOST_KEY_CHECKING, SessionOptional.V_DEFAULT_STRICT_HOST_KEY_CHECKING.toString())
                .connectSession();
    }

    /**
     * 命令执行器
     *
     * @param cmd 需要执行的单个命令
     */
    public boolean cmdRunner(String cmd) {
        return cmdRunner(new String[]{cmd});
    }

    /**
     * 命令执行器
     *
     * @param commands 需要执行的命令集合
     * @return 如果命令执行成功，那么返回值为true，否则返回值为false
     */
    public boolean cmdRunner(String[] commands) {
        // 命令类型识别器
        List<ChannelType> channelTypes = new ArrayList<>();
        for (String command : commands) {
            channelTypes.add(checkCmdExecType(command));
        }

        int index = 0;
        String typeStr;
        for (ChannelType channelType : channelTypes) {
            typeStr = channelType.toString();
            if (typeStr.equals(ChannelType.SHELL.toString())) {
                this.sshTerminal.runShell(new String[]{commands[index++]});
            } else if (typeStr.equals(ChannelType.EXEC.toString())) {
                this.sshTerminal.execCmd(commands[index++]);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前需要执行的命令类型
     *
     * @param cmd 需要判断的命令
     * @return 返回对应的执行类型
     */
    private ChannelType checkCmdExecType(String cmd) {
        StringTokenizer tokenizer = new StringTokenizer(cmd, ";");
        while (tokenizer.hasMoreTokens()) {
            String c = tokenizer.nextToken();
            if (c.contains("tail")) {
                return ChannelType.EXEC;
            }
        }
        return ChannelType.SHELL;
    }

    /**
     * 绑定会话交互输出
     *
     * @param listener 绑定的输出监听器
     */
    public void bindingInteraction(SSHTerminal.TerminalListener listener) {
        this.sshTerminal.bindingTerminalListener(listener);
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        this.sshTerminal.disconnect();
    }
}
