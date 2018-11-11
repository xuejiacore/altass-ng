/**
 * Project: x-framework
 * Package Name: org.ike.security
 * Author: Xuejia
 * Date Time: 2016/11/21 20:27
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.security;

import java.util.UUID;

/**
 * Class Name: NetSecurity
 * Create Date: 2016/11/21 20:27
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 网络传输安全控制
 */
public class NetSecurity {

    /**
     * 校验来源准确幸
     * <p>
     * 该校验算法用来检验通过体系内部进行交互的数据，必须通过一定的加解密规则进行处理后，才能够获得对应的数据
     *
     * @param timestamp  时间戳
     * @param nonceStr   随机字符串
     * @param token      令牌
     * @param encryptStr 客户端加密的结果
     * @param isEncrypt  是否加密
     * @return 如果校验通过，那么返回值为true，否则返回值为false
     */
    public static boolean signature(long timestamp, String nonceStr, String token, String encryptStr, boolean isEncrypt) {
        String tmpStr = "\001" + timestamp + "\007" + nonceStr + (isEncrypt ? token : "") + "\003";
        String encryptResult = MD5Utils.toMD5(tmpStr, true);
        return encryptResult.equals(encryptStr);
    }

    /**
     * 默认使用将token加入加密串中进行校验的方式进行加密验证
     *
     * @param timestamp  时间戳
     * @param nonceStr   随机字符串
     * @param token      token
     * @param encryptStr 比较的加密串
     * @return 如果校验通过，那么返回值为true，否则返回值为false
     */
    public static boolean signature(long timestamp, String nonceStr, String token, String encryptStr) {
        return signature(timestamp, nonceStr, token, encryptStr, true);
    }

    /**
     * 准备安全体系请求的加密串
     *
     * @return 返回加密串
     */
    public static String prepareEncryptStr(long timestamp, String nonceStr, String token, boolean isEncrypt) {
        String tmpStr = "\001" + timestamp + "\007" + nonceStr + (isEncrypt ? token : "") + "\003";
        return MD5Utils.toMD5(tmpStr, true);
    }

    /**
     * 使用token加入到加密串种进行加密
     *
     * @param timestamp 时间戳
     * @param nonceStr  随机字符串
     * @param token     token
     * @return 返回加密串
     */
    public static String prepareEncryptStr(long timestamp, String nonceStr, String token) {
        return prepareEncryptStr(timestamp, nonceStr, token, true);
    }

    /**
     * 生成uuid字符串
     *
     * @return 返回UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成当前的时间戳
     *
     * @return 返回当前的时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
