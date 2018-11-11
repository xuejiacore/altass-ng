/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml
 * Author: Xuejia
 * Date Time: 2016/12/12 13:46
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.chim.altass.base.parser.xml.AbstractXmlParser;
import org.chim.altass.base.parser.xml.XMLWriter;
import org.chim.altass.base.parser.xml.domain.XmlAttribute;
import org.chim.altass.base.parser.xml.domain.XmlElement;
import org.chim.altass.base.parser.xml.domain.XmlMapping;
import org.chim.altass.base.parser.xml.domain.XmlNamespace;
import org.chim.altass.base.parser.xml.meta.MetaClassParser;
import org.chim.altass.base.parser.xml.meta.MetaXmlParser;
import org.chim.altass.base.utils.type.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.mortbay.util.ajax.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class Name: XmlParser
 * Create Date: 2016/12/12 13:46
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Xml解析工厂类
 */
public class XmlParser extends AbstractXmlParser {
    private MetaClassParser classParser = new MetaClassParser();
    private MetaXmlParser metaXmlParser = new MetaXmlParser();
    private static XmlParser xmlParser = null;

    private XmlParser() {
    }

    public String attributeNameFilter(String name) {
        return name.substring(name.indexOf(":") + 1);
    }

    /**
     * 获得Xml解析工厂单例
     *
     * @return 返回Xml解析工厂实例
     */
    public static XmlParser getInstance() {
        if (xmlParser == null) {
            synchronized (XmlParser.class) {
                if (xmlParser == null) {
                    xmlParser = new XmlParser();
                }
            }
        }
        return xmlParser;
    }

