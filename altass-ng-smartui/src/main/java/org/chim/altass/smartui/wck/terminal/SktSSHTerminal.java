/**
 * Project: x-framework
 * Package Name: org.ike.monitor.websockets.terminal
 * Author: Xuejia
 * Date Time: 2016/10/10 13:00
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.wck.terminal;

import com.jcraft.jsch.JSchException;
import org.chim.altass.smartui.support.ssh.SSHTerminal;
import org.chim.altass.smartui.support.ssh.TerminalSession;
import org.chim.altass.smartui.utils.MonitorProperties;
import org.chim.altass.smartui.wck.face.IWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class Name: SktSSHTerminal
 * Create Date: 2016/10/10 13:00
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: ssh连接处理器
 */
@ServerEndpoint("/sshbox")
public class SktSSHTerminal implements IWebSocket, SSHTerminal.TerminalListener {

    private final Logger log = LoggerFactory.getLogger(SktSSHTerminal.class);
    private static int onlineCount = 0;
    private static CopyOnWriteArrayList<SktSSHTerminal> webSockets =
            new CopyOnWriteArrayList<SktSSHTerminal>();
    private Session session = null;
    private TerminalSession terminalSession = null;

    private boolean isInit = true;
    private static final int FLOW_REQ_USER_NAME = 0;
    private static final int FLOW_REQ_USER_PWD = 1;
    private int currFlow = FLOW_REQ_USER_NAME;

    private String user = null;
    private String host = MonitorProperties.getValue("SSH_IP");
    private String pwd = null;

    private String currentCmd = null;

    /**
     * 用户打开一个终端连接
     * <p>
     * 流程规划：
     *
     * @param session 当前打开链接的session
     */
    @OnOpen
    @Override
    public void onOpen(Session session) {
        log.debug("Session On Opening.Current count:" + getOnlineCount());
        this.session = session;
        webSockets.add(this);
        addOnlineCount();
        System.out.println("连接建立成功：" + session.toString() + " 当前的在线人数为：" + getOnlineCount());
    }

    @OnClose
    @Override
    public void onClose() {
        webSockets.remove(this);
        subOnlineCount();
        terminalSession.disconnect();
        log.debug("Session On Closing.Current count:" + getOnlineCount());
    }

    @OnError
    @Override
    public void onError(Session session, Throwable err) {
        log.debug("On error");
        err.printStackTrace();
    }

    @OnMessage
    @Override
    public void onMessage(String message, Session session) {
        System.err.println(message);
        try {
            if (currFlow == FLOW_REQ_USER_NAME) {
                this.user = message;
                sendMessage(String.format("%s@%s's password:", this.user, this.host));
                currFlow++;
            } else if (currFlow == FLOW_REQ_USER_PWD) {
                this.pwd = message;
                boolean authSuccess = true;
                try {
                    terminalSession = new TerminalSession(this.host, this.user, this.pwd);
                    this.terminalSession.bindingInteraction(this);
                } catch (JSchException e) {
                    if (e.getMessage().contains("Auth fail")) {
                        sendMessage("Access denied!\n" + String.format("%s@%s's password:", this.user, this.host));
                        authSuccess = false;
                    }
                }
                if (authSuccess) {
                    currFlow++;
                    terminalSession.cmdRunner("");
                }
            } else {
                currentCmd = message;
                terminalSession.cmdRunner(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SktSSHTerminal.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SktSSHTerminal.onlineCount--;
    }

    /**
     * 流式命令的输出
     *
     * @param buffer 输出的流式信息
     */
    @Override
    public void onStreamShow(String buffer) {
        // TODO：执行SSH流式命令后的输出
        // 类似tail 等 持续输出的命令需要在此处进行处理
        try {
            this.sendMessage(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行交互式命令的输出
     *
     * @param buffer 输出的交互式信息
     */
    @Override
    public void onBufferShow(String buffer) {
        // TODO：执行交互式命令的输出
        // 常见输出
        try {
            this.sendMessage(currentCmd == null ? buffer : buffer.replace(" " + currentCmd, ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}