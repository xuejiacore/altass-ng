/**
 * Project: x-framework
 * Package Name: org.ike.core.exception
 * Author: Xuejia
 * Date Time: 2016/12/8 23:32
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.script;

import org.apache.commons.jexl2.Expression;

/**
 * Class Name: ScriptException
 * Create Date: 2016/12/8 23:32
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Script 表达式
 */
public class ScriptExpression {

    private Expression expression;

    public ScriptExpression() {
    }

    public void setScriptException(Expression scriptExpression) {
        this.expression = scriptExpression;
    }

    public Object evaluate(ScriptContext context) {
        return this.expression.evaluate(context.getScriptContext());
    }

}
