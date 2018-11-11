/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml
 * Author: Xuejia
 * Date Time: 2016/12/10 18:42
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml;


import org.chim.altass.base.exception.XmlParserException;

/**
 * Class Name: XmlParser
 * Create Date: 2016/12/10 18:42
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface XmlParser {
    /**
     * 获得某个xml路径下的xml内容
     *
     * @param xmlPath xml文件的路径
     * @return 返回xml的内容
     * @throws XmlParserException
     */
    String getXmlString(String xmlPath) throws XmlParserException;

    /**
     * 从工程Xml路径中获取Xml内容
     *
     * @param xmlPath 工程xml路径
     * @return 返回xml文件中的内容
     * @throws XmlParserException
     */
    String getXmlStringFromClassPath(String xmlPath) throws XmlParserException;

    /**
     * 根据绑定的文件路径获得xml文件的内容
     *
     * @param bindingFilePath 绑定xml文件路径
     * @param obj
     * @return
     * @throws XmlParserException
     */
    String getXmlString(String bindingFilePath, Object obj) throws XmlParserException;

    String getXmlString(Class<?> clazz, Object obj) throws XmlParserException;

    String getFormatXmlString(Class<?> clazz, Object obj) throws XmlParserException;

    <T> T read(String bindingFilePath, String xmlPath) throws XmlParserException;

    <T> T read(Class<?> clazz, String xmlPath) throws XmlParserException;

    <T> T readClassPathResource(String bindingFilePath, String xmlPath) throws XmlParserException;

    <T> T readClassPathResource(Class<?> clazz, String xmlPath) throws XmlParserException;

    <T> T readXmlStr(String bindingFilePath, String xmlString) throws XmlParserException;

    <T> T readXmlStr(Class<?> clazz, String xmlString) throws XmlParserException;

    void write(String bindingFilePath, Object obj, String xmlPath) throws XmlParserException;

    void write(Class<?> clazz, Object obj, String xmlPath) throws XmlParserException;
}
