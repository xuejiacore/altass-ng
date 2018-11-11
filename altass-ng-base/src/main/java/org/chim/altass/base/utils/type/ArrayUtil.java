/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/10 21:31
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.type;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class Name: ArrayUtil
 * Create Date: 2016/12/10 21:31
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 数组工具
 */
public class ArrayUtil {
    public ArrayUtil() {
    }

    public static <T> T[] clone(T[] array) {
        return ArrayUtils.clone(array);
    }

    public static <T> T[] addAll(T[] array1, T... array2) {
        return ArrayUtils.addAll(array1, array2);
    }

    /**
     *
     * @param s 数字字符串数组
     * @return 将String数组转换成int数组，并返回
     */
    public static int[] stringToInts(String[] s) {
        int[] n = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            n[i] = Integer.parseInt(s[i]);
        }
        return n;
    }

    /**
     *
     * @param s
     * @return 将String数组转换成Integer数组，并返回
     */
    public static Integer[] stringToIntegers(String[] s) {
        Integer[] n = new Integer[s.length];
        for (int i = 0; i < s.length; i++) {
            n[i] = Integer.parseInt(s[i]);
        }
        return n;
    }

    /**
     *
     * @param separator 连接符
     * @param objects 数组
     * @return 把数组通过连接符拼接成字符串
     */
    public static String objs2StrLinkBy(String separator, Object... objects) {
        if (objects == null || objects.length == 0) return "";
        StringBuilder result = new StringBuilder();
        for (Object temp : objects) {
            if(temp != null)result.append(temp.toString()).append(separator);
        }
        if (result.toString().length() > 0) {
            return result.toString().substring(0, result.toString().lastIndexOf(separator));
        }
        return "";
    }

    /**
     *
     * @param objArr Object对象数组
     * @return 将objArr转换成字符串数组返回
     */
    public static String[] objArr2StrArr(Object[] objArr){
        if(objArr == null){
            return null;
        }
        String[] values = new String[objArr.length];
        int index = 0;
        for(Object obj : objArr){
            values[index++] = (obj == null ? null : obj.toString());
        }

        return values;
    }

    /**
     *
     * @param objArr
     * @return 将objArr转换成set返回
     */
    public static Set<Object> objArr2ObjSet(Object[] objArr){
        if(objArr == null){
            return null;
        }
        return new HashSet<>(Arrays.asList(objArr));
    }
}
