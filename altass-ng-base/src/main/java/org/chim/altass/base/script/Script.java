/**
 * Project: x-framework
 * Package Name: org.ike.core.engine
 * Author: Xuejia
 * Date Time: 2016/12/8 23:35
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.script;


import org.chim.altass.base.taglib.Functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: Script
 * Create Date: 2016/12/8 23:35
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 脚本执行器
 */
public class Script {

    private final ScriptEngine scriptEngine = new ScriptEngine();
    private final Map<String, Object> functions = new HashMap<>();

    /**
     * 创建一个脚本执行器
     */
    public Script() {
        this.functions.putAll(getCoreBaseFunc());
        this.scriptEngine.setFunctions(this.functions);
    }

    /**
     * 向脚本执行器中添加命名空间以及方法
     *
     * @param namespaces 指定js调用的命名空间
     * @param function   指定调用的方法类
     */
    public void setFunctions(String namespaces, Object function) {
        this.functions.put(namespaces, function);
    }

    /**
     * 向脚本执行器中添加方法
     *
     * @param function 指定调用的方法类
     */
    public void setFunctions(Object function) {
        this.setFunctions((String) null, function);
    }

    public Map<String, Class> getCoreBaseFunc() {
        Map<String, Class> functions = new HashMap<>();
        functions.put(null, Functions.class);
        return functions;
    }

    /**
     * 获得脚本引擎
     *
     * @return 脚本迎请对象实例
     */
    public ScriptEngine getScriptEngine() {
        return this.scriptEngine;
    }

    /**
     * 获得脚本上下文
     *
     * @return 脚本上下文
     */
    public ScriptContext getScriptContext() {
        return new ScriptContext();
    }

    public ScriptContext getScriptContext(Map<String, Object> context) {
        ScriptContext scriptContext = this.getScriptContext();
        if (context != null) {
            for (Object o : context.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                scriptContext.set((String) entry.getKey(), entry.getValue());
            }
        }

        return scriptContext;
    }

    /*    *//**
     * 设置脚本上下文
     *
     * @param context 脚本的页面上下文对象
     * @return 返回脚本你的上下文对象
     *//*
    public ScriptContext getScriptContext(PageContext context) {
        Map<String, Object> pageContext = WebUtil.getWebContext(context);
        return this.getScriptContext(pageContext);
    }*/

    /**
     * 计算表达式
     *
     * @param script  需要执行的表达式语句
     * @param context 表达式执行的上下文环境
     * @param <T>     返回类型
     * @return 返回表达式的执行结果
     */
    @SuppressWarnings("all")
    public <T> T evaluateScript(String script, ScriptContext context) {
        try {
            ScriptExpression e = this.getScriptEngine().createExpression(script);
            return (T) e.evaluate(context);
        } catch (Exception e) {
            return (T) "";
        }
    }

    /**
     * 根据上下文对象计算表达式值
     *
     * @param script  需要执行的脚本表达式
     * @param context 上下文对象
     * @param <T>     返回类型
     * @return 返回表达式的执行结果
     */
    @SuppressWarnings("unchecked")
    public <T> T evaluateScript(String script, Map<String, Object> context) {
        ScriptContext scriptContext = this.getScriptContext(context);
        Object o = this.evaluateScript(script, scriptContext);
        context.putAll(((AltassScriptContext)scriptContext.getScriptContext()).valMapping());
        return (T) o;
    }

    /*    *//**
     * 根据页面的上下文计算表达式结果
     *
     * @param script  需要计算的脚本表达式
     * @param context 页面上下文对象
     * @param <T>     返回类型
     * @return 返回表达式的执行结果
     *//*
    public <T> T evaluateScript(String script, PageContext context) {
        ScriptContext scriptContext = this.getScriptContext(context);
        return this.evaluateScript(script, scriptContext);
    }*/

    /**
     * 使用默认的${}作为变量的开闭符号，解析替换content
     *
     * @param context 解析上下文
     * @param content 需要解析替换的内容
     * @return 返回解析替换后的字符串
     * @throws Exception
     */
    public String parseScript(Map<String, Object> context, String content) throws Exception {
        return this.parseScript(content, context, "${", "}");
    }

    /**
     * 使用map解析替换特定变量的数据
     *
     * @param content 需要解析替换的内容
     * @param context 解析上下文
     * @param open    左开符号
     * @param close   右开符号
     * @return 返回解析替换后的字符串
     * @throws Exception
     */
    public String parseScript(String content, Map<String, Object> context, String open, String close) throws Exception {
        if (content == null) {
            return null;

        } else {
            int scriptLength = content.length();
            if (scriptLength == 0) {
                return content;

            } else {
                ScriptContext scriptContext = getScriptContext(context);
                StringBuilder strBuilder = new StringBuilder();
                int openLen = open.length();
                int closeLen = close.length();

                int pos;
                int end;
                for (pos = 0; pos <= scriptLength - closeLen; pos = end + closeLen) {
                    int start = content.indexOf(open, pos);
                    if (start < 0) {
                        strBuilder.append(content.substring(pos));
                        break;
                    }

                    start += openLen;
                    end = content.indexOf(close, start);
                    if (end < 0) {
                        break;
                    }

                    strBuilder.append(content.substring(pos, start - openLen));
                    Object obj = evaluateScript(content.substring(start, end).trim(), scriptContext);
                    if (obj == null) {
                        throw new Exception("不存在的变量：" + content.substring(start, end).trim());
                    }

                    strBuilder.append(obj);
                }
                return pos == 0 ? content : strBuilder.toString();
            }
        }
    }

//    public static void main(String[] args) throws Exception {
        /*
            Script escript = new Script();
            HashMap<String, String> map = new HashMap<>();
            map.put("name", "zfd");
            map.put("pwd", "123456");
            ScriptContext esc = new ScriptContext();
            esc.set("map1", map);
            escript.setFunctions("n", Functions.class);
            System.err.println((String) escript.evaluateScript("n:java2json(map1)", esc));
        /*
            Script script = new Script();
            Map<String, Object> context = new HashMap<>();
            context.put("op_time", "201703");
            System.err.println(script.parseScript(context, "SELECT * FROM TABLE_NAME_${op_time}"));
        */
        /*
        Script script = new Script();

        ScriptContext context = new ScriptContext();
        context.set("a", "4");
        context.set("b", "4");
        script.evaluateScript("" +
                "if (a == b) {" +
                "   c = a - b;" +
                "} else {" +
                "   c = a * b;" +
                "   d = c + 1;" +
                "}", context);
        System.err.println("c = " + context.get("c") + " | d = " + context.get("d"));
        */
//    }
}
