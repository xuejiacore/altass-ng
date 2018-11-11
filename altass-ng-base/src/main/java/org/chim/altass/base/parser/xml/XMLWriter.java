/**
 * Project: x-framework
 * Package Name: org.ike.core.parser
 * Author: Xuejia
 * Date Time: 2016/12/10 15:24
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.NamespaceStack;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.*;
import java.util.*;

/**
 * Class Name: XMLWriter
 * Create Date: 2016/12/10 15:24
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * XML写入器
 */
public class XMLWriter extends XMLFilterImpl implements LexicalHandler {
    protected static final String[] LEXICAL_HANDLER_NAMES = new String[]{
            "http://xml.org/sax/properties/lexical-handler",
            "http://xml.org/sax/handlers/LexicalHandler"
    };                                                                                  // xml命名空间
    protected static final OutputFormat DEFAULT_FORMAT = new OutputFormat();            // 输出格式化
    private boolean resolveEntityRefs;                                                  //
    protected int lastOutputNodeType;                                                   //
    private boolean lastElementClosed;                                                  //
    protected boolean preserve;                                                         //
    protected Writer writer;                                                            //
    private NamespaceStack namespaceStack;                                              //
    private OutputFormat format;                                                        //
    private boolean escapeText;                                                         //
    private int indentLevel;                                                            //
    private StringBuffer buffer;                                                        //
    private boolean charsAdded;                                                         //
    private char lastChar;                                                              //
    private boolean autoFlush;                                                          //
    private LexicalHandler lexicalHandler;                                              //
    private boolean showCommentsInDTDs;                                                 //
    private boolean inDTD;                                                              //
    private Map<String, Object> namespacesMap;                                                          //
    private int maximumAllowedCharacter;                                                //

    /**
     * 创建一个xml写入器
     *
     * @param writer 写入对象
     */
    public XMLWriter(Writer writer) {
        this(writer, DEFAULT_FORMAT);
    }

