package org.chim.altass.core.executor.config;

/**
 * Class Name: RandomSequenceConfig
 * Create Date: 11/16/18 8:23 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 字符序列化配置
 */
public class ComplexSequenceConfig {

    // 序列化类型，1 uuid字符串，2随机字符串，3 自定义规则字符串
    private Integer seqType = null;

    // 生成数量
    private Integer cnt = null;

    public Integer getSeqType() {
        return seqType;
    }

    public void setSeqType(Integer seqType) {
        this.seqType = seqType;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
