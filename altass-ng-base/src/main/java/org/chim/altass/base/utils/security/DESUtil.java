/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/10 18:07
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.SecureRandom;

/**
 * Class Name: DESUtil
 * Create Date: 2016/12/10 18:07
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 通过3重DES算法，加密或解密数据
 */
public class DESUtil {
    /**
     * 3重DES
     */
    private final static String DES = "DESede";

    /**
     * DES加密KEY 默认的key 长度必须是24位
     */
    public final static String DES_KEY = "ike_dp_beaf_md_dg_&n*2b_2u";

    /**
     * 加密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是24位
     * @return 返回加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key)
            throws RuntimeException {
        // DES算法要求有一个可信任的随机数源
        try {
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESedeKeySpec dks = new DESedeKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是24位
     * @return 返回解密后的原始数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key)
            throws RuntimeException {
        try { // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESedeKeySpec dks = new DESedeKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES数据解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data) {
        if (data == null) {
            return null;
        }
        return new String(
                decrypt(hex2byte(data.getBytes()), DES_KEY.getBytes()));
    }

    /**
     * DES数据解密
     *
     * @param data
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
    }

    /**
     * DES数据加密,使用默认的key
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static String encrypt(String data) {
        if (data != null)
            try {
                return byte2hex(encrypt(data.getBytes(), DES_KEY.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        return null;
    }

    /**
     * DES数据加密
     *
     * @param data
     * @param key  密钥
     * @return
     * @throws Exception
     */
    public final static String encrypt(String data, String key) {
        if (data != null && key != null)
            try {
                return byte2hex(encrypt(data.getBytes(), key.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        return null;
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static void main(String[] args) {
        String test = "bonc123456789123456789123456789";
        // DES加密
        test = encrypt(test);
        System.err.println("加密后的数据：" + test);
        System.err.println("加密后的数据：" + test.length());
        // DES解密
        test = decrypt(test);
        System.err.println("解密后的数据：" + test);
    }
}
