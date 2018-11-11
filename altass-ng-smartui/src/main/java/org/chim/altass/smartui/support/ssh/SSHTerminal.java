/**
 * Project: x-framework
 * Package Name: org.ike.monitor.support.ssh
 * Author: Xuejia
 * Date Time: 2016/10/9 23:26
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.support.ssh;

import com.jcraft.jsch.*;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.EofMatch;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import expect4j.matches.TimeoutMatch;
import org.apache.oro.text.regex.MalformedPatternException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Class Name: SSHTerminal
 * Create Date: 2016/10/9 23:26
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: SSH终端操作
 */
@SuppressWarnings({"unused", "Duplicates"})
public class SSHTerminal {
    private final Logger log = LoggerFactory.getLogger(SSHTerminal.class);

    public static final String SETTING_K_BUFFER_SIZE = "BUFFER_SIZE";       // 缓冲大小，默认值为1024
    public static final String SETTING_K_SSH_PORT = "SSH_PORT";             // 连接端口，默认值为22
    public static final String SETTING_K_TIME_OUT = "DEFAULT_TIME_OUT";     // 超时时常，默认值为3000

    private JSch jSch = null;                                               // JSch实例
    private Session session = null;                                         // Session
    private Channel channel = null;                                         // 交互类型的Channel
    private ChannelExec channelExec = null;                                 // 执行类型的Channel
    private Expect4j expect = null;                                         // Expect
    private TerminalListener terminalListener = null;                       // 执行结果回调监听器

    public static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;          // 命令执行成功的返回
    public static final String BACKSLASH_R = "\r";                          // 回车符
    public static final String BACKSLASH_N = "\n";                          // 换行符
    public static final String COLON_CHAR = ":";                            // Colon符号
    public static final String ENTER_CHARACTER = BACKSLASH_R;               // 回车符

    private boolean checkExpect = false;                                    // 是否开启
    private ChannelType channelType = ChannelType.SHELL;                    // Channel 的模式，默认为Shell类型
    private HashMap<String, Object> terminalConfig = new HashMap<>();       // 终端配置


    public static final String[] linuxPromptRegEx = new String[]{
            "~]#", "~#", "#", ":~#", ":~$", "/$", ">", "\r\n"
    };                                                                  // 正则匹配，用于处理服务器返回的结果
    private List<Match> lstPattern = null;

    /**
     * 初始化一个SSH连接工具箱类
     */
    public SSHTerminal() {
        this.jSch = new JSch();
        this.prepareDefaultSetting();
    }

    /**
     *
     */
    private void prepareDefaultSetting() {
        configTerminal(SETTING_K_BUFFER_SIZE, 1024);
        configTerminal(SETTING_K_SSH_PORT, 22);
        configTerminal(SETTING_K_TIME_OUT, 3000);
//        configTerminal("BUFFER_SIZE", BUFFER_SIZE);
    }

    /**
     * 终端输出监听器
     */
    public interface TerminalListener {
        /**
         * 命令流式输出回调
         *
         * @param buffer 输出的流式信息
         */
        void onStreamShow(String buffer);

        /**
         * 交互式Shell信息输出
         *
         * @param buffer 输出的交互式信息
         */
        void onBufferShow(String buffer);
    }

    /**
     * 以默认方式打开Session
     *
     * @param host 主机地址
     * @param user 用户名
     * @param pwd  用户密码
     * @return 返回工具箱实例
     * @throws JSchException
     */
    public SSHTerminal confSession(String host, String user, String pwd) throws JSchException {
        return confSession(new SSHInfo(host, user, pwd));
    }

    /**
     * 配置终端
     *
     * @param key 配置名称
     * @param val 配置的值
     * @return 返回当前SSH终端实例
     */
    public SSHTerminal configTerminal(String key, Object val) {
        this.terminalConfig.put(key, val);
        return this;
    }

    /**
     * 配置Session
     *
     * @param info SSH的配置信息
     * @return 返回工具箱实例
     * @throws JSchException
     */
    public SSHTerminal confSession(SSHInfo info) throws JSchException {
        this.session = this.jSch.getSession(info.user, info.host, info.port < 0 ? 22 : info.port);
        this.session.setPassword(info.pwd);
        this.session.setUserInfo(info.userInfo);
        return this;
    }

