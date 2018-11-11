package org.chim.altass.core.manager;

import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.toolkit.job.UpdateAnalysis;

import java.io.Serializable;

/**
 * Class Name: UpdatePkgWrapper
 * Create Date: 18-3-19 下午5:53
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 作业更新包
 */
public class UpdatePkgWrapper implements Serializable {
    private static final long serialVersionUID = -1916954575956245021L;

    private String xml = null;

    public UpdatePkgWrapper() {
    }

    public UpdatePkgWrapper(UpdateAnalysis updatePkg) {
        try {
            this.xml = EXmlParser.toXml(updatePkg);
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    public void setUpdatePkg(UpdateAnalysis updatePkg) {
        try {
            this.xml = EXmlParser.toXml(updatePkg);
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
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
            Class<? extends T> entryClz = (Class<? extends T>) Class.forName(UpdateAnalysis.class.getName());
            return EXmlParser.fromXml(this.xml, entryClz);
        } catch (XmlParserException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
