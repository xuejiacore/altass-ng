/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.meta
 * Author: Xuejia
 * Date Time: 2016/12/10 21:28
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
 * Class Name: MetaXmlParser
 * Create Date: 2016/12/10 21:28
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class MetaXmlParser extends MetaParser {

    @Override
    public XmlBinding decode(Object parameter) throws XmlParserException {
        Element elementAnnotation = (Element) ((Class) parameter).getAnnotation(Element.class);
        if (elementAnnotation == null) {
            return null;
        } else {
            this.binding.setStart(elementAnnotation.name());
            try {
                this.mappingDecode((Class) parameter);
            } catch (ClassNotFoundException var4) {
                throw new XmlParserException(var4);
            }

            return this.binding;
        }
    }

    public XmlMapping mappingDecode(Class<?> clazz) throws ClassNotFoundException {
        Element annotation = clazz.getAnnotation(Element.class);
        if (annotation == null) {
            return null;
        } else {
            Field[] fields = ReflectionUtil.getAllDeclaredFields(clazz);
            XmlMapping mapping = new XmlMapping();
            mapping.setName(annotation.name());
            mapping.setClazz(clazz.getName());
            this.textDecode(fields, mapping);
            this.attributeDecode(fields, mapping);
            this.elementDecode(fields, mapping);
            this.binding.addMapping(mapping.getName(), mapping);
            return mapping;
        }
    }

    public void attributeDecode(Field[] fields, XmlMapping mapping) {
        for (Field field : fields) {
            Attribute annotation = field.getAnnotation(Attribute.class);
            if (annotation != null) {
                XmlAttribute attribute = new XmlAttribute();
                attribute.setName(annotation.name());
                attribute.setField(field.getName());
                mapping.addAttribute(attribute);
            }
        }

    }

    public void elementDecode(Field[] fields, XmlMapping mapping) throws ClassNotFoundException {
        for (Field field : fields) {
            Element annotation = field.getAnnotation(Element.class);
            if (annotation != null) {
                XmlElement element = new XmlElement();
                element.setName(annotation.name());
                element.setField(field.getName());
                element.setType("mapping");
                String[] genericTypeNames = ReflectionUtil.getFieldGenericTypeName(field);
                if (genericTypeNames != null) {
                    element.setCollection(true);
                }

                mapping.addElement(element);
                this.mappingDecode(genericTypeNames != null ? Class.forName(genericTypeNames[0]) : field.getType());
            }
        }

    }

    public void textDecode(Field[] fields, XmlMapping mapping) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Text.class)) {
                mapping.setTextField(field.getName());
            }
        }
    }

}
