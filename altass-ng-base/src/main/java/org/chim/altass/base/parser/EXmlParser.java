package org.chim.altass.base.parser;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.exml.Meta;
import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class Name: XmlParserUtil
 * Create Date: 17-12-11 下午12:35
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 简单的xml解析逆解析工具
 */
@SuppressWarnings({"Duplicates", "unchecked"})
public class EXmlParser {

    /**
     * 将对象转化为xml后写入到对应的文件中
     *
     * @param obj     需要转化成xml文档的对象实例
     * @param outPath 需要输出xml内容的文件路径
     */
    public static void writeTo(Object obj, String outPath) throws XmlParserException, IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");

        Class<?> rootClass = obj.getClass();
        Elem annotation = rootClass.getDeclaredAnnotation(Elem.class);
        Document document = DocumentHelper.createDocument();
        if (annotation != null) {
            Element root = document.addElement(annotation.alias());
            parseClass(root, obj, rootClass);
        } else {
            throw new XmlParserException("Could not found any Element.");
        }
        XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(outPath)), format);
        xmlWriter.write(document);
        xmlWriter.close();
    }

    /**
     * 从指定的文件路径中读取xml内容并解析为对应的对象实例
     *
     * @param inputPath 文件输入路径
     * @param clz       需要转化的类
     * @param <T>       T
     * @return 转化后的对象实例
     */
    public static <T> T readFrom(String inputPath, Class<T> clz) throws IOException, XmlParserException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return fromXml(content.toString(), clz);
        }
    }

    /**
     * 解析泛型List
     */
    private static final Pattern LIST_TYPE_PATTERN = Pattern.compile("java\\.util\\..*?List<(.*)>");

    /**
     * 解析对象为dom字符串（压缩）
     *
     * @param obj 需要解析的对象
     * @return 解析后的压缩格式xml
     * @throws XmlParserException 解析异常
     */
    public static String toXml(Object obj) throws XmlParserException {
        Class<?> rootClass = obj.getClass();
        Elem annotation = rootClass.getDeclaredAnnotation(Elem.class);
        Document document = DocumentHelper.createDocument();
        if (annotation != null) {
            Element root = document.addElement(annotation.alias());
            parseClass(root, obj, rootClass);
        } else {
            throw new XmlParserException("Could not found any Element.");
        }

        return document.asXML();
    }

    /**
     * 将一个对象解析为可阅读的xml字符串
     *
     * @param obj 需要解析的对象实例
     * @return 解析后的可阅读的xml字符串
     * @throws XmlParserException 解析异常
     */
    public static String toXmlPretty(Object obj) throws XmlParserException {
        Class<?> rootClass = obj.getClass();
        Elem annotation = rootClass.getDeclaredAnnotation(Elem.class);
        Document document = DocumentHelper.createDocument();
        if (annotation != null) {
            Element root = document.addElement(annotation.alias());
            parseClass(root, obj, rootClass);
        } else {
            throw new XmlParserException("Could not found any Element.");
        }
        OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
        StringWriter sw = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(sw, xmlFormat);
        try {
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            throw new XmlParserException(e);
        }
        return sw.toString();
    }

    /**
     * 从一个xml字符串中解析成一个对象的实例
     *
     * @param xml       需要解析的xml字符串
     * @param rootClass 解析目标类
     * @param <T>       目标类
     * @return 目标类实例
     * @throws XmlParserException xml解析异常
     */
    public static <T> T fromXml(String xml, Class<T> rootClass) throws XmlParserException {
        if (xml == null || xml.length() == 0) return null;
        // 解析目标
        T targetObj;
        try {
            Document document = DocumentHelper.parseText(xml);
            Element rootElement = document.getRootElement();

            // 递归解析
            targetObj = parseElement(rootElement, rootClass);
        } catch (DocumentException | IllegalAccessException | InstantiationException e) {
            throw new XmlParserException(e);
        }
        return targetObj;
    }

    // =================================================================================================================
    // =================================================================================================================

    /**
     * 递归解析xml节点到Root Class 对应的实例中
     *
     * @param rootElement 当前解析的根节点
     * @param rootClz     需要解析对应的实例类
     * @param <M>         解析后返回的实例对象，对应root class
     * @return 如果解析正常，那么返回root class对应的对象实例
     * @throws IllegalAccessException -
     * @throws InstantiationException -
     * @throws XmlParserException     -
     */
    private static <M> M parseElement(Element rootElement, Class<M> rootClz) throws IllegalAccessException,
            InstantiationException, XmlParserException {
        // 如果是对象类型或者是接口类型的定义，那么需要获取meta定义，对应实现类进行逆解析
        if (Object.class.equals(rootClz) || rootClz.isInterface()) {
            Attribute clzMeta = rootElement.attribute("_meta");
            if (clzMeta != null) {
                try {
                    Matcher matcher = LIST_TYPE_PATTERN.matcher(clzMeta.getValue());
                    Class beanClz = Object.class;
                    Class<?> objClz;
                    if (matcher.find()) {
                        beanClz = Class.forName(matcher.group(1));
                        objClz = Class.forName(List.class.getName());
                    } else {
                        objClz = Class.forName(clzMeta.getValue());
                    }

                    if (isBaseType(objClz)) {
                        return (M) transferBaseType(rootElement.getText(), objClz);
                    }
                    if (Map.class.isAssignableFrom(objClz) || HashMap.class.isAssignableFrom(objClz)) {
                        // 抽取map类型数据
                        return parseMapObjFromXml(rootElement);
                    } else if (List.class.isAssignableFrom(objClz)) {
                        return parseListObjFromXml(rootElement, null, beanClz.getSimpleName(), null, null, beanClz);
                    }
                } catch (ClassNotFoundException | InvocationTargetException e) {
                    throw new XmlParserException(e);
                }
                return null;
            }
        }
        M targetObj = rootClz.newInstance();
        Elem annotation = rootClz.getDeclaredAnnotation(Elem.class);
        if (rootElement == null || annotation == null) return null;
        if (!rootElement.getName().equals(annotation.alias())) return null;

        // 此处需要进行解析优先级排序
        List<Field> sortedPriority = sortedFieldAnnotationPriority(getFields(rootClz));
        // 堆排序后的字段列表进行注解映射
        LinkedHashMap<String, Field> fieldMap = new LinkedHashMap<>();
        for (Field field : sortedPriority) {
            Elem elemAnnotation = field.getAnnotation(Elem.class);
            if (elemAnnotation != null) {
                fieldMap.put(elemAnnotation.alias(), field);
            } else {
                Attr attrAnnotation = field.getAnnotation(Attr.class);
                if (attrAnnotation != null) {
                    fieldMap.put(attrAnnotation.alias(), field);
                }
            }
        }

        // 获取当前类所有需要解析的字段
        for (String nodeName : fieldMap.keySet()) {
            Element fieldElem = rootElement.element(nodeName);
            Attribute fieldAttr = rootElement.attribute(nodeName);
            if (fieldElem == null && fieldAttr == null) continue;
            Field field = fieldMap.get(nodeName);
            if (field == null) continue;
            Class fieldClz = field.getType();
            Elem fieldAnnotation = (Elem) fieldClz.getAnnotation(Elem.class);

            try {
                String fieldName = field.getName();
                Method setMethod = rootClz.getMethod("set" +
                        fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
                if (fieldAnnotation != null && fieldElem != null) {
                    Object target = parseElement(fieldElem, fieldClz);
                    setMethod.invoke(targetObj, target);
                } else {
                    Attr attrAnnotation = (Attr) fieldClz.getAnnotation(Attr.class);
                    if (attrAnnotation != null) {
                        throw new XmlParserException("暂不支持的解析类型");
                    } else {
                        if (isBaseType(fieldClz)) {
                            // 基本类型
                            setMethod.invoke(targetObj, transferBaseType(fieldAttr.getData(), field.getType()));
                        } else {
                            if (List.class.isAssignableFrom(fieldClz)) {
                                // 获取当前节点的子节点，遍历解析
                                parseListObjFromXml(rootElement, targetObj, nodeName, field, setMethod, null);
                            } else if (Map.class.isAssignableFrom(fieldClz)) {
                                Object o = parseMapObjFromXml(rootElement.element(nodeName));
                                setMethod.invoke(targetObj, o);
                            } else {
                                throw new XmlParserException("暂不支持的解析类型");
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException | InvocationTargetException | XmlParserException | ClassNotFoundException e) {
                throw new XmlParserException(e);
            }
        }
        return targetObj;
    }

    /**
     * 对解析优先级进行排序
     *
     * @param fields 需要进行排序筛选的filed列表
     * @return 返回优先级排序后的待解析字段
     */
    private static List<Field> sortedFieldAnnotationPriority(List<Field> fields) {
        if (fields == null || fields.size() == 0) {
            return fields;
        }

        // 获得所有合法的field注解定义
        List<Field> sortedPriority = new ArrayList<>();
        for (Field field : fields) {
            Elem elemAnnotation = field.getAnnotation(Elem.class);
            if (elemAnnotation != null) {
                sortedPriority.add(field);
            } else {
                Attr attrAnnotation = field.getAnnotation(Attr.class);
                if (attrAnnotation != null) {
                    sortedPriority.add(field);
                }
            }
        }

        // 对抽取的field注解优先级进行升序排序
        sortedPriority.sort((field1, field2) -> {
            Integer priority1;
            Integer priority2;
            Annotation field1Annotation;
            Annotation field2Annotation;
            field1Annotation = field1.getType().getAnnotation(Elem.class);
            if (field1Annotation == null) {
                field1Annotation = field1.getType().getAnnotation(Attr.class);
                priority1 = field1Annotation == null ? Integer.MAX_VALUE : ((Attr) field1Annotation).priority();
            } else {
                priority1 = ((Elem) field1Annotation).priority();
            }

            field2Annotation = field2.getType().getAnnotation(Elem.class);
            if (field2Annotation == null) {
                field2Annotation = field2.getType().getAnnotation(Attr.class);
                priority2 = field2Annotation == null ? Integer.MAX_VALUE : ((Attr) field2Annotation).priority();
            } else {
                priority2 = ((Elem) field2Annotation).priority();
            }

            return priority1 - priority2;
        });
        return sortedPriority;
    }

    /**
     * 解析map类型xml
     *
     * @param rootElement 需要解析的map节点
     * @param <M>         返回数值
     * @return 返回解析后的map数据
     * @throws ClassNotFoundException -
     * @throws IllegalAccessException -
     * @throws InstantiationException -
     * @throws XmlParserException     -
     */
    private static <M> M parseMapObjFromXml(Element rootElement) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, XmlParserException {
        Map data = new HashMap();
        Iterator mapEntryElems = rootElement.elementIterator();
        Object key;
        Object val;
        while (mapEntryElems.hasNext()) {
            Element next = (Element) mapEntryElems.next();
            Element keyElem = next.element("key");
            Element valueElem = next.element("value");

            String keyClzName = keyElem.attribute("_meta").getValue();
            String valClzName = valueElem.attribute("_meta").getValue();

            Class<?> keyClz = Class.forName(keyClzName);
            Class<?> valClz = Class.forName(valClzName);

            if (isBaseType(keyClz)) {
                key = transferBaseType(String.valueOf(keyElem.getData()), keyClz);
            } else {
                Elem keyClzElem = keyClz.getAnnotation(Elem.class);
                if (keyClzElem == null) continue;
                key = parseElement(keyElem.element(keyClzElem.alias()), keyClz);
            }
            if (isBaseType(valClz)) {
                val = transferBaseType(String.valueOf(valueElem.getData()), valClz);
            } else {
                Elem valClzElem = valClz.getAnnotation(Elem.class);
                if (valClzElem == null) continue;
                val = parseElement(valueElem.element(valClzElem.alias()), valClz);
            }
            data.put(key, val);
        }
        return (M) data;
    }

    /**
     * 从一个object 类型的 list 中解析xml节点
     *
     * @param rootElement 当前解析的根节点
     * @param targetObj   需要解析成的目标实例对象
     * @param nodeName    当前解析的节点名称
     * @param field       当前解析的字段
     * @param setMethod   当前使用的set方法
     * @param entryClass  当前解析的List Entry
     * @param <M>         解析后返回的对象实例
     * @return 如果解析正常，那么返回为M对应的对象实例
     * @throws XmlParserException        -
     * @throws ClassNotFoundException    -
     * @throws IllegalAccessException    -
     * @throws InstantiationException    -
     * @throws InvocationTargetException -
     */
    private static <M> M parseListObjFromXml(Element rootElement, M targetObj, String nodeName, Field field, Method setMethod,
                                             Class entryClass)
            throws XmlParserException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        List data = new ArrayList<>();
        Element listElement = field == null ? rootElement : rootElement.element(nodeName);
        String typeClassName = entryClass == null ? Object.class.getName() : entryClass.getName();
        if (field != null) {
            Type fieldGenericType = field.getGenericType();
            Class<? extends Type> fieldGenericTypeClass = fieldGenericType.getClass();
            Matcher matcher = LIST_TYPE_PATTERN.matcher(fieldGenericType.getTypeName());
            if (matcher.find()) {
                typeClassName = matcher.group(1);
            }
            if (typeClassName == null) return null;

            if (isBaseType(fieldGenericTypeClass)) {
                throw new XmlParserException("暂未实现基础列表类型");
            }
        }

        Iterator elementIterator = listElement.elementIterator();
        while (elementIterator.hasNext()) {
            Element next = (Element) elementIterator.next();
            Class<?> clz = Class.forName(typeClassName);
            Class<?> entryClz = clz;
            if (clz.isInterface()) {
                Attribute meta = next.attribute("_meta");
                if (meta != null) {
                    entryClz = Class.forName(meta.getValue());
                }
            }

            Object entry;
            if (isBaseType(entryClz)) {
                entry = transferBaseType(next.getData(), entryClz);
            } else {
                entry = parseElement(next, entryClz);
            }
            if (entry == null) {
                throw new XmlParserException("暂不支持的操作");
            }
            data.add(entry);
        }

        if (setMethod == null) return (M) data;
        setMethod.invoke(targetObj, data);
        return targetObj;
    }

    /**
     * 将一个Object类型的数据，通过检测转化对应的数值
     *
     * @param data 需要转化的数据对象
     * @param clz  需要转化的数据对象类型
     * @return 返回转化后的数据
     */
    private static Object transferBaseType(Object data, Class<?> clz) {
        if (clz.equals(String.class)) {
            return String.valueOf(data);
        } else if (clz.equals(Integer.class) || clz.equals(int.class)) {
            return Integer.valueOf(String.valueOf(data));
        } else if (clz.equals(Boolean.class) || clz.equals(boolean.class)) {
            return Boolean.valueOf(String.valueOf(data));
        } else if (clz.equals(Byte.class) || clz.equals(byte.class)) {
            return Byte.valueOf(String.valueOf(data));
        } else if (clz.equals(Long.class) || clz.equals(long.class)) {
            return Long.valueOf(String.valueOf(data));
        } else if (clz.equals(Double.class) || clz.equals(double.class)) {
            return Double.valueOf(String.valueOf(data));
        } else if (clz.equals(Character.class) || clz.equals(char.class)) {
            return String.valueOf(data).charAt(0);
        } else if (clz.equals(Short.class) || clz.equals(short.class)) {
            return Short.valueOf(String.valueOf(data));
        } else if (clz.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(Long.valueOf(String.valueOf(data)));
        } else if (clz.equals(BigInteger.class)) {
            return BigInteger.valueOf(Long.valueOf(String.valueOf(data)));
        }
        return String.valueOf(data);
    }

    /**
     * 讲一个对象解析到document中
     *
     * @param element   当前解析的节点
     * @param obj       需要解析的对象
     * @param rootClass 节点类
     * @throws XmlParserException 解析异常
     */
    @SuppressWarnings("unchecked")
    private static void parseClass(Element element, Object obj, Class<?> rootClass) throws XmlParserException {
        try {
            for (Field field : getFields(rootClass)) {
                Method getMethod;
                Elem fieldElem = field.getAnnotation(Elem.class);
                if (fieldElem != null) {

                    getMethod = rootClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    if (getMethod != null) {
                        Class<?> childClass = field.getType();
                        Object retVal = getMethod.invoke(obj);

                        if (retVal != null) {
                            Element childElement = element.addElement(fieldElem.alias());
                            if (Collection.class.isAssignableFrom(retVal.getClass())) {
                                // 处理迭代集合类型
                                processCollection(childElement, (Collection) retVal, getMethod.getGenericReturnType());
                            } else if (Map.class.isAssignableFrom(retVal.getClass())) {
                                processMap(childElement, (Map) retVal);
                            } else {
                                Elem childElemAnnotation = childClass.getAnnotation(Elem.class);
                                if (childElemAnnotation != null) {
                                    Meta meta;
                                    if (childElemAnnotation.interfacing()) {
                                        Method getMetaMethod = field.getType().getMethod("meta");
                                        if (getMetaMethod != null) {
                                            meta = (Meta) getMetaMethod.invoke(retVal);
                                            childElement.addAttribute("_meta", meta.getTypeClz().getName());
                                        }
                                    }
                                }
                                parseClass(childElement, retVal, childClass);
                            }
                        }
                    }
                } else {
                    Attr fieldAttr = field.getAnnotation(Attr.class);
                    if (fieldAttr != null) {
                        getMethod = rootClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                        if (getMethod != null) {
                            Object retVal = getMethod.invoke(obj);
                            if (retVal != null)
                                element.addAttribute(fieldAttr.alias(), retVal.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new XmlParserException(e);
        }
    }

    /**
     * 获得当前类以及父类的所有字段
     *
     * @param clz 需要获得字段field列表的类
     * @return 包含当前类以及父类的所有字段
     */
    private static List<Field> getFields(Class clz) {
        List<Field> fields = new ArrayList<>();
        Field[] declaredFields = clz.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));

        Class superclass = clz.getSuperclass();
        while (superclass != null && !superclass.equals(Object.class)) {
            fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }
        return fields;
    }

    @SuppressWarnings("unchecked")
    private static void processMap(Element childElement, Map retVal) throws XmlParserException {
        Set<Map.Entry<Object, Object>> set = retVal.entrySet();
        for (Map.Entry<Object, Object> entry : set) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (key == null || value == null) {
                // 注意，此处不允许key以及value为空
                continue;
            }

            Element entryElem = childElement.addElement("entry");
            Element keyElem = entryElem.addElement("key");
            keyElem.addAttribute("_meta", key.getClass().getName());
            Element valElem = entryElem.addElement("value");
            valElem.addAttribute("_meta", value.getClass().getName());
            // 添加并解析key
            if (isBaseType(key.getClass())) {
                keyElem.setText(String.valueOf(key));
            } else {
                Elem keyAnnotation = key.getClass().getAnnotation(Elem.class);
                if (keyAnnotation == null) continue;
                parseClass(keyElem.addElement(keyAnnotation.alias()), key, key.getClass());
            }

            // 添加并解析value
            if (isBaseType(value.getClass())) {
                valElem.setText(String.valueOf(value));
            } else {
                Elem valAnnotation = value.getClass().getAnnotation(Elem.class);
                if (valAnnotation == null) continue;
                parseClass(valElem.addElement(valAnnotation.alias()), value, value.getClass());
            }
        }
    }

    private static void processCollection(Element childElement, Collection retVal, Type returnType) throws XmlParserException,
            InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        boolean isObj = false;
        if (returnType != null) {
            Matcher matcher = LIST_TYPE_PATTERN.matcher(returnType.getTypeName());
            if (matcher.find()) {
                String className = matcher.group(1);
                isObj = Object.class.equals(Class.forName(className));
            }
        }
        for (Object retEntry : retVal) {
            Class<?> retEntryClz = retEntry.getClass();
            Elem retEntryClzAnnotation = retEntryClz.getAnnotation(Elem.class);
            if (retEntryClzAnnotation != null) {
                Element iterateElement = childElement.addElement(retEntryClzAnnotation.alias());
                Elem childElemAnnotation = retEntryClz.getAnnotation(Elem.class);
                if (childElemAnnotation != null) {
                    Meta meta;
                    if (childElemAnnotation.interfacing()) {
                        Method getMetaMethod = retEntryClz.getMethod("meta");
                        if (getMetaMethod != null) {
                            meta = (Meta) getMetaMethod.invoke(retEntry);
                            iterateElement.addAttribute("_meta", meta.getTypeClz().getName());
                        }
                    }
                }
                parseClass(iterateElement, retEntry, retEntry.getClass());
            } else if (isBaseType(retEntryClz)) {
                Element element = childElement.addElement(retEntryClz.getSimpleName());
                if (isObj) {
                    element.addAttribute("_meta", retEntryClz.getName());
                }
                element.addText(String.valueOf(retEntry));
            } else {
                Element iterateElement = childElement.addElement(retEntryClz.getSimpleName());
                iterateElement.addAttribute("_meta", retEntryClz.getName());
                if (retEntry instanceof Map) {
                    processMap(iterateElement, (Map) retEntry);
                } else if (retEntry instanceof List) {
                    if (((List) retEntry).size() == 0) continue;

                    iterateElement.attribute("_meta").setValue(retEntryClz.getName() + "<" + ((List) retEntry).get(0).getClass().getName() + ">");
                    processCollection(iterateElement, (Collection) retEntry, null);
                }
            }
        }
    }

    /**
     * 判断类型是否是基本类型，如果是基本类型，那么返回值为true，否则返回值为false
     *
     * @param clz 需要判断的类型class
     * @return 如果是节本数据类型，那么返回值为true，否则返回值为false
     */
    private static boolean isBaseType(Class clz) {
        return clz.isPrimitive() || (clz.equals(String.class) ||
                clz.equals(Integer.class) ||
                clz.equals(Long.class) ||
                clz.equals(Short.class) ||
                clz.equals(Double.class) ||
                clz.equals(Float.class) ||
                clz.equals(Boolean.class) ||
                clz.equals(Byte.class) ||
                clz.equals(Character.class) ||
                clz.equals(BigDecimal.class) ||
                clz.equals(BigInteger.class) ||
                clz.equals(Date.class)
        );
    }
}
