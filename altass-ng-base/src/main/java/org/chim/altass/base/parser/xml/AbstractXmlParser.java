/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml
 * Author: Xuejia
 * Date Time: 2016/12/10 21:25
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.xml.domain.XmlBinding;
import org.chim.altass.base.parser.xml.meta.MetaClassParser;
import org.chim.altass.base.parser.xml.meta.MetaXmlParser;
import org.chim.altass.base.utils.type.ClassUtil;
import org.chim.altass.base.utils.type.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import java.io.*;

/**
 * Class Name: AbstractXmlParser
 * Create Date: 2016/12/10 21:25
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class AbstractXmlParser implements XmlParser {

    private MetaClassParser classParser = new MetaClassParser();
    private MetaXmlParser metaXmlParser = new MetaXmlParser();
    protected XmlBinding binding;

    public AbstractXmlParser() {
    }

    public MetaClassParser getClassParser() {
        return this.classParser;
    }

    private Document readXML(InputStream in) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        return saxReader.read(in);
    }

    private void writeXML(Document document, OutputStream out) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();
    }

    public void setBinding(XmlBinding binding) {
        this.binding = binding;
    }

    public MetaXmlParser getXmlParser() {
        return this.metaXmlParser;
    }

    public XmlBinding getBinding() {
        return this.binding;
    }

    @Override
    public String getXmlString(String xmlPath) throws XmlParserException {
        try {
            Document e = this.readXML(new FileInputStream(xmlPath));
            return e.asXML();
        } catch (FileNotFoundException | DocumentException e) {
            throw new XmlParserException(e);
        }
    }

    @Override
    public String getXmlStringFromClassPath(String xmlPath) throws XmlParserException {
        try {
            Document e = this.readXML(ClassUtil.getURL(xmlPath).openStream());
            return e.asXML();
        } catch (Throwable throwable) {
            throw new XmlParserException(throwable);
        }
    }

    @Override
    public String getXmlString(String bindingFilePath, Object obj) throws XmlParserException {
        try {
            bindingFilePath = ClassUtil.getURL(bindingFilePath).getPath();
            this.setBinding(this.getXmlParser().decode(bindingFilePath));
            Document e = DocumentHelper.createDocument();
            this.rootMappingEncode(e, this.getBinding().getStart(), obj);
            return e.asXML();
        } catch (Throwable throwable) {
            throw new XmlParserException(throwable.toString());
        }
    }

    @Override
    public String getXmlString(Class<?> clazz, Object obj) throws XmlParserException {
        this.setBinding(this.getClassParser().decode(clazz));
        Document document = DocumentHelper.createDocument();
        try {
            this.rootMappingEncode(document, this.getBinding().getStart(), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document.asXML();
    }

    @Override
    public String getFormatXmlString(Class<?> clazz, Object obj) throws XmlParserException {
        this.setBinding(this.getClassParser().decode(clazz));
        Document document = DocumentHelper.createDocument();
        try {
            this.rootMappingEncode(document, this.getBinding().getStart(), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringWriter writer = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter xmlwriter = new XMLWriter(writer, format);

        try {
            xmlwriter.write(document);
            xmlwriter.close();
        } catch (IOException e) {
            throw new XmlParserException(e);
        }

        return writer.toString();
    }

    @Override
    public <T> T read(String bindingFilePath, String xmlPath) throws XmlParserException {
        try {
            bindingFilePath = ClassUtil.getURL(bindingFilePath).getPath();
            Document document = this.readXML(new FileInputStream(xmlPath));
            this.setBinding(this.getXmlParser().decode(bindingFilePath));
            return (T) this.mappingDecode(document.getRootElement(), this.getBinding().getStart());
        } catch (Throwable throwable) {
            throw new XmlParserException(throwable);
        }
    }

    @Override
    public <T> T read(Class<?> clazz, String xmlPath) throws XmlParserException {
        try {
            Document document = this.readXML(new FileInputStream(xmlPath));
            this.setBinding(this.getClassParser().decode(clazz));
            try {
                return (T) this.mappingDecode(document.getRootElement(), this.getBinding().getStart());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException | DocumentException e) {
            throw new XmlParserException(e);
        }
        return null;
    }

    @Override
    public <T> T readClassPathResource(String bindingFilePath, String xmlPath) throws XmlParserException {
        try {
            bindingFilePath = ClassUtil.getURL(bindingFilePath).getPath();
            Document document = this.readXML(ClassUtil.getURL(xmlPath).openStream());
            this.setBinding(this.getXmlParser().decode(bindingFilePath));
            return (T) this.mappingDecode(document.getRootElement(), this.getBinding().getStart());
        } catch (Throwable throwable) {
            throw new XmlParserException(throwable);
        }
    }

    @Override
    public <T> T readClassPathResource(Class<?> clazz, String xmlPath) throws XmlParserException {
        try {
            Document document = this.readXML(ClassUtil.getURL(xmlPath).openStream());
            this.setBinding(this.getClassParser().decode(clazz));
            return (T) this.mappingDecode(document.getRootElement(), this.getBinding().getStart());
        } catch (Throwable throwable) {
            throw new XmlParserException(throwable);
        }
    }

    @Override
    public <T> T readXmlStr(String bindingFilePath, String xmlString) throws XmlParserException {
        return null;
    }

    @Override
    public <T> T readXmlStr(Class<?> clazz, String xmlString) throws XmlParserException {
        if (StringUtil.isEmpty(xmlString)) {
            return null;
        } else {
            try {
                Document document = DocumentHelper.parseText(xmlString);
                this.setBinding(this.getClassParser().decode(clazz));
                try {
                    return (T) this.mappingDecode(document.getRootElement(), this.getBinding().getStart());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (DocumentException e) {
                throw new XmlParserException(e);
            }
        }
        return null;
    }

    @Override
    public void write(String bindingFilePath, Object obj, String xmlPath) throws XmlParserException {
        try {
            bindingFilePath = ClassUtil.getURL(bindingFilePath).getPath();
            this.setBinding(this.getXmlParser().decode(bindingFilePath));
            Document e = DocumentHelper.createDocument();
            this.rootMappingEncode(e, this.getBinding().getStart(), obj);
            this.writeXML(e, new FileOutputStream(xmlPath));
        } catch (Throwable throwable) {
            throw new XmlParserException(throwable);
        }
    }

    @Override
    public void write(Class<?> clazz, Object obj, String xmlPath) throws XmlParserException {
        try {
            this.setBinding(this.getClassParser().decode(clazz));
            Document doc = DocumentHelper.createDocument();
            try {
                this.rootMappingEncode(doc, this.getBinding().getStart(), obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.writeXML(doc, new FileOutputStream(xmlPath));
        } catch (IOException e) {
            throw new XmlParserException(e);
        }
    }

    protected abstract Object mappingDecode(Element element, String mappingName) throws XmlParserException, InstantiationException, Exception;

    protected abstract Object rootMappingEncode(Document doc, String mappingName, Object obj) throws XmlParserException, IllegalAccessException, Exception;
}