    /**
     * 解析属性
     *
     * @param element 需要解析的节点
     * @param mapping 解析的映射
     * @param obj     解析的对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public void attributeDecode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (mapping.getAttributes() != null) {
            for (XmlAttribute attribute : mapping.getAttributes()) {
                Object val;
                String valStr = element.attributeValue(attributeNameFilter(attribute.getName()));
                switch (attribute.getAttributeType()) {
                    case TEXT:
                        val = valStr;
                        break;
                    case INT:
                        // TODO：
                        val = valStr == null ? null : Integer.parseInt(valStr);
                        break;
                    case NUMERIC:
                        val = valStr == null ? 0.0 : Double.parseDouble(valStr);
                        break;
                    case DATE:
                        System.err.println("警告：日期数据类型尚未实现");
                        val = valStr;
                        break;
                    case COLLECTION:
                        System.err.println("警告：日期数据类型尚未实现");
                        val = valStr;
                        break;
                    case MAP:
                        val = com.alibaba.fastjson.JSON.parseObject(valStr, Map.class);
                        break;
                    default:
                        val = "";
                }
                try {
                    if (!"VERSION".equals(attribute.getName())) {
                        PropertyUtils.setProperty(obj, attribute.getField(), val);
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessException("属性解码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + e.toString());
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetException(e, "属性解码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + e.toString());
                } catch (NoSuchMethodException e) {
                    throw new NoSuchMethodException("属性解码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + e.toString());
                }
            }
        }

    }

    /**
     * 元素节点解析
     *
     * @param element 需要解析的节点
     * @param mapping 映射对象
     * @param obj     解析的对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public void elementDecode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, ClassNotFoundException {
        if (mapping.getElements() != null) {
            Iterator iterator = mapping.getElements().iterator();

            while (true) {
                XmlElement xmlElement;
                List<Element> elements;
                do {
                    label:
                    do {
                        while (iterator.hasNext()) {
                            xmlElement = (XmlElement) iterator.next();
                            if (xmlElement.isCollection()) {
                                elements = this.getElements(element, xmlElement.getName());
                                continue label;
                            }

                            try {
                                Element e = this.getElement(element, xmlElement.getName());
                                if (e != null) {
                                    BeanUtils.setProperty(obj, xmlElement.getField(), this.mappingDecode(e, xmlElement.getName()));
                                }
                            } catch (IllegalAccessException var10) {
                                throw new IllegalAccessException("结点解码异常[结点" + element.getName() + "]" + var10.toString());
                            } catch (InvocationTargetException var11) {
                                throw new InvocationTargetException(var11, "结点解码异常[结点" + element.getName() + "]" + var11.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        return;
                    } while (elements == null);
                } while (elements.size() <= 0);

                ArrayList<Object> parameters = new ArrayList<>();

                try {
                    for (Object node : elements) {
                        parameters.add(this.mappingDecode((Element) node, xmlElement.getName()));
                    }
                    PropertyUtils.setProperty(obj, xmlElement.getField(), parameters);
                } catch (IllegalAccessException var12) {
                    throw new IllegalAccessException("结点解码异常[结点" + element.getName() + "]" + var12.toString());
                } catch (InvocationTargetException var13) {
                    throw new InvocationTargetException(var13, "结点解码异常[结点" + element.getName() + "]" + var13.toString());
                } catch (NoSuchMethodException var14) {
                    throw new NoSuchMethodException("结点解码异常[结点" + element.getName() + "]" + var14.toString());
                } catch (InstantiationException var15) {
                    throw new InstantiationException("结点解码异常[结点" + element.getName() + "]" + var15.toString());
                } catch (ClassNotFoundException var16) {
                    throw new ClassNotFoundException("结点解码异常[结点" + element.getName() + "]" + var16.toString(), var16);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析映射
     *
     * @param element     节点
     * @param mappingName 映射的名称
     * @return 返回解析后的对象
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Object mappingDecode(Element element, String mappingName) throws Exception {
        XmlMapping mapping = this.binding.getMappings().get(mappingName);
        try {
            Object obj = Class.forName(mapping.getClazz()).newInstance();
            if (mapping.getTextField() != null) {
                PropertyUtils.setProperty(obj, mapping.getTextField(), element.getText());
            }

            this.attributeDecode(element, mapping, obj);
            this.elementDecode(element, mapping, obj);
            return obj;
        } catch (InstantiationException e) {
            throw new InstantiationException("映射解码异常[映射" + mappingName + "]" + e.toString());
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException("映射解码异常[映射" + mappingName + "]" + e.toString());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("映射解码异常[映射" + mappingName + "]" + e.toString(), e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetException(e, "映射解码异常[映射" + mappingName + "]" + e.toString());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("映射解码异常[映射" + mappingName + "]" + e.toString());
        }
    }

    /**
     * 将一个对象根据Mapping规则添加到元素节点中
     *
     * @param element 需要操作的元素节点
     * @param mapping Mapping对象实例
     * @param obj     需要xml化的对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public void attributeEncode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (mapping.getAttributes() != null) {

            for (XmlAttribute attribute : mapping.getAttributes()) {
                try {
                    element.addAttribute(attribute.getName(),
                            "VERSION".equals(attribute.getName()) ? attribute.getField() : BeanUtils.getProperty(obj, attribute.getField()));
                } catch (IllegalAccessException var7) {
                    throw new IllegalAccessException("属性编码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + var7.toString());
                } catch (InvocationTargetException var8) {
                    throw new InvocationTargetException(var8, "属性编码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + var8.toString());
                } catch (NoSuchMethodException var9) {
                    throw new NoSuchMethodException("属性编码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + var9.toString());
                }
            }
        }

    }

    /**
     * 将一个节点元素进行xml化
     *
     * @param element 需要添加到的元素节点
     * @param mapping 映射规则
     * @param obj     需要进行xml化的对象实例
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public void elementEncode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (mapping.getElements() != null) {
            for (XmlElement xmlElement : mapping.getElements()) {
                Object o;
                if (xmlElement.isCollection()) {
                    try {
                        o = PropertyUtils.getProperty(obj, xmlElement.getField());
                        if (o != null) {
                            for (Object object : ((List) o)) {
                                this.mappingEncode(element, xmlElement.getName(), object);
                            }
                        }
                    } catch (IllegalAccessException var12) {
                        throw new IllegalAccessException("结点编码异常[结点" + element.getName() + "]" + var12.toString());
                    } catch (InvocationTargetException var13) {
                        throw new InvocationTargetException(var13, "结点编码异常[结点" + element.getName() + "]" + var13.toString());
                    } catch (NoSuchMethodException var14) {
                        throw new NoSuchMethodException("结点编码异常[结点" + element.getName() + "]" + var14.toString());
                    }
                } else {
                    try {
                        o = PropertyUtils.getProperty(obj, xmlElement.getField());
                        if (o != null) {
                            this.mappingEncode(element, xmlElement.getName(), o);
                        }
                    } catch (IllegalAccessException var9) {
                        throw new IllegalAccessException("结点编码异常[结点" + element.getName() + "]" + var9.toString());
                    } catch (InvocationTargetException var10) {
                        throw new InvocationTargetException(var10, "结点编码异常[结点" + element.getName() + "]" + var10.toString());
                    } catch (NoSuchMethodException var11) {
                        throw new NoSuchMethodException("结点编码异常[结点" + element.getName() + "]" + var11.toString());
                    }
                }
            }
        }
    }

    /**
     * 将一个mapping对象转化
     *
     * @param element     需要添加到的节点
     * @param mappingName 映射名称
     * @param obj         需要转化的对象
     * @return 返回节点元素
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Object mappingEncode(Element element, String mappingName, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        DefaultElement newElement = new DefaultElement(mappingName);
        XmlMapping mapping = this.binding.getMappings().get(mappingName);

        try {
            if (mapping.getNamespaces() != null) {
                for (XmlNamespace namespace : mapping.getNamespaces()) {
                    newElement.addNamespace(namespace.getPrefix(), namespace.getUri());
                }
            }
            if (mapping.getTextField() != null) {
                newElement.setText(BeanUtils.getProperty(obj, mapping.getTextField()));
            }

            this.attributeEncode(newElement, mapping, obj);
            this.elementEncode(newElement, mapping, obj);
            element.add(newElement);
            return newElement;
        } catch (IllegalAccessException var8) {
            throw new IllegalAccessException("映射编码异常[映射" + mappingName + "]" + var8.toString());
        } catch (InvocationTargetException var9) {
            throw new InvocationTargetException(var9, "映射编码异常[映射" + mappingName + "]" + var9.toString());
        } catch (NoSuchMethodException var10) {
            throw new NoSuchMethodException("映射编码异常[映射" + mappingName + "]" + var10.toString());
        }
    }

    /**
     * 将一个文档进行xml化
     *
     * @param doc         文档对象实例
     * @param mappingName 映射名称
     * @param obj         需要操作的对象
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @Override
    public Object rootMappingEncode(Document doc, String mappingName, Object obj) throws Exception {
        Element element = doc.addElement(mappingName);
        XmlMapping mapping = this.binding.getMappings().get(mappingName);
        if (mapping.getNamespaces() != null) {
            for (XmlNamespace namespace : mapping.getNamespaces()) {
                element.addNamespace(namespace.getPrefix(), namespace.getUri());
            }
        }

        try {
            if (mapping.getTextField() != null && BeanUtils.getProperty(obj, mapping.getTextField()) != null) {
                element.setText(BeanUtils.getProperty(obj, mapping.getTextField()));
            }

            this.attributeEncode(element, mapping, obj);
            this.elementEncode(element, mapping, obj);
            return element;
        } catch (IllegalAccessException var8) {
            throw new IllegalAccessException("文档编码异常[映射" + mappingName + "]" + var8.toString());
        } catch (InvocationTargetException var9) {
            throw new InvocationTargetException(var9, "文档编码异常[映射" + mappingName + "]" + var9.toString());
        } catch (NoSuchMethodException var10) {
            throw new NoSuchMethodException("文档编码异常[映射" + mappingName + "]" + var10.toString());
        }
    }

    /**
     * 解析xml文档到一个类中
     *
     * @param bindingFile 绑定文件
     * @param document    xml文档对象
     * @return 将doc实例转化为对象实例
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T decode(String bindingFile, Document document) throws Exception {
        this.binding = this.metaXmlParser.decode(bindingFile);
        return (T) this.mappingDecode(document.getRootElement(), this.binding.getStart());
    }

    /**
     * 解析xml文档到一个指定的类中
     *
     * @param clazz    需要转化的类
     * @param document xml文档对象
     * @return 将doc实例转化为对象实例
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T decode(Class<?> clazz, Document document) throws Exception {
        this.binding = this.classParser.decode(clazz);
        return (T) this.mappingDecode(document.getRootElement(), this.binding.getStart());
    }

    /**
     * 将xml字符串转化为指定的类中
     *
     * @param clazz 需要转化的类
     * @param xml   xml文档对象
     * @return 将字符串转化为对象实例
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T decode(Class<?> clazz, String xml) throws Exception {
        if (StringUtil.isEmpty(xml)) {
            return null;
        } else {
            this.binding = this.classParser.decode(clazz);
            Document document = DocumentHelper.parseText(xml);
            return (T) this.mappingDecode(document.getRootElement(), this.binding.getStart());
        }
    }

    /**
     * 将对象转化为Document对象
     *
     * @param bindingFile 绑定文件的路径
     * @param parameter   需要转化的对象实例
     * @return 返回Document对象实例
     * @throws Exception
     */
    public <T> Document encode(String bindingFile, T parameter) throws Exception {
        this.binding = this.metaXmlParser.decode(bindingFile);
        Document document = DocumentHelper.createDocument();
        this.rootMappingEncode(document, this.binding.getStart(), parameter);
        return document;
    }

