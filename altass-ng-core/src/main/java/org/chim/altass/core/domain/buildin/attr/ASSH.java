package org.chim.altass.core.domain.buildin.attr;


import org.chim.altass.base.parser.xml.annotation.Attribute;
import org.chim.altass.base.parser.xml.annotation.AttributeType;
import org.chim.altass.base.parser.xml.annotation.Element;

/**
 * Class Name: ASSH
 * Create Date: 2017/9/6 0:50
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Element(name = "ssh", version = "1.0", assemble = true, desc = "ssh配置")
public class ASSH {

    @Element(name = "host", desc = "Host配置")
    private AHost host = null;                                                  // 主机信息
    @Attribute(name = "command", type = AttributeType.TEXT, desc = "命令")
    private String command = null;                                              // 需要执行的shell命令
    @Attribute(name = "expectResult", type = AttributeType.TEXT, desc = "期望值")
    private String expectResult = null;                                         // 执行的期望结果

    public AHost getHost() {
        return host;
    }

    public void setHost(AHost host) {
        this.host = host;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getExpectResult() {
        return expectResult;
    }

    public void setExpectResult(String expectResult) {
        this.expectResult = expectResult;
    }

    @Override
    public String toString() {
        return "ASSH{" +
                "host=" + host +
                ", command='" + command + '\'' +
                ", expectResult='" + expectResult + '\'' +
                '}';
    }
}
