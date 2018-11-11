package org.chim.altass.core.domain.buildin.attr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.base.script.Script;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: ACommon
 * Create Date: 2017/9/4 20:35
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 常规元素的类型
 */
@Elem(alias = "common", version = "1.0")
public class ACommon {
    private static final Pattern matchIdxTag = Pattern.compile("(.*)\\[(\\d+)]$");
    private Script script = null;

    @Attr(alias = "id")
    private Integer id = 0;                                                         // ID
    @Attr(alias = "name")
    private String name = null;                                                     // 数据名称
    @Attr(alias = "tag")
    private String tag = null;                                                      // 数据标签
    @Attr(alias = "jsonAttr")
    private String jsonAttr = null;                                                 // json格式的扩展数据，与extAttr内容相同

    /**
     * 解析后存储字段，持久化使用的是jsonAttr，在Entry中进行reset操作即可
     */
    private Map<String, Object> extAttr = null;                                     // 扩展数据

    private Map<String, Object> parseContext = null;

    public ACommon() {
        this.script = new Script();
    }

    public ACommon(Integer id, String name) {
        this(id, name, null);
    }

    public ACommon(Integer id, String name, String tag) {
        this(id, name, tag, null);
    }

    public ACommon(Integer id, String name, String tag, Map<String, Object> extAttr) {
        this();
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.extAttr = extAttr;
        this.parseContext = new HashMap<String, Object>(this.extAttr);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, Object> getExtAttr() {
        return extAttr;
    }

    public void setExtAttr(Map<String, Object> extAttr) {
        this.extAttr = extAttr;
        this.parseContext = new HashMap<String, Object>(this.extAttr);
        this.jsonAttr = JSON.toJSONString(this.extAttr);
    }

    public String getJsonAttr() {
        return jsonAttr;
    }

    @SuppressWarnings("unchecked")
    public void setJsonAttr(String jsonAttr) {
        this.jsonAttr = jsonAttr;
        this.extAttr = JSON.parseObject(jsonAttr, Map.class);
        this.parseContext = new HashMap<String, Object>(this.extAttr);
    }

    /**
     * make attention to this, default config in 'common' property will be cover by extParse, generally, extParse may be
     * a input parameters of the executor.
     *
     * @param extParse to extend the parser context
     */
    public void extParseContext(Map<String, Object> extParse) {
        if (extParse != null && extParse.size() > 0) {
            if (this.parseContext == null) {
                this.parseContext = new HashMap<String, Object>(this.extAttr);
            }
            this.parseContext.putAll(extParse);
        }
    }

    /**
     * add new attribute to common data source
     *
     * @param key the key that will be add
     * @param val the value that will be add
     */
    public void addAttr(String key, Object val) {
        if (this.extAttr == null) {
            this.extAttr = new HashMap<String, Object>();
        }
        this.extAttr.put(key, val);
        this.jsonAttr = JSON.toJSONString(this.extAttr);
    }

    /**
     * add new attribute to common data source
     *
     * @param keyType the key type will be add, use type camel simple class name.
     * @param val     the value that will be add
     */
    public void addAttr(Class<?> keyType, Object val) {
        String simpleName = keyType.getSimpleName();
        addAttr(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1), val);
    }

    /**
     * add new attribute to common data source
     *
     * @param key     the key will be add.
     * @param jsonStr the value that is a json structure, actually it will be parse as a json object type.
     */
    public void addJsonAttr(String key, String jsonStr) {
        addAttr(key, JSON.parse(jsonStr));
    }

    /**
     * add new attribute to common data source
     *
     * @param keyType the key type will be add, use type camel simple class name.
     * @param jsonStr the value that is a json structure, actually it will be parse as a json object type.
     */
    public void addJsonAttr(Class<?> keyType, String jsonStr) {
        String simpleName = keyType.getSimpleName();
        addJsonAttr(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1), jsonStr);
    }

    /**
     * to find the attributes from current common entry
     * <p>
     * recognize the object search expression an return the val
     *
     * @param keyExpr    the expression to parse
     * @param paramParse 是否需要参数解析
     * @return the value of found object
     */
    public Object getAttr(String keyExpr, boolean paramParse) {
        try {
            return getAttr(null, keyExpr, paramParse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * to find the attributes from a common entry
     * <p>
     * recognize the object search expression an return the val
     *
     * @param obj        the obj that will be searcher in recursive call
     * @param keyExpress the expression to parse
     * @param paramParse 是否进行参数解析
     * @return the value of found object
     */
    @SuppressWarnings("unchecked")
    private Object getAttr(JSONObject obj, String keyExpress, boolean paramParse) throws Exception {
        // prepare the key expression
        keyExpress = keyExpress.startsWith(".") ? keyExpress.substring(1) : keyExpress;
        JSONObject jsonObject = obj != null ? obj : JSON.parseObject(this.jsonAttr);

        // is a simple pure key to obtain value which not contain symbol dot.
        if (!keyExpress.contains(".")) {
            if (keyExpress.length() == 0 || !keyExpress.contains("[") && !keyExpress.contains("]")) {
                // empty key will back obj or will return the value with key expression
                Object data = keyExpress.length() == 0 ? jsonObject : jsonObject.get(keyExpress);
                if (data instanceof Map) {
                    // parse variables by self
                    for (Object key : ((Map) data).keySet()) {
                        Object val = ((Map) data).get(key);
                        if (val instanceof String) {
                            if (paramParse) {
                                val = this.script.parseScript(this.parseContext, (String) val);
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
            if (matcher.find()) {

                pureKey = matcher.group(1);
                arrayIdx = Integer.valueOf(matcher.group(2));
                attr = ((JSONArray) jsonObject.get(pureKey)).get(arrayIdx);
            } else {

                attr = jsonObject.get(key);
            }
        } while (attr == null && index < keyArrays.length);


        if (attr != null) {
            if (attr instanceof JSONObject) {
                String keyExpr = keyExpress.replaceFirst(key.replace("[", "\\["), "");
                return getAttr((JSONObject) attr, keyExpr, paramParse);
            } else {
                return attr;
            }
        }

        // not found any value of
        return null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
