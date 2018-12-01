package org.chim.altass.toolkit.script;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.chim.altass.base.script.Script;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: JsonHelper
 * Create Date: 11/24/18 1:20 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class JsonHelper {

    private static final Pattern matchIdxTag = Pattern.compile("(.*)\\[(\\d+)]$");

    private static final Script script = new Script();


    public static final Object jsonget(String json, String keyExpress) throws Exception {
        return jsonget(JSON.parseObject(json), keyExpress, null, false);
    }

    public static final Object jsonget(JSONObject obj, String keyExpress) throws Exception {
        return jsonget(obj, keyExpress, null, false);
    }

    public static Object jsonget(JSONObject obj, String keyExpress, Map<String, Object> context, boolean paramParse) throws Exception {
        return jsonget(obj, keyExpress, context, paramParse, null);
    }

    public static Object jsonget(String json, String keyExpress, Map<String, Object> context, boolean paramParse) throws Exception {
        return jsonget(JSON.parseObject(json), keyExpress, context, paramParse, null);
    }

    public static Object jsonget(JSONObject obj, String keyExpress, Script script) throws Exception {
        return jsonget(obj, keyExpress, null, false, script);
    }

    public static Object jsonget(String json, String keyExpress, Script script) throws Exception {
        return jsonget(JSON.parseObject(json), keyExpress, null, false, script);
    }


    /**
     * to find the attributes from a common entry
     * <p>
     * recognize the object search expression an return the val
     *
     * @param obj        the obj that will be searcher in recursive call
     * @param keyExpress the expression to parse
     * @param paramParse true if need to parse parameters, else false
     * @param context    express context
     * @param script     parse script
     * @return the value of found object
     */
    @SuppressWarnings("unchecked")
    public static Object jsonget(JSONObject obj, String keyExpress, Map<String, Object> context, boolean paramParse, Script script) throws Exception {
        if (script == null) {
            script = JsonHelper.script;
        }
        // prepare the key expression
        keyExpress = keyExpress.startsWith(".") ? keyExpress.substring(1) : keyExpress;

        // is a simple pure key to obtain value which not contain symbol dot.
        if (!keyExpress.contains(".")) {
            if (keyExpress.length() == 0 || !keyExpress.contains("[") && !keyExpress.contains("]")) {
                // empty key will back obj or will return the value with key expression
                Object data = keyExpress.length() == 0 ? obj : obj.get(keyExpress);
                if (data instanceof Map) {
                    // parse variables by self
                    for (Object key : ((Map) data).keySet()) {
                        Object val = ((Map) data).get(key);
                        if (val instanceof String) {
                            if (paramParse) {
                                val = script.parseScript(context, (String) val);
                            }
                            ((Map) data).put(key, val);
                        }
                    }
                }
                return data;
            }
        }

        // split the key expression
        String[] keyArrays = keyExpress.split("\\.");

        // the value that will be return
        Object attr;

        // expression detector with symbol dot
        int index = 0;

        String key = "";
        // to save pure key which contain index tag
        String pureKey;
        // to save array index which contain index tag
        int arrayIdx;

        do {
            key += (index == 0 ? "" : ".") + keyArrays[index++];
            // match whether key contain a array index, it will tag the key is a array object
            Matcher matcher = matchIdxTag.matcher(key);
            if (!matcher.find()) {
                attr = obj.get(key);
            } else {
                pureKey = matcher.group(1);
                arrayIdx = Integer.valueOf(matcher.group(2));
                attr = ((JSONArray) obj.get(pureKey)).get(arrayIdx);
            }
        } while (attr == null && index < keyArrays.length);


        if (attr != null) {
            if (attr instanceof JSONObject) {
                String keyExpr = keyExpress.replaceFirst(key.replace("[", "\\["), "");
                return jsonget((JSONObject) attr, keyExpr, context, paramParse);
            } else {
                return attr;
            }
        }

        // not found any value of
        return null;
    }
}
