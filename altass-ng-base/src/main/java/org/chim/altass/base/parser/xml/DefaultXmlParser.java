/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml
 * Author: Xuejia
 * Date Time: 2016/12/10 22:14
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.xml.domain.XmlAttribute;
import org.chim.altass.base.parser.xml.domain.XmlElement;
import org.chim.altass.base.parser.xml.domain.XmlMapping;
import org.chim.altass.base.parser.xml.domain.XmlNamespace;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class Name: DefaultXmlParser
 * Create Date: 2016/12/10 22:14
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DefaultXmlParser extends AbstractXmlParser {

    /**
     * 创建一个默认的Xml解析器
     */
    public DefaultXmlParser() {
    }

    /**
     * 将一个映射名称转化为一个对象
     *
     * @param element     xml节点
     * @param mappingName
     * @return
     * @throws XmlParserException
     */
    @Override
    public Object mappingDecode(Element element, String mappingName) throws XmlParserException {
        XmlMapping mapping = this.getBinding().getMappings().get(mappingName);
        try {
            Object obj = Class.forName(mapping.getClazz()).newInstance();
            if (mapping.getTextField() != null) {
                PropertyUtils.setProperty(obj, mapping.getTextField(), element.getText());
            }

            this.attributeDecode(element, mapping, obj);
            this.elementDecode(element, mapping, obj);
            return obj;
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException e) {
            throw new XmlParserException("映射解码异常[映射" + mappingName + "]" + e.toString());
        } catch (ClassNotFoundException e) {
            throw new XmlParserException("映射解码异常[映射" + mappingName + "]" + e.toString(), e);
        } catch (InvocationTargetException e) {
            throw new XmlParserException("映射解码异常[映射" + mappingName + "]" + e.getTargetException().toString());
        }
    }

    @Override
    public Object rootMappingEncode(Document doc, String mappingName, Object obj) throws XmlParserException {
        Element element = doc.addElement(mappingName);
        XmlMapping mapping = this.getBinding().getMappings().get(mappingName);
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
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new XmlParserException("文档编码异常[映射" + mappingName + "]" + e.toString());
        } catch (InvocationTargetException e) {
            throw new XmlParserException("文档编码异常[映射" + mappingName + "]" + e.getTargetException().toString());
        }
    }

    public String attributeNameFilter(String name) {
        return name.substring(name.indexOf(":") + 1);
    }

    public String elementNameFilter(String name) {
        return "*[local-name()=\'" + name.substring(name.indexOf(":") + 1) + "\']";
    }

    private Element getElement(Element element, String path) {
        return (Element) element.selectSingleNode(this.elementNameFilter(path));
    }

    private List<Element> getElements(Element element, String path) {
        return element.selectNodes(this.elementNameFilter(path));
    }

    public void attributeDecode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (mapping.getAttributes() != null) {
            for (XmlAttribute attribute : mapping.getAttributes()) {
                try {
                    Object val;
                    String valStr = element.attributeValue(this.attributeNameFilter(attribute.getName()));
                    switch (attribute.getAttributeType()) {
                        case TEXT:
                            val = valStr;
                            break;
                        case INT:
                            val = valStr == null ? 0 : Integer.parseInt(valStr);
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
                    PropertyUtils.setProperty(obj, attribute.getField(), val);
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

    public void elementDecode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, XmlParserException {
        if (mapping.getElements() != null) {
            Iterator iterator = mapping.getElements().iterator();
            while (true) {
                XmlElement XmlElement;
                List elementList;
                do {
                    label:
                    do {
                        while (iterator.hasNext()) {
                            XmlElement = (XmlElement) iterator.next();
                            if (XmlElement.isCollection()) {
                                elementList = this.getElements(element, XmlElement.getName());
                                continue label;
                            }

                            try {
                                Element ele = this.getElement(element, XmlElement.getName());
                                if (ele != null) {
                                    BeanUtils.setProperty(obj, XmlElement.getField(), this.mappingDecode(ele, XmlElement.getName()));
                                }
                            } catch (IllegalAccessException e) {
                                throw new IllegalAccessException("结点解码异常[结点" + element.getName() + "]" + e.toString());
                            } catch (InvocationTargetException e) {
                                throw new InvocationTargetException(e, "结点解码异常[结点" + element.getName() + "]" + e.toString());
                            }
                        }
                        return;
                    } while (elementList == null);
                } while (elementList.size() <= 0);

                ArrayList parameters = new ArrayList();
                try {
                    for (Object node : elementList) {
                        parameters.add(this.mappingDecode((Element) node, XmlElement.getName()));
                    }
                    PropertyUtils.setProperty(obj, XmlElement.getField(), parameters);
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessException("结点解码异常[结点" + element.getName() + "]" + e.toString());
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetException(e, "结点解码异常[结点" + element.getName() + "]" + e.toString());
                } catch (NoSuchMethodException e) {
                    throw new NoSuchMethodException("结点解码异常[结点" + element.getName() + "]" + e.toString());
                }
            }
        }
    }

    public void attributeEncode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (mapping.getAttributes() != null) {
            for (XmlAttribute attribute : mapping.getAttributes()) {
                try {
                    element.addAttribute(attribute.getName(), BeanUtils.getProperty(obj, attribute.getField()));
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessException("属性编码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + e.toString());
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetException(e, "属性编码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + e.toString());
                } catch (NoSuchMethodException e) {
                    throw new NoSuchMethodException("属性编码异常[结点" + element.getName() + "的" + attribute.getName() + "属性]" + e.toString());
                }
            }
        }

    }

    public void elementEncode(Element element, XmlMapping mapping, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (mapping.getElements() != null) {
            Iterator iterator = mapping.getElements().iterator();
            while (true) {
                while (iterator.hasNext()) {
                    XmlElement XmlElement = (XmlElement) iterator.next();
                    Object val;
                    if (XmlElement.isCollection()) {
                        try {
                            val = PropertyUtils.getProperty(obj, XmlElement.getField());
                            if (val != null) {
                                for (Object object : ((List) val)) {
                                    this.mappingEncode(element, XmlElement.getName(), object);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            throw new IllegalAccessException("结点编码异常[结点" + element.getName() + "]" + e.toString());
                        } catch (InvocationTargetException e) {
                            throw new InvocationTargetException(e, "结点编码异常[结点" + element.getName() + "]" + e.toString());
                        } catch (NoSuchMethodException e) {
                            throw new NoSuchMethodException("结点编码异常[结点" + element.getName() + "]" + e.toString());
                        }
                    } else {
                        try {
                            val = PropertyUtils.getProperty(obj, XmlElement.getField());
                            if (val != null) {
                                this.mappingEncode(element, XmlElement.getName(), val);
                            }
                        } catch (IllegalAccessException e) {
                            throw new IllegalAccessException("结点编码异常[结点" + element.getName() + "]" + e.toString());
                        } catch (InvocationTargetException e) {
                            throw new InvocationTargetException(e, "结点编码异常[结点" + element.getName() + "]" + e.toString());
                        } catch (NoSuchMethodException e) {
                            throw new NoSuchMethodException("结点编码异常[结点" + element.getName() + "]" + e.toString());
                        }
                    }
                }

                return;
            }
        }
    }

    public Object mappingEncode(Element element, String mappingName, Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        DefaultElement newElement = new DefaultElement(mappingName);
        XmlMapping mapping = this.getBinding().getMappings().get(mappingName);

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
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException("映射编码异常[映射" + mappingName + "]" + e.toString());
        } catch (InvocationTargetException e) {
            throw new InvocationTargetException(e, "映射编码异常[映射" + mappingName + "]" + e.toString());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("映射编码异常[映射" + mappingName + "]" + e.toString());
        }
    }

}
