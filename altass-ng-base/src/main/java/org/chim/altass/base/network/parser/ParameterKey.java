/**
 * Project: IkeChat
 * Package Name: org.ike.wechat.parser
 * Authorization: Xuejia
 * Date Time: 2016/6/13 15:43
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.network.parser;

/**
 * Class Name: ParameterKey
 * Create Date: 2016/6/13 15:43
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ParameterKey implements IParameterKey {

    private Object key = null;

    private String keyDesc = null;

    /**
     * 进行参数初始化
     *
     * @param key 键
     */
    public ParameterKey(Object key) {
        this.key = key;
    }

    /**
     * 进行参数初始化
     *
     * @param key     键
     * @param keyDesc 键的描述
     */
    public ParameterKey(Object key, String keyDesc) {
        this.key = key;
        this.keyDesc = keyDesc;
    }

    public String getKey() {
        return this.key.toString();
    }

    public String getDesc() {
        return this.keyDesc;
    }

    @Override
    public String toString() {
        return getKey();
    }
}