    /**
     * 将对象转化为Document对象
     *
     * @param clazz     需要转化的类
     * @param parameter 需要转化的对象实例
     * @return 返回Document对象实例
     * @throws Exception
     */
    public <T> Document encode(Class<?> clazz, T parameter) throws Exception {
        this.binding = this.classParser.decode(clazz);
        Document document = DocumentHelper.createDocument();
        this.rootMappingEncode(document, this.binding.getStart(), parameter);
        return document;
    }

    /**
     * 从某一个元素节点中获得另外一个节点
     *
     * @param element 节点源
     * @param path    需要获取的节点路径
     * @return 返回路径对应的节点
     */
    private Element getElement(Element element, String path) {
        return (Element) element.selectSingleNode(elementNameFilter(path));
    }

    /**
     * 从某一个节点元素中获取一个节点集合
     *
     * @param element 节点源
     * @param path    节点路径
     * @return 返回节点源对应的节点集合
     */
    @SuppressWarnings("unchecked")
    private List<Element> getElements(Element element, String path) {
        return element.selectNodes(elementNameFilter(path));
    }

    /**
     * 获得元素过滤
     *
     * @param name
     * @return
     */
    private static String elementNameFilter(String name) {
        return "*[local-name()=\'" + name.substring(name.indexOf(":") + 1) + "\']";
    }