    /**
     * 配置session的属性
     *
     * @param key 属性的键名
     * @param val 属性的键值
     * @return 返回工具箱实例
     */
    public SSHTerminal configSession(SessionOptional key, String val) {
        this.session.setConfig(key.toString(), val);
        return this;
    }

    /**
     * 配置Channel
     *
     * @param channelType ChannelType
     * @param inps        输入流
     * @param outs        输出流
     * @return 返回工具箱实例
     * @throws JSchException
     */
    public SSHTerminal confChannel(ChannelType channelType, InputStream inps, OutputStream outs) throws JSchException {
        if (!this.session.isConnected()) {
            connectSession();
        }
        this.channel = this.session.openChannel(channelType.toString());
        this.channel.setInputStream(inps);
        this.channel.setOutputStream(outs);
        this.expect = new Expect4j(inps, outs);
        return this;
    }

    /**
     * 打开Shell Channel，不同于ExecChannel，Shell Channel需要手动打开
     *
     * @throws JSchException
     */
    public void openShellChannel() throws JSchException {
        if (!this.channel.isConnected()) {
            this.channel.connect();
        }
    }

    public boolean sendSignal(String signal) throws Exception {
        channel.sendSignal(signal);
        return true;
    }

    /**
     * 执行执行命令
     *
     * @param cmd 需要执行的命令
     * @return 如果执行成功，那么返回值为true，否则返回值为false
     */
    public boolean execCmd(String cmd) {
        if (this.terminalListener == null) {
            return false;
        }
        if (this.channelExec == null || this.channelExec.isClosed()) {
            this.getExecChanel();
        }
        try {
            InputStream input = channelExec.getInputStream();
            channelExec.setCommand(cmd);
            channelExec.connect();

            this.terminalListener.onStreamShow(" " + cmd + "\n");

            byte[] buffer = new byte[(int) this.terminalConfig.get(SETTING_K_BUFFER_SIZE)];
            while (true) {
                while (input.available() > 0) {
                    int i = input.read(buffer, 0, (int) this.terminalConfig.get(SETTING_K_BUFFER_SIZE));
                    if (i < 0) {
                        break;
                    }
                    this.terminalListener.onStreamShow(new String(buffer, 0, i));
                }
                if (channelExec.isClosed()) {
                    if (input.available() > 0) {
                        continue;
                    }
                    break;
                }
            }

        } catch (IOException | JSchException e) {
            e.printStackTrace();
        }
        this.channelExec.disconnect();
        return false;
    }

