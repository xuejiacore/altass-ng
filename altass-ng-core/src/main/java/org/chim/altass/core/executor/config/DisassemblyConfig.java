package org.chim.altass.core.executor.config;

/**
 * Class Name: DisassemblyConfig
 * Create Date: 11/24/18 2:20 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DisassemblyConfig {

    private String groupId = null;              // Disassembly group id, will use data unique id if not specify group id rule.

    private String express = null;              // Specifies the key expression to be iterated.

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }
}
