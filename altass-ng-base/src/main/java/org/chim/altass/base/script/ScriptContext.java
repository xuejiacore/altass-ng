/**
 * Project: x-framework
 * Package Name: org.ike.core.engine
 * Author: Xuejia
 * Date Time: 2016/12/8 23:33
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.script;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.MapContext;

/**
 * Class Name: ScriptContext
 * Create Date: 2016/12/8 23:33
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ScriptContext {
    private final JexlContext scriptContext = new MapContext();

    public ScriptContext() {
    }

    public JexlContext getScriptContext() {
        return this.scriptContext;
    }

    public Object get(String name) {
        return this.scriptContext.get(name);
    }

    public void set(String name, Object value) {
        this.scriptContext.set(name, value);
    }

    public boolean has(String name) {
        return this.scriptContext.has(name);
    }

}
