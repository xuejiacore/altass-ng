package org.chim.altass.core.executor.config;

/**
 * Class Name: SequenceConfig
 * Create Date: 11/16/18 8:04 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 范围序列化配置
 */
public class SimpleSequenceConfig {

    // 序列起始位置，左闭
    private Long start = null;
    // 序列结束位置，右开
    private Long end = null;
    // 表达式满足选择器
    private String expression = null;
    // 输出表达式
    private String outputExpression = null;

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getOutputExpression() {
        return outputExpression;
    }

    public void setOutputExpression(String outputExpression) {
        this.outputExpression = outputExpression;
    }
}
