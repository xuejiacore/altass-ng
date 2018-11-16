package org.chim.altass.toolkit.script;

import org.chim.altass.base.script.Script;
import org.chim.altass.base.script.ScriptContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Class Name: ScriptFunctionTest
 * Create Date: 11/6/18 9:37 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ScriptFunctionTest {

    @Test
    public void test() {
        Script script = new Script();
        HashMap<String, String> data = new HashMap<>();
        data.put("name", "chim");
        data.put("age", "26");
        ScriptContext context = new ScriptContext();
        context.set("d", data);
        script.setFunctions("n", TestFunction.class);
        System.out.println((Long) script.evaluateScript("n:test() * 2 /10000000", context));
    }

    @Test
    public void testValueSet() {
        Script script = new Script();
        ScriptContext context = new ScriptContext();
        script.evaluateScript("{a = 10 + 1;" +
                "b = a + 5;}", context);
        System.err.println("a = " + context.get("a") + " | b = " + context.get("b"));
    }

    @Test
    public void testCondition() {
        Script script = new Script();
        ScriptContext context = new ScriptContext();
        context.set("index", 5);
        context.set("val", 5);
        Boolean b = script.evaluateScript("index == val", context);
        Assert.assertTrue(b);

        context.set("val", 4);
        b = script.evaluateScript("index != val", context);
        Assert.assertTrue(b);

        b = script.evaluateScript("index % 2 == 1", context);
        Assert.assertTrue(b);

        String val = script.evaluateScript("'asdf' + index", context);
        System.out.println(val);
    }
}