    /**
     * 使用自定义的OutputFormat
     *
     * @param writer 写入器
     * @param format 自定义的OutputFormat
     */
    public XMLWriter(Writer writer, OutputFormat format) {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.writer = writer;
        this.format = format;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    /**
     * c黄建一个默认的写入器
     */
    public XMLWriter() {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = DEFAULT_FORMAT;
        this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    /**
     * 指定输出流写入Xml
     *
     * @param out 输出流
     * @throws UnsupportedEncodingException
     */
    public XMLWriter(OutputStream out) throws UnsupportedEncodingException {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = DEFAULT_FORMAT;
        this.writer = this.createWriter(out, this.format.getEncoding());
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    /**
     * 指定输出流以及输出格式化
     *
     * @param out    输出流实例
     * @param format 格式化
     * @throws UnsupportedEncodingException
     */
    public XMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = format;
        this.writer = this.createWriter(out, format.getEncoding());
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    /**
     * 使用默认的标准输出，指定格式输出
     *
     * @param format 格式化输出实例
     * @throws UnsupportedEncodingException
     */
    public XMLWriter(OutputFormat format) throws UnsupportedEncodingException {
        this.resolveEntityRefs = true;
        this.lastElementClosed = false;
        this.preserve = false;
        this.namespaceStack = new NamespaceStack();
        this.escapeText = true;
        this.indentLevel = 0;
        this.buffer = new StringBuffer();
        this.charsAdded = false;
        this.format = format;
        this.writer = this.createWriter(System.out, format.getEncoding());
        this.autoFlush = true;
        this.namespaceStack.push(Namespace.NO_NAMESPACE);
    }

    /**
     * 设置写入实例
     *
     * @param writer 写入实例
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
        this.autoFlush = false;             // 禁用自动清出缓存
    }

    /**
     * 设置输出流实例
     *
     * @param out 输出实例
     * @throws UnsupportedEncodingException
     */
    public void setOutputStream(OutputStream out) throws UnsupportedEncodingException {
        this.writer = this.createWriter(out, this.format.getEncoding());
        this.autoFlush = true;              // 打开自动清出缓存
    }

    /**
     * 是否转义
     *
     * @return 如果转义，那么返回值为true，否则返回值为false
     */
    public boolean isEscapeText() {
        return this.escapeText;
    }

    public void setEscapeText(boolean escapeText) {
        this.escapeText = escapeText;
    }

    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }

    /**
     * 获得最大允许字符数
     *
     * @return 最大允许字符数
     */
    public int getMaximumAllowedCharacter() {
        if (this.maximumAllowedCharacter == 0) {
            this.maximumAllowedCharacter = this.defaultMaximumAllowedCharacter();
        }

        return this.maximumAllowedCharacter;
    }

    public void setMaximumAllowedCharacter(int maximumAllowedCharacter) {
        this.maximumAllowedCharacter = maximumAllowedCharacter;
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }

    public void println() throws IOException {
        this.writer.write(this.format.getLineSeparator());
    }

    /**
     * 写入一个属性
     *
     * @param attribute 需要写入的属性
     * @throws IOException
     */
    public void write(Attribute attribute) throws IOException {
        this.writeAttribute(attribute);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写文档
     *
     * @param doc 文档对象实例
     * @throws IOException
     */
    public void write(Document doc) throws IOException {
        this.writeDeclaration();
        if (doc.getDocType() != null) {
            this.indent();
            this.writeDocType(doc.getDocType());
        }

        int i = 0;

        // 将文档中的节点对象写入文档中
        for (int size = doc.nodeCount(); i < size; ++i) {
            Node node = doc.node(i);
            this.writeNode(node);
        }

        this.writePrintln();
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入一个元素
     *
     * @param element 元素
     * @throws IOException
     */
    public void write(Element element) throws IOException {
        this.writeElement(element);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入字符数据
     *
     * @param cdata 字符数据
     * @throws IOException
     */
    public void write(CDATA cdata) throws IOException {
        this.writeCDATA(cdata.getText());
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入注释
     *
     * @param comment 注释
     * @throws IOException
     */
    public void write(Comment comment) throws IOException {
        this.writeComment(comment.getText());
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入文档类型
     *
     * @param docType 文档类型
     * @throws IOException
     */
    public void write(DocumentType docType) throws IOException {
        this.writeDocType(docType);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入一个实体
     *
     * @param entity 实体实例
     * @throws IOException
     */
    public void write(Entity entity) throws IOException {
        this.writeEntity(entity);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入命名空间
     *
     * @param namespace 命名空间
     * @throws IOException
     */
    public void write(Namespace namespace) throws IOException {
        this.writeNamespace(namespace);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入处理指令
     *
     * @param processingInstruction 处理指令时调用的实例
     * @throws IOException
     */
    public void write(ProcessingInstruction processingInstruction) throws IOException {
        this.writeProcessingInstruction(processingInstruction);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入文本
     *
     * @param text 文本内容
     * @throws IOException
     */
    public void write(String text) throws IOException {
        this.writeString(text);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入文本内容
     *
     * @param text 文本内容
     * @throws IOException
     */
    public void write(Text text) throws IOException {
        this.writeString(text.getText());
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入一个节点
     *
     * @param node 节点
     * @throws IOException
     */
    public void write(Node node) throws IOException {
        this.writeNode(node);
        if (this.autoFlush) {
            this.flush();
        }

    }

    /**
     * 写入一个对象
     *
     * @param object 对象
     * @throws IOException
     */
    public void write(Object object) throws IOException {
        if (object instanceof Node) {
            this.write((Node) object);
        } else if (object instanceof String) {
            this.write((String) object);
        } else if (object instanceof List) {
            List list = (List) object;
            int i = 0;

            for (int size = list.size(); i < size; ++i) {
                this.write(list.get(i));
            }
        } else if (object != null) {
            throw new IOException("Invalid object: " + object);
        }

    }

    /**
     * 写入一个开标签
     *
     * @param element 需要写入的元素
     * @throws IOException
     */
    public void writeOpen(Element element) throws IOException {
        this.writer.write("<");
        this.writer.write(element.getQualifiedName());
        this.writeAttributes(element);
        this.writer.write(">");
    }

    /**
     * 写入一个闭标签
     *
     * @param element 写入的元素
     * @throws IOException
     */
    public void writeClose(Element element) throws IOException {
        this.writeClose(element.getQualifiedName());
    }

    /**
     * 根据标签名写入一个结束标签
     *
     * @param qualifiedName 标签名称
     * @throws IOException
     */
    protected void writeClose(String qualifiedName) throws IOException {
        this.writer.write("</");
        this.writer.write(qualifiedName);
        this.writer.write(">");
    }

    /**
     * 根据输入源获取词法处理
     *
     * @param source 输入源
     * @throws IOException
     * @throws SAXException
     */
    public void parse(InputSource source) throws IOException, SAXException {
        this.installLexicalHandler();
        // 使用父类的词法解析
        super.parse(source);
    }

    /**
     * 设置配置属性
     *
     * @param name  属性名
     * @param value 属性值
     * @throws SAXNotRecognizedException
     * @throws SAXNotSupportedException
     */
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        for (String LEXICAL_HANDLER_NAME : LEXICAL_HANDLER_NAMES) {
            if (LEXICAL_HANDLER_NAME.equals(name)) {
                this.setLexicalHandler((LexicalHandler) value);
                return;
            }
        }

        super.setProperty(name, value);
    }

    /**
     * 根dui'yi据属性名称获取属性值
     *
     * @param name 属性名称
     * @return 属性名称对应的属性值
     * @throws SAXNotRecognizedException
     * @throws SAXNotSupportedException
     */
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        for (String LEXICAL_HANDLER_NAME : LEXICAL_HANDLER_NAMES) {
            if (LEXICAL_HANDLER_NAME.equals(name)) {
                return this.getLexicalHandler();
            }
        }

        return super.getProperty(name);
    }

    /**
     * 设置词法解析处理
     *
     * @param handler 词法解析处理对象实例
     */
    public void setLexicalHandler(LexicalHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Null lexical handler");
        } else {
            this.lexicalHandler = handler;
        }
    }

    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
        try {
            this.writeDeclaration();
            super.startDocument();
        } catch (IOException var2) {
            this.handleException(var2);
        }

    }

    public void endDocument() throws SAXException {
        super.endDocument();
        if (this.autoFlush) {
            try {
                this.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 命名空间
     *
     * @param prefix 命名空间prefix名称
     * @param uri    命名空间uri
     * @throws SAXException
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this.namespacesMap == null) {
            this.namespacesMap = new HashMap<>();
        }

        this.namespacesMap.put(prefix, uri);
        super.startPrefixMapping(prefix, uri);
    }

    /**
     * 命名空间结束
     *
     * @param prefix 命名空间前缀
     * @throws SAXException
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        super.endPrefixMapping(prefix);
    }

    /**
     * 元素开始
     *
     * @param namespaceURI 命名空间uri
     * @param localName    本地名称
     * @param qName        节点名称
     * @param attributes   属性
     * @throws SAXException
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            this.charsAdded = false;
            this.writePrintln();
            this.indent();
            this.writer.write("<");
            this.writer.write(qName);
            this.writeNamespaces();
            this.writeAttributes(attributes);
            this.writer.write(">");
            ++this.indentLevel;
            this.lastOutputNodeType = 1;
            this.lastElementClosed = false;
            super.startElement(namespaceURI, localName, qName, attributes);
        } catch (IOException var6) {
            this.handleException(var6);
        }

    }

    /**
     * 元素结束
     *
     * @param namespaceURI 命名空间uri
     * @param localName    本地名称
     * @param qName        节点名称
     * @throws SAXException
     */
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            this.charsAdded = false;
            --this.indentLevel;
            if (this.lastElementClosed) {
                this.writePrintln();
                this.indent();
            }

            boolean isNotEmptyEle = true;
            if (isNotEmptyEle) {
                this.writeClose(qName);
            } else {
                this.writeEmptyElementClose(qName);
            }

            this.lastOutputNodeType = 1;
            this.lastElementClosed = true;
            super.endElement(namespaceURI, localName, qName);
        } catch (IOException var5) {
            this.handleException(var5);
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (ch != null && ch.length != 0 && length > 0) {
            try {
                String entity = String.valueOf(ch, start, length);
                if (this.escapeText) {
                    entity = this.escapeElementEntities(entity);
                }

                if (!this.format.isTrimText()) {
                    this.writer.write(entity);
                } else {
                    if (this.lastOutputNodeType == 3 && !this.charsAdded) {
                        this.writer.write(32);
                    } else if (this.charsAdded && Character.isWhitespace(this.lastChar)) {
                        this.writer.write(32);
                    } else if (this.lastOutputNodeType == 1 && this.format.isPadText() && this.lastElementClosed && Character.isWhitespace(ch[0])) {
                        this.writer.write(" ");
                    }

                    String delimiter = "";

                    for (StringTokenizer tokens = new StringTokenizer(entity); tokens.hasMoreTokens(); delimiter = " ") {
                        this.writer.write(delimiter);
                        this.writer.write(tokens.nextToken());
                    }
                }

                this.charsAdded = true;
                this.lastChar = ch[start + length - 1];
                this.lastOutputNodeType = 3;
                super.characters(ch, start, length);
            } catch (IOException var7) {
                this.handleException(var7);
            }

        }
    }

    /**
     * 忽略空白符
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        super.ignorableWhitespace(ch, start, length);
    }

    /**
     *
     * @param target
     * @param data
     * @throws SAXException
     */
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            this.indent();
            this.writer.write("<?");
            this.writer.write(target);
            this.writer.write(" ");
            this.writer.write(data);
            this.writer.write("?>");
            this.writePrintln();
            this.lastOutputNodeType = 7;
            super.processingInstruction(target, data);
        } catch (IOException var4) {
            this.handleException(var4);
        }

    }

    public void notationDecl(String name, String publicID, String systemID) throws SAXException {
        super.notationDecl(name, publicID, systemID);
    }

    public void unparsedEntityDecl(String name, String publicID, String systemID, String notationName) throws SAXException {
        super.unparsedEntityDecl(name, publicID, systemID, notationName);
    }

    public void startDTD(String name, String publicID, String systemID) throws SAXException {
        this.inDTD = true;

        try {
            this.writeDocType(name, publicID, systemID);
        } catch (IOException var5) {
            this.handleException(var5);
        }

        if (this.lexicalHandler != null) {
            this.lexicalHandler.startDTD(name, publicID, systemID);
        }

    }

    public void endDTD() throws SAXException {
        this.inDTD = false;
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endDTD();
        }

    }

    public void startCDATA() throws SAXException {
        try {
            this.writer.write("<![CDATA[");
        } catch (IOException var2) {
            this.handleException(var2);
        }

        if (this.lexicalHandler != null) {
            this.lexicalHandler.startCDATA();
        }

    }

    public void endCDATA() throws SAXException {
        try {
            this.writer.write("]]>");
        } catch (IOException var2) {
            this.handleException(var2);
        }

        if (this.lexicalHandler != null) {
            this.lexicalHandler.endCDATA();
        }

    }

    public void startEntity(String name) throws SAXException {
        try {
            this.writeEntityRef(name);
        } catch (IOException var3) {
            this.handleException(var3);
        }

        if (this.lexicalHandler != null) {
            this.lexicalHandler.startEntity(name);
        }

    }

    public void endEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endEntity(name);
        }

    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.showCommentsInDTDs || !this.inDTD) {
            try {
                this.charsAdded = false;
                this.writeComment(new String(ch, start, length));
            } catch (IOException var5) {
                this.handleException(var5);
            }
        }

        if (this.lexicalHandler != null) {
            this.lexicalHandler.comment(ch, start, length);
        }

    }

    protected void writeElement(Element element) throws IOException {
        int size = element.nodeCount();
        String qualifiedName = element.getQualifiedName();
        this.writePrintln();
        this.indent();
        this.writer.write("<");
        this.writer.write(qualifiedName);
        int previouslyDeclaredNamespaces = this.namespaceStack.size();
        Namespace ns = element.getNamespace();
        if (this.isNamespaceDeclaration(ns)) {
            this.namespaceStack.push(ns);
            this.writeNamespace(ns);
        }

        boolean textOnly = true;

        for (int i = 0; i < size; ++i) {
            Node node = element.node(i);
            if (node instanceof Namespace) {
                Namespace additional = (Namespace) node;
                if (this.isNamespaceDeclaration(additional)) {
                    this.namespaceStack.push(additional);
                    this.writeNamespace(additional);
                }
            } else if (node instanceof Element) {
                textOnly = false;
            } else if (node instanceof Comment) {
                textOnly = false;
            }
        }

        this.writeAttributes(element);
        this.lastOutputNodeType = 1;
        if (size <= 0) {
            this.writeEmptyElementClose(qualifiedName);
        } else {
            this.writer.write(">");
            if (textOnly) {
                this.writeElementContent(element);
            } else {
                ++this.indentLevel;
                this.writeElementContent(element);
                --this.indentLevel;
                this.writePrintln();
                this.indent();
            }

            this.writer.write("</");
            this.writer.write(qualifiedName);
            this.writer.write(">");
        }

        while (this.namespaceStack.size() > previouslyDeclaredNamespaces) {
            this.namespaceStack.pop();
        }

        this.lastOutputNodeType = 1;
    }

    protected final boolean isElementSpacePreserved(Element element) {
        Attribute attr = element.attribute("space");
        boolean preserveFound = this.preserve;
        if (attr != null) {
            if ("xml".equals(attr.getNamespacePrefix()) && "preserve".equals(attr.getText())) {
                preserveFound = true;
            } else {
                preserveFound = false;
            }
        }

        return preserveFound;
    }

    protected void writeElementContent(Element element) throws IOException {
        boolean trim = this.format.isTrimText();
        boolean oldPreserve = this.preserve;
        if (trim) {
            this.preserve = this.isElementSpacePreserved(element);
            trim = !this.preserve;
        }

        if (trim) {
            Text lastTextNode = null;
            StringBuffer i = null;
            boolean size = true;
            int node = 0;

            for (int txt = element.nodeCount(); node < txt; ++node) {
                Node lastTextChar = element.node(node);
                if (lastTextChar instanceof Text) {
                    if (lastTextNode == null) {
                        lastTextNode = (Text) lastTextChar;
                    } else {
                        if (i == null) {
                            i = new StringBuffer(lastTextNode.getText());
                        }

                        i.append(((Text) lastTextChar).getText());
                    }
                } else {
                    char lastTextChar1;
                    if (!size && this.format.isPadText()) {
                        lastTextChar1 = 97;
                        if (i != null) {
                            lastTextChar1 = i.charAt(0);
                        } else if (lastTextNode != null) {
                            lastTextChar1 = lastTextNode.getText().charAt(0);
                        }

                        if (Character.isWhitespace(lastTextChar1)) {
                            this.writer.write(" ");
                        }
                    }

                    if (lastTextNode != null) {
                        if (i != null) {
                            this.writeString(i.toString());
                            i = null;
                        } else {
                            this.writeString(lastTextNode.getText());
                        }

                        if (this.format.isPadText()) {
                            lastTextChar1 = 97;
                            if (i != null) {
                                lastTextChar1 = i.charAt(i.length() - 1);
                            } else if (lastTextNode != null) {
                                String txt1 = lastTextNode.getText();
                                lastTextChar1 = txt1.charAt(txt1.length() - 1);
                            }

                            if (Character.isWhitespace(lastTextChar1)) {
                                this.writer.write(" ");
                            }
                        }

                        lastTextNode = null;
                    }

                    size = false;
                    this.writeNode(lastTextChar);
                }
            }

            if (lastTextNode != null) {
                if (!size && this.format.isPadText()) {
                    boolean var15 = true;
                    char var16;
                    if (i != null) {
                        var16 = i.charAt(0);
                    } else {
                        var16 = lastTextNode.getText().charAt(0);
                    }

                    if (Character.isWhitespace(var16)) {
                        this.writer.write(" ");
                    }
                }

                if (i != null) {
                    this.writeString(i.toString());
                    i = null;
                } else {
                    this.writeString(lastTextNode.getText());
                }

                lastTextNode = null;
            }
        } else {
            Node var12 = null;
            int var14 = 0;

            for (int var13 = element.nodeCount(); var14 < var13; ++var14) {
                Node var18 = element.node(var14);
                if (var18 instanceof Text) {
                    this.writeNode(var18);
                    var12 = var18;
                } else {
                    if (var12 != null && this.format.isPadText()) {
                        String var17 = var12.getText();
                        char var19 = var17.charAt(var17.length() - 1);
                        if (Character.isWhitespace(var19)) {
                            this.writer.write(" ");
                        }
                    }

                    this.writeNode(var18);
                    var12 = null;
                }
            }
        }

        this.preserve = oldPreserve;
    }

    protected void writeCDATA(String text) throws IOException {
        this.writer.write("<![CDATA[");
        if (text != null) {
            this.writer.write(text);
        }

        this.writer.write("]]>");
        this.lastOutputNodeType = 4;
    }

    /**
     * 写入文档类型
     *
     * @param docType 文档类型
     * @throws IOException
     */
    protected void writeDocType(DocumentType docType) throws IOException {
        if (docType != null) {
            docType.write(this.writer);
            this.writePrintln();
        }

    }

    protected void writeNamespace(Namespace namespace) throws IOException {
        if (namespace != null && (!"".equals(namespace.getPrefix()) || !"".equals(namespace.getURI()))) {
            this.writeNamespace(namespace.getPrefix(), namespace.getURI());
        }

    }

    protected void writeNamespaces() throws IOException {
        if (this.namespacesMap != null) {
            Iterator iter = this.namespacesMap.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String prefix = (String) entry.getKey();
                String uri = (String) entry.getValue();
                this.writeNamespace(prefix, uri);
            }

            this.namespacesMap = null;
        }

    }

    protected void writeNamespace(String prefix, String uri) throws IOException {
        if (prefix != null && prefix.length() > 0) {
            this.writer.write(" xmlns:");
            this.writer.write(prefix);
            this.writer.write("=\"");
        } else {
            this.writer.write(" xmlns=\"");
        }

        this.writer.write(uri);
        this.writer.write("\"");
    }

    protected void writeProcessingInstruction(ProcessingInstruction pi) throws IOException {
        this.writer.write("<?");
        this.writer.write(pi.getName());
        this.writer.write(" ");
        this.writer.write(pi.getText());
        this.writer.write("?>");
        this.writePrintln();
        this.lastOutputNodeType = 7;
    }

    protected void writeString(String text) throws IOException {
        if (text != null && text.length() > 0) {
            if (this.escapeText) {
                text = this.escapeElementEntities(text);
            }

            if (this.format.isTrimText()) {
                boolean first = true;

                String token;
                for (StringTokenizer tokenizer = new StringTokenizer(text); tokenizer.hasMoreTokens(); this.lastChar = token.charAt(token.length() - 1)) {
                    token = tokenizer.nextToken();
                    if (first) {
                        first = false;
                        if (this.lastOutputNodeType == 3) {
                            this.writer.write(" ");
                        }
                    } else {
                        this.writer.write(" ");
                    }

                    this.writer.write(token);
                    this.lastOutputNodeType = 3;
                }
            } else {
                this.lastOutputNodeType = 3;
                this.writer.write(text);
                this.lastChar = text.charAt(text.length() - 1);
            }
        }

    }

    protected void writeNodeText(Node node) throws IOException {
        String text = node.getText();
        if (text != null && text.length() > 0) {
            if (this.escapeText) {
                text = this.escapeElementEntities(text);
            }

            this.lastOutputNodeType = 3;
            this.writer.write(text);
            this.lastChar = text.charAt(text.length() - 1);
        }

    }

    protected void writeNode(Node node) throws IOException {
        short nodeType = node.getNodeType();
        switch (nodeType) {
            case 1:
                this.writeElement((Element) node);
                break;
            case 2:
                this.writeAttribute((Attribute) node);
                break;
            case 3:
                this.writeNodeText(node);
                break;
            case 4:
                this.writeCDATA(node.getText());
                break;
            case 5:
                this.writeEntity((Entity) node);
                break;
            case 6:
            case 11:
            case 12:
            default:
                throw new IOException("Invalid node type: " + node);
            case 7:
                this.writeProcessingInstruction((ProcessingInstruction) node);
                break;
            case 8:
                this.writeComment(node.getText());
                break;
            case 9:
                this.write((Document) node);
                break;
            case 10:
                this.writeDocType((DocumentType) node);
            case 13:
        }

    }

    /**
     * 安装一个词法处理器
     */
    protected void installLexicalHandler() {
        XMLReader parent = this.getParent();
        if (parent == null) {
            throw new NullPointerException("No parent for filter");
        } else {
            for (String LEXICAL_HANDLER_NAME : LEXICAL_HANDLER_NAMES) {
                try {
                    parent.setProperty(LEXICAL_HANDLER_NAME, this);
                    break;
                } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    protected void writeDocType(String name, String publicID, String systemID) throws IOException {
        boolean hasPublic = false;
        this.writer.write("<!DOCTYPE ");
        this.writer.write(name);
        if (publicID != null && !publicID.equals("")) {
            this.writer.write(" PUBLIC \"");
            this.writer.write(publicID);
            this.writer.write("\"");
            hasPublic = true;
        }

        if (systemID != null && !systemID.equals("")) {
            if (!hasPublic) {
                this.writer.write(" SYSTEM");
            }

            this.writer.write(" \"");
            this.writer.write(systemID);
            this.writer.write("\"");
        }

        this.writer.write(">");
        this.writePrintln();
    }

    protected void writeEntity(Entity entity) throws IOException {
        if (!this.resolveEntityRefs()) {
            this.writeEntityRef(entity.getName());
        } else {
            this.writer.write(entity.getText());
        }

    }

    protected void writeEntityRef(String name) throws IOException {
        this.writer.write("&");
        this.writer.write(name);
        this.writer.write(";");
        this.lastOutputNodeType = 5;
    }

    protected void writeComment(String text) throws IOException {
        if (this.format.isNewlines()) {
            this.println();
            this.indent();
        }

        this.writer.write("<!--");
        this.writer.write(text);
        this.writer.write("-->");
        this.lastOutputNodeType = 8;
    }

    protected void writeAttributes(Element element) throws IOException {
        int i = 0;

        for (int size = element.attributeCount(); i < size; ++i) {
            Attribute attribute = element.attribute(i);
            Namespace ns = attribute.getNamespace();
            String attName;
            String quote;
            if (ns != null && ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
                attName = ns.getPrefix();
                quote = this.namespaceStack.getURI(attName);
                if (!ns.getURI().equals(quote)) {
                    this.writeNamespace(ns);
                    this.namespaceStack.push(ns);
                }
            }

            attName = attribute.getName();
            if (attName.startsWith("xmlns:")) {
                quote = attName.substring(6);
                if (this.namespaceStack.getNamespaceForPrefix(quote) == null) {
                    String uri = attribute.getValue();
                    this.namespaceStack.push(quote, uri);
                    this.writeNamespace(quote, uri);
                }
            } else if (attName.equals("xmlns")) {
                if (this.namespaceStack.getDefaultNamespace() == null) {
                    quote = attribute.getValue();
                    this.namespaceStack.push((String) null, quote);
                    this.writeNamespace((String) null, quote);
                }
            } else {
                char var9 = this.format.getAttributeQuoteCharacter();
                this.writer.write(" ");
                this.writer.write(attribute.getQualifiedName());
                this.writer.write("=");
                this.writer.write(var9);
                this.writeEscapeAttributeEntities(attribute.getValue());
                this.writer.write(var9);
            }
        }

    }

    protected void writeAttribute(Attribute attribute) throws IOException {
        this.writer.write(" ");
        this.writer.write(attribute.getQualifiedName());
        this.writer.write("=");
        char quote = this.format.getAttributeQuoteCharacter();
        this.writer.write(quote);
        this.writeEscapeAttributeEntities(attribute.getValue());
        this.writer.write(quote);
        this.lastOutputNodeType = 2;
    }

    protected void writeAttributes(Attributes attributes) throws IOException {
        int i = 0;

        for (int size = attributes.getLength(); i < size; ++i) {
            this.writeAttribute(attributes, i);
        }

    }

    protected void writeAttribute(Attributes attributes, int index) throws IOException {
        char quote = this.format.getAttributeQuoteCharacter();
        this.writer.write(" ");
        this.writer.write(attributes.getQName(index));
        this.writer.write("=");
        this.writer.write(quote);
        this.writeEscapeAttributeEntities(attributes.getValue(index));
        this.writer.write(quote);
    }

    /**
     * 写入缩进
     *
     * @throws IOException
     */
    protected void indent() throws IOException {
        // 缩进字符
        String indent = this.format.getIndent();
        if (indent != null && indent.length() > 0) {
            // 缩进级别
            for (int i = 0; i < this.indentLevel; ++i) {
                this.writer.write(indent);
            }
        }

    }

    protected void writePrintln() throws IOException {
        if (this.format.isNewlines()) {
            String seperator = this.format.getLineSeparator();
            if (this.lastChar != seperator.charAt(seperator.length() - 1)) {
                this.writer.write(this.format.getLineSeparator());
            }
        }

    }

    protected Writer createWriter(OutputStream outStream, String encoding) throws UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(outStream, encoding));
    }

    /**
     * 写入文档描述
     *
     * @throws IOException
     */
    protected void writeDeclaration() throws IOException {
        OutputFormat format = this.getOutputFormat();
        String encoding = format.getEncoding();
        if (!format.isSuppressDeclaration()) {

            this.writer.write("<?xml version=\"1.0\"");
            if (!format.isOmitEncoding()) {
                // 编码非缺省，使用格式化的编码格式
                this.writer.write(" encoding=\"" + (encoding.equals("UTF8") ? "UTF-8" : encoding) + "\"");
            }
            this.writer.write(" standalone=\"yes\"");
            this.writer.write("?>");

            if (format.isNewLineAfterDeclaration()) {
                this.println();
            }
        }

    }

    protected void writeEmptyElementClose(String qualifiedName) throws IOException {
        if (!this.format.isExpandEmptyElements()) {
            this.writer.write("/>");
        } else {
            this.writer.write("></");
            this.writer.write(qualifiedName);
            this.writer.write(">");
        }

    }

    protected boolean isExpandEmptyElements() {
        return this.format.isExpandEmptyElements();
    }

    protected String escapeElementEntities(String text) {
        char[] block = null;
        int last = 0;
        int size = text.length();

        int i;
        String answer;
        for (i = 0; i < size; ++i) {
            answer = null;
            char c = text.charAt(i);
            switch (c) {
                case '\t':
                case '\n':
                case '\r':
                    if (this.preserve) {
                        answer = String.valueOf(c);
                    }
                    break;
                case '&':
                    answer = "&amp;";
                    break;
                case '<':
                    answer = "&lt;";
                    break;
                case '>':
                    answer = "&gt;";
                    break;
                default:
                    if (c < 32 || this.shouldEncodeChar(c)) {
                        answer = "&#" + c + ";";
                    }
            }

            if (answer != null) {
                if (block == null) {
                    block = text.toCharArray();
                }

                this.buffer.append(block, last, i - last);
                this.buffer.append(answer);
                last = i + 1;
            }
        }

        if (last == 0) {
            return text;
        } else {
            if (last < size) {
                if (block == null) {
                    block = text.toCharArray();
                }

                this.buffer.append(block, last, i - last);
            }

            answer = this.buffer.toString();
            this.buffer.setLength(0);
            return answer;
        }
    }

    protected void writeEscapeAttributeEntities(String txt) throws IOException {
        if (txt != null) {
            String escapedText = this.escapeAttributeEntities(txt);
            this.writer.write(escapedText);
        }

    }

    protected String escapeAttributeEntities(String text) {
        char quote = this.format.getAttributeQuoteCharacter();
        char[] block = null;
        int last = 0;
        int size = text.length();

        int i;
        String answer;
        for (i = 0; i < size; ++i) {
            answer = null;
            char c = text.charAt(i);
            switch (c) {
                case '\t':
                case '\n':
                case '\r':
                    break;
                case '\"':
                    if (quote == 34) {
                        answer = "&quot;";
                    }
                    break;
                case '&':
                    answer = "&amp;";
                    break;
                case '\'':
                    if (quote == 39) {
                        answer = "&apos;";
                    }
                    break;
                case '<':
                    answer = "&lt;";
                    break;
                case '>':
                    answer = "&gt;";
                    break;
                default:
                    if (c < 32 || this.shouldEncodeChar(c)) {
                        answer = "&#" + c + ";";
                    }
            }

            if (answer != null) {
                if (block == null) {
                    block = text.toCharArray();
                }

                this.buffer.append(block, last, i - last);
                this.buffer.append(answer);
                last = i + 1;
            }
        }

        if (last == 0) {
            return text;
        } else {
            if (last < size) {
                if (block == null) {
                    block = text.toCharArray();
                }

                this.buffer.append(block, last, i - last);
            }

            answer = this.buffer.toString();
            this.buffer.setLength(0);
            return answer;
        }
    }

    protected boolean shouldEncodeChar(char c) {
        int max = this.getMaximumAllowedCharacter();
        return max > 0 && c > max;
    }

    protected int defaultMaximumAllowedCharacter() {
        String encoding = this.format.getEncoding();
        return encoding != null && encoding.equals("US-ASCII") ? 127 : -1;
    }

    protected boolean isNamespaceDeclaration(Namespace ns) {
        if (ns != null && ns != Namespace.XML_NAMESPACE) {
            String uri = ns.getURI();
            if (uri != null && !this.namespaceStack.contains(ns)) {
                return true;
            }
        }

        return false;
    }

    protected void handleException(IOException e) throws SAXException {
        throw new SAXException(e);
    }

    protected OutputFormat getOutputFormat() {
        return this.format;
    }

    public boolean resolveEntityRefs() {
        return this.resolveEntityRefs;
    }

    public void setResolveEntityRefs(boolean resolve) {
        this.resolveEntityRefs = resolve;
    }

    public static void main(String[] args) throws IOException {
        XMLWriter xmlWriter = new XMLWriter();
        Document document = new DefaultDocument("ASDASD");
        Element element = new BaseElement("test");
        element.addAttribute("enable", "true");
        document.add(element);
        xmlWriter.write(document);
    }
}
