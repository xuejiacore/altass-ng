package org.chim.altass.core.domain.buildin.attr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.base.script.Script;
import org.chim.altass.toolkit.script.JsonHelper;

import java.util.HashMap;
import java.util.Map;

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
        this.parseContext = new HashMap<>(this.extAttr);
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
        this.parseContext = new HashMap<>(this.extAttr);
        this.jsonAttr = JSON.toJSONString(this.extAttr);
    }

    public String getJsonAttr() {
        return jsonAttr;
    }

    @SuppressWarnings("unchecked")
    public void setJsonAttr(String jsonAttr) {
        this.jsonAttr = jsonAttr;
        this.extAttr = JSON.parseObject(jsonAttr, Map.class);
        this.parseContext = new HashMap<>(this.extAttr);
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
                this.parseContext = new HashMap<>(this.extAttr);
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
            this.extAttr = new HashMap<>();
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
     * @param paramParse true if need to parse parameters, else false
     * @return the value of found object
     */
    public Object getAttr(String keyExpr, boolean paramParse) {
        try {
            return JsonHelper.jsonget(JSON.parseObject(this.jsonAttr), keyExpr, this.parseContext, paramParse, this.script);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
