package org.chim.altass.toolkit;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.domain.IEntry;

import java.io.Serializable;

/**
 * Class Name: JDFWrapper
 * Create Date: 18-1-8 上午12:16
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Job Description File
 * 作业文件描述表示
 * <p>
 * 用于将执行的作业对象Entry封装成作业文件描述进行网络传输
 */
public class JDFWrapper implements Serializable {

    private static final long serialVersionUID = 8275048646375117133L;

    /**
     * Entry xml序列化
     */
    private String entryXml = null;

    /**
     * entry对应的实类
     */
    private String clz = null;

    public JDFWrapper() {
    }

    public JDFWrapper(IEntry entry) {
        try {
            this.entryXml = EXmlParser.toXml(entry);
            this.clz = entry.getClass().getName();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    private void setEntry(IEntry entry) {
        try {
            this.entryXml = EXmlParser.toXml(entry);
            this.clz = entry.getClass().getName();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    public String getEntryXml() {
        return entryXml;
    }

    public void setEntryXml(String entryXml) {
        this.entryXml = entryXml;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    /**
     * 将一个包含了序列化后的xml数据和class信息，恢复为对应实例对象
     *
     * @param <T> 恢复后的对象实例
     * @return 恢复后的实例对象
     */
    @SuppressWarnings("unchecked")
    public <T> T restore() {
        try {
            Class<? extends T> entryClz = (Class<? extends T>) Class.forName(this.clz);
            return EXmlParser.fromXml(this.entryXml, entryClz);
        } catch (XmlParserException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
