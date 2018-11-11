/**
 * Project: x-framework
 * Package Name: org.ike.core.parser.xml.meta
 * Author: Xuejia
 * Date Time: 2016/12/10 21:27
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.parser.xml.meta;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.xml.domain.XmlBinding;

/**
 * Class Name: MetaParser
 * Create Date: 2016/12/10 21:27
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class MetaParser {
    protected XmlBinding binding = new XmlBinding();

    public MetaParser() {
    }

    /**
     * 对参数进行编码解码
     *
     * @param parameter 需要编码解码的对象
     * @return 返回Xml绑定对象实例
     * @throws XmlParserException
     */
    abstract XmlBinding decode(Object parameter) throws XmlParserException;
}
