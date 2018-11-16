package org.chim.altass.core.executor.toolkit;

import org.apache.commons.lang3.StringUtils;
import org.chim.altass.base.script.Script;
import org.chim.altass.core.annotation.AltassAutowired;
import org.chim.altass.core.annotation.Executable;
import org.chim.altass.core.annotation.Resource;
import org.chim.altass.core.constant.ExecutorAbility;
import org.chim.altass.core.constant.StreamData;
import org.chim.altass.core.exception.ExecuteException;
import org.chim.altass.core.executor.AbstractPipelineExecutor;
import org.chim.altass.core.executor.config.ComplexSequenceConfig;
import org.chim.altass.core.executor.config.SimpleSequenceConfig;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: GenSequenceExecutor
 * Create Date: 11/16/18 6:52 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 序列生成节点，用于生成一系列序列，可以用于后续节点的迭代序列的生成，支持流式处理
 * <p>
 * 目前支持数字序列的迭代生成，字符串序列的生成，随机字符串的生成
 * <p>
 * 生成如果是范围数据，则左开右闭
 */
@Executable(name = "genSeqExecutor", assemble = true, ability = ExecutorAbility.ABILITY_STREAMING)
@Resource(name = "序列生成器", clazz = GenSequenceExecutor.class, midImage = "res/images/executor/seqgen_bg.png", pageUrl = "nodeConfigs/ext/seqgenNodeConfig.jsp")
public class GenSequenceExecutor extends AbstractPipelineExecutor {

    /**
     * 简单数字生成器的配置
     */
    @AltassAutowired(required = false)
    private SimpleSequenceConfig simpleSequenceConfig = null;

    /**
     * 复杂字符序列生成器的配置
     */
    @AltassAutowired(required = false)
    private ComplexSequenceConfig complexSequenceConfig = null;

    /**
     * 初始化一个节点执行器
     *
     * @param executeId 执行id
     */
    public GenSequenceExecutor(String executeId) throws ExecuteException {
        super(executeId);
    }

    @Override
    protected void dataSource() throws ExecuteException {
        if (simpleSequenceConfig != null) {
            Long start = simpleSequenceConfig.getStart();
            Long end = simpleSequenceConfig.getEnd();

            String expression = simpleSequenceConfig.getExpression();
            String outputExpression = simpleSequenceConfig.getOutputExpression();
            boolean useExpress = StringUtils.isNotEmpty(expression);
            boolean useOutExpress = StringUtils.isNotEmpty(outputExpression);

            Map<String, Object> runtimeParam = null;
            int index = 0;
            if (useExpress) {
                runtimeParam = new HashMap<>();
            }

            for (Long s = start; s < end; s++) {
                if (useExpress) {
                    runtimeParam.put("index", index);
                    runtimeParam.put("val", s);
                    Boolean isTrue = this.script.evaluateScript(expression, runtimeParam);
                    if (isTrue) {
                        if (useOutExpress) {
                            String result = this.script.evaluateScript(outputExpression, runtimeParam);
                            pushData(new StreamData(this.executeId, null, result));
                        } else {
                            pushData(new StreamData(this.executeId, null, s));
                        }
                    }
                } else {
                    pushData(new StreamData(this.executeId, null, s));
                }
                index++;
            }

        } else if (complexSequenceConfig != null) {

        }
    }

    @Override
    protected boolean onPipelineInit() throws ExecuteException {
        if (simpleSequenceConfig == null && complexSequenceConfig == null) {
            throw new ExecuteException(new IllegalArgumentException("Could not be found any sequence configuration." +
                    " Maybe RangeSequenceConfig or RandomSequenceConfig"));
        }

        this.script = new Script();
        return true;
    }

    @Override
    public void rollback(StreamData data) {

    }

    @Override
    protected void onArrange(UpdateAnalysis analysis) {

    }
}
