/**
 * Project: x-framework
 * Package Name: org.ike.security
 * Author: Xuejia
 * Date Time: 2016/11/21 20:55
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.security;

import java.security.MessageDigest;

/**
 * Class Name: MD5Utils
 * Create Date: 2016/11/21 20:55
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * MD5加密工具
 */
public class MD5Utils {

    /**
     * 将一个字符串进行MD5加密
     *
     * @param str     需要加密的源字符串
     * @param isUpper 是否将加密结果转化为大写
     * @return 返回根据要求转化的字符串
     */
    public static String toMD5(String str, boolean isUpper) {
        return isUpper ? toMD5(str).toUpperCase() : toMD5(str).toLowerCase();
    }

    /**
     * 生成MD5加密串
     *
     * @param str 需要进行加密的字符串
     * @return 返回加密后的字符串
     */
    public static String toMD5(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