    /**
     * 从一个xml文件中读取Document对象
     *
     * @param xmlPath xml文件的文件路径
     * @return 返回xml转化后的Document对象
     * @throws DocumentException
     */
    public static Document readXML(String xmlPath) throws DocumentException {
        Document document = null;
        if (xmlPath != null) {
            SAXReader saxReader = new SAXReader();

            try {
                document = saxReader.read(new File(xmlPath));
            } catch (DocumentException var4) {
                throw new DocumentException("读取xml文档出错[文档" + xmlPath + "]" + var4.toString(), var4);
            }
        }

        return document;
    }

    /**
     * 将一个Document对象写入到指定的文件中
     *
     * @param document 需要写入的Document对象实例
     * @param xmlPath  xml文件路径
     * @throws IOException
     */
    public static void writeXML(Document document, String xmlPath) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");

        try {
            XMLWriter e = new XMLWriter(new FileOutputStream(xmlPath), format);
            e.write(document);
            e.close();
        } catch (IOException var4) {
            throw new IOException("写入xml文档出错[文档" + xmlPath + "]" + var4.toString(), var4);
        }
    }

    /**
     * 获得默认的类加载器
     *
     * @return 返回XmlParser的类加载器
     * @throws Throwable
     */
    public static ClassLoader getDefaultClassLoader() throws Throwable {
        ClassLoader classLoader;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var2) {
            throw new Throwable(var2.toString(), var2);
        }

        if (classLoader == null) {
            classLoader = XmlParser.class.getClassLoader();
        }

        return classLoader;
    }

    /**
     * 获得资源位置的URL
     *
     * @param resourceLocation 资源的路径
     * @return 资源位置URL
     * @throws Throwable
     */
    public static URL getURL(String resourceLocation) throws Throwable {
        if (resourceLocation.startsWith("/")) {
            resourceLocation = resourceLocation.substring(1);
        }

        return getDefaultClassLoader().getResource(resourceLocation);
    }

    /**
     * 获得资源路径
     *
     * @param resourceLocation 资源的位置
     * @return 资源位置URL
     * @throws Throwable
     */
    public static String getPath(String resourceLocation) throws Throwable {
        return getURL(resourceLocation).getPath();
    }

    /**
     * 获得资源路径对应的资源文件对象实例
     *
     * @param resourceLocation 资源的位置
     * @return 资源位置URL
     * @throws Throwable
     */
    public static File getFile(String resourceLocation) throws Throwable {
        return new File(getURL(resourceLocation).getFile());
    }
}
