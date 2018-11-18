package org.chim.altass.base.script;

import org.apache.commons.jexl2.MapContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class Name: AltassScriptContext
 * Create Date: 11/18/18 4:39 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class AltassScriptContext extends MapContext {

    private Set<String> vars = null;

    public AltassScriptContext() {
        super();
        vars = new HashSet<>();
    }

    public AltassScriptContext(Map<String, Object> varMap) {
        super(varMap);
        vars = new HashSet<>();
    }

    @Override
    public boolean has(String name) {
        return super.has(name);
    }

    @Override
    public Object get(String name) {
        return super.get(name);
    }

    @Override
    public void set(String name, Object value) {
        super.set(name, value);
        vars.add(name);
    }

    public Set<String> varSet() {
        return vars;
    }

    public Map valMapping() {
        return map;
    }
}
