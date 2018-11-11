/**
 * Project: x-framework
 * Package Name: org.ike.core.engine
 * Author: Xuejia
 * Date Time: 2016/12/8 23:29
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.script;

import org.apache.commons.jexl2.JexlEngine;

import java.util.Map;

/**
 * Class Name: ScriptEngine
 * Create Date: 2016/12/8 23:29
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 脚本解释引擎
 */
public class ScriptEngine {
    // 脚本解释器
    private final JexlEngine scriptEngine = new JexlEngine();

    public ScriptEngine() {
        this.scriptEngine.setCache(512);
        this.scriptEngine.setLenient(false);
        this.scriptEngine.setSilent(false);
    }

    /**
     * 将方法设置到引擎中
     *
     * @param functions 需要设置的方法
     */
    public void setFunctions(Map<String, Object> functions) {
        this.scriptEngine.setFunctions(functions);
    }

    /**
     * 创建一个表达式
     *
     * @param expression 需要创建的表达式字符串
     * @return 返回创建的表达式
     */
    public ScriptExpression createExpression(String expression) {
        ScriptExpression scriptException = new ScriptExpression();
        scriptException.setScriptException(this.scriptEngine.createExpression(expression));
        return scriptException;
    }
}
