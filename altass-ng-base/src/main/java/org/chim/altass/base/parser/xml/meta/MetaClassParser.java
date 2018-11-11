/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.meta
 * Author: Xuejia
 * Date Time: 2016/12/10 21:53
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.meta;



import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.xml.annotation.Attribute;
import org.chim.altass.base.parser.xml.annotation.Element;
import org.chim.altass.base.parser.xml.annotation.Text;
import org.chim.altass.base.parser.xml.domain.XmlAttribute;
import org.chim.altass.base.parser.xml.domain.XmlBinding;
import org.chim.altass.base.parser.xml.domain.XmlElement;
import org.chim.altass.base.parser.xml.domain.XmlMapping;
import org.chim.altass.base.utils.ReflectionUtil;

import java.lang.reflect.Field;

/**
 * Class Name: MetaClassParser
 * Create Date: 2016/12/10 21:53
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class MetaClassParser extends MetaParser {
    public MetaClassParser() {
    }

    /**
     * 解析一个类为一个XmlMapping对象实例
     *
     * @param clazz 需要转化的类
     * @return 如果Class是集成自@See org.ike.core.parser.xml.annotation.Element那么解析Element中的配置
     * @throws ClassNotFoundException
     */
    public XmlMapping mappingDecode(Class<?> clazz) throws ClassNotFoundException {
        Element annotation = clazz.getAnnotation(Element.class);
        if (annotation == null) {
            return null;
        } else {
            Field[] fields = ReflectionUtil.getAllDeclaredFields(clazz);
            XmlMapping mapping = new XmlMapping();
            mapping.setName(annotation.name());                                     // Element的name值
            mapping.setClazz(clazz.getName());                                      // Element的Class
            this.textDecode(fields, mapping);                                       // 文本解析

            XmlAttribute versionAttr = new XmlAttribute();
            versionAttr.setName("VERSION");
            versionAttr.setField(annotation.version());
            mapping.addAttribute(versionAttr);
            this.binding.addMapping(mapping.getName(), mapping);                    // 将Class包含的节点信息映射到XmlMapping中
            this.attributeDecode(fields, mapping);                                  // 属性解析
            this.elementDecode(fields, mapping);                                    // 元素节点解析
            return mapping;
        }
    }

    /**
     * 解析类字段中的属性配置
     *
     * @param fields  需要解析的字段集合
     * @param mapping 映射的对象
     */
    public void attributeDecode(Field[] fields, XmlMapping mapping) {
        for (Field field : fields) {
            Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
            if (attributeAnnotation != null) {
                XmlAttribute attribute = new XmlAttribute();
                attribute.setName(attributeAnnotation.name());                  // 设置属性的名称
                attribute.setField(field.getName());                            // 设置属性的字段名
                attribute.setAttributeType(attributeAnnotation.type());         // 设置属性的数据类型
                mapping.addAttribute(attribute);                                // 向mapping中添加当前属性对象
            }
        }
    }

    /**
     * 解析字段中的元素节点配置
     *
     * @param fields  需要解析的字段数组
     * @param mapping 解析需要添加到的mapping
     * @throws ClassNotFoundException
     */
    public void elementDecode(Field[] fields, XmlMapping mapping) throws ClassNotFoundException {
        for (Field field : fields) {
            // 字段被 Element 注解
            Element annotation = field.getAnnotation(Element.class);
            if (annotation != null) {
                XmlElement element = new XmlElement();
                element.setName(annotation.name());                                             // 设置字段的注解名称
                element.setField(field.getName());                                              // 设置字段的名称
                element.setType("mapping");                                                     // 设置元素的类型为mapping
                String[] genericTypeNames = ReflectionUtil.getFieldGenericTypeName(field);      // 获得类型名称
                if (genericTypeNames != null) {
                    // 如果一个集合，那么把当前的节点元素设置为集合类型，在其子节点中，以集合的形式存在
                    element.setCollection(true);
                }

                mapping.addElement(element);
                this.mappingDecode(genericTypeNames != null ? Class.forName(genericTypeNames[0]) : field.getType());
            }
        }

    }

    /**
     * 获取文本类型@Text的字段注解
     *
     * @param fields  需要解析的字段数组
     * @param mapping 需要添加到的mapping
     */
    public void textDecode(Field[] fields, XmlMapping mapping) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Text.class)) {
                mapping.setTextField(field.getName());
            }
        }
    }

    /**
     * 将对象Xml化
     *
     * @param decodeObj 需要编码解码的对象
     * @return 返回Xml化后的XmlBinding对象实例
     * @throws XmlParserException
     */
    @Override
    public XmlBinding decode(Object decodeObj) throws XmlParserException {
        Element annotation = (Element) ((Class) decodeObj).getAnnotation(Element.class);
        if (annotation == null) {
            return null;
        } else {
            this.binding.setStart(annotation.name());
            try {
                this.mappingDecode((Class) decodeObj);
            } catch (ClassNotFoundException var4) {
                throw new XmlParserException(var4);
            }
            return this.binding;
        }
    }
}