    /**
     * 执行脚本
     *
     * @param shell 需要执行的Shell集合
     * @return 如果执行成功，那么返回值为true，否则返回值为false
     */
    public boolean runShell(String[] shell) {
        if (expect == null || this.channel.isClosed()) {
            try {
                confChannel(ChannelType.SHELL);
            } catch (JSchException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        Closure closure = new Closure() {
            @Override
            public void run(ExpectState expectState) throws Exception {
                if (terminalListener != null) {
                    terminalListener.onBufferShow(expectState.getBuffer());
                }
                expectState.exp_continue();
            }
        };
        if (lstPattern == null) {
            lstPattern = new ArrayList<>();
            String[] regEx = linuxPromptRegEx;
            if (regEx.length > 0) {
                for (String regexEle : regEx) {
                    try {
                        RegExpMatch mat = new RegExpMatch(regexEle, closure);
                        lstPattern.add(mat);
                    } catch (MalformedPatternException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                lstPattern.add(new EofMatch(new Closure() {
                    @Override
                    public void run(ExpectState expectState) throws Exception {
                    }
                }));

                lstPattern.add(new TimeoutMatch((Integer) this.terminalConfig.get(SETTING_K_TIME_OUT), new Closure() {
                    @Override
                    public void run(ExpectState expectState) throws Exception {
                    }
                }));
            }
        }

        for (String cmd : shell) {
            if (!isSuccess(lstPattern, cmd)) {
                return false;
            }
        }

        /*
         * TODO：此处需要进行优化处理
         * 目前使用开启线程的方式处理结果检查，该方式有一定的时间消耗，如果不放在线程中处理，会导致卡顿现象
         */
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        checkResult(expect.expect(lstPattern));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 绑定终端输出响应监听器
     *
     * @param listener 需要绑定的监听接口
     */
    public void bindingTerminalListener(TerminalListener listener) {
        this.terminalListener = listener;
    }

    /**
     * 关闭通道
     */
    public void disconnect() {
        if (this.channelExec != null && this.channelExec.isConnected()) {
            this.channelExec.disconnect();
        }
        if (this.channel != null && this.channel.isConnected()) {
            this.channel.disconnect();
        }
        if (this.session.isConnected()) {
            this.session.disconnect();
        }
    }

    /**
     * 连接并打开Session
     *
     * @return 返回工具箱实例
     */
    public SSHTerminal connectSession() throws JSchException {
        this.session.connect();
        return this;
    }

    /**
     * 是否开启表达式检查
     * 如果开启会导致响应速度慢的问题
     */
    public void openExpectChecker() {
        checkExpect = false;
    }

    /**
     * 配置Channel
     *
     * @param channelType ChannelType
     * @return 返回工具类实例
     * @throws JSchException
     * @throws IOException
     */
    private SSHTerminal confChannel(ChannelType channelType) throws JSchException, IOException {
        if (!this.session.isConnected()) {
            connectSession();
        }
        this.channel = this.session.openChannel(channelType.toString());
        this.channel.connect();
        this.expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
        return this;
    }

    /**
     * 获得可执行的Channel
     *
     * @return 返回可执行Channel实例
     */
    private ChannelExec getExecChanel() {
        try {
            this.channelExec = (ChannelExec) this.session.openChannel(ChannelType.EXEC.toString());
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return this.channelExec;
    }

    /**
     * 获得Shell Channel
     *
     * @return 返回Shell Channel 实例
     */
    private Channel getShellChannel() {
        try {
            confChannel(ChannelType.SHELL);
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return this.channel;
    }

    /**
     * 判断命令执行是否成功
     *
     * @param matches 匹配项
     * @param cmd     命令
     * @return 如果执行成功，那么返回值为true，否则返回值为false
     */
    private boolean isSuccess(List<Match> matches, String cmd) {
        try {
            boolean isFailed = false;
            if (checkExpect) {
                isFailed = checkResult(expect.expect(matches));
            }
            if (!isFailed) {
                expect.send(cmd);
                expect.send("\r");
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 检查执行返回的状态
     *
     * @param intRetVal 命令执行返回值
     * @return 如果执行的结果是-2，那么返回值为true，否则返回值为false
     */
    private boolean checkResult(int intRetVal) {
        return intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE;
    }

    public static void main(String[] args) {
        try {
            SSHTerminal sshTerminal = new SSHTerminal();
//            sshTerminal.confSession("115.28.40.152", "zhanxuejia", "ikezhanxuejia!@#")
            sshTerminal.confSession("120.24.248.37", "root", "darkkidzxjQWE!@#")
                    .configSession(SessionOptional.K_STRICT_HOST_KEY_CHECKING,
                            SessionOptional.V_DEFAULT_STRICT_HOST_KEY_CHECKING.toString())
                    .connectSession();
//                    .confChannel(ChannelType.SHELL)
//                    .openChannel();

            sshTerminal.bindingTerminalListener(new TerminalListener() {
                @Override
                public void onStreamShow(String buffer) {
                    System.out.println(buffer);
                }

                @Override
                public void onBufferShow(String buffer) {
                    System.out.println(buffer);
                }
            });
            System.err.println("----------------ready-------------------");
            sshTerminal.execCmd("tail -f /root/tomcat_sso/apache-tomcat-6.0.37/logs/catalina.out");

            Thread.sleep(1000);
            sshTerminal.sendSignal("SIGINT");
        } catch (JSchException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    public class SSHInfo {
        private String host = null;
        private String user = null;
        private String pwd = null;
        private int port = -1;
        private UserInfo userInfo = null;
        private Properties connConf = null;

        public SSHInfo(String host, String user, String pwd, int port, UserInfo userInfo, Properties connConf) {
            this.host = host;
            this.user = user;
            this.pwd = pwd;
            this.port = port;
            this.userInfo = userInfo;
            this.connConf = connConf;
        }

        public SSHInfo(String host, String user, String pwd, int port) {
            this.host = host;
            this.user = user;
            this.pwd = pwd;
            this.port = port;
        }

        public SSHInfo(String host, String user, String pwd) {
            this.host = host;
            this.user = user;
            this.pwd = pwd;
        }

        public SSHInfo(String host, String user, String pwd, UserInfo userInfo) {
            this.host = host;
            this.user = user;
            this.pwd = pwd;
            this.userInfo = userInfo;
        }
    }

}
