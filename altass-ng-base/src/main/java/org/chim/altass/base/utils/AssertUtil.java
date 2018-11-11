/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/8 23:19
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils;


import org.chim.altass.base.utils.type.CollectionUtil;
import org.chim.altass.base.utils.type.ObjectUtil;
import org.chim.altass.base.utils.type.StringUtil;

import java.util.Collection;
import java.util.Map;

/**
 * Class Name: AssertUtil
 * Create Date: 2016/12/8 23:19
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */

public abstract class AssertUtil {
    /**
     * 断言表达式的值是为true
     *
     * @param expression 需要断言的bool表达式
     * @param message    如果为false，抛出的消息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言表达式的值为true
     *
     * @param expression 需要断言的bool表达式
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    /**
     * 断言对象为null
     *
     * @param object  需要断言的对象
     * @param message 对象不为空，抛出的消息
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象为null
     *
     * @param object 需要断言的对象
     */
    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    /**
     * 断言对象不为null
     *
     * @param object  需要断言的对象
     * @param message 当为null时抛出的消息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象不为null
     *
     * @param object 需要断言的对象
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * 断言字符串的长度不为0
     *
     * @param text    需要断言的字符串对象
     * @param message 字符串的长度为0时抛出的消息
     */
    public static void hasLength(String text, String message) {
        if (!StringUtil.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串的长度不为0
     *
     * @param text 需要断言的字符串对象
     */
    public static void hasLength(String text) {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    /**
     * 断言字符串有文本
     *
     * @param text    需要断言的对象
     * @param message 字符串中没有文本的时候抛出的消息
     */
    public static void hasText(String text, String message) {
        if (!StringUtil.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串有文本
     *
     * @param text 需要断言的对象
     */
    public static void hasText(String text) {
        hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    /**
     * 断言不包含
     *
     * @param textToSearch 需要断言的对象
     * @param substring    子串
     * @param message      包含时抛出的信息
     */
    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtil.hasLength(textToSearch) && StringUtil.hasLength(substring) && textToSearch.indexOf(substring) != -1) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言不包含
     *
     * @param textToSearch 需要断言的对象
     * @param substring    子串
     */
    public static void doesNotContain(String textToSearch, String substring) {
        doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
    }

    /**
     * 断言数组不为空
     *
     * @param array   需要断言的数组
     * @param message 数组为空时抛出的消息
     */
    public static void notEmpty(Object[] array, String message) {
        if (ObjectUtil.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数组不为空
     *
     * @param array 需要断言的数组
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    /**
     * 断言数组中没有null 的元素
     *
     * @param array   需要断言的数组对象
     * @param message 如果数组中有任意一个null对象，抛出的信息
     */
    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object anArray : array) {
                if (anArray == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }

    }

    /**
     * 断言没有null 的元素
     *
     * @param array 需要断言的数组
     */
    public static void noNullElements(Object[] array) {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    /**
     * 断言集合不为空
     *
     * @param collection 需要断言的集合
     * @param message    集合为空时抛出的信息
     */
    public static void notEmpty(Collection collection, String message) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param collection 需要断言的集合
     */
    public static void notEmpty(Collection collection) {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    /**
     * 断言不为空
     *
     * @param map     需要断言的map对象
     * @param message map为空时抛出的信息
     */
    public static void notEmpty(Map map, String message) {
        if (CollectionUtil.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言map对象不为空
     *
     * @param map 需要断言的map对象
     */
    public static void notEmpty(Map map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    /**
     * 断言对象的类型
     *
     * @param clazz 对象的类型
     * @param obj   需要断言的对象实例
     */
    public static void isInstanceOf(Class clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }

    /**
     * 断言对象的类型
     *
     * @param type    对象的类型
     * @param obj     需要断言的对象的实例
     * @param message 对象实例不是某个类型时，抛出的信息
     */
    public static void isInstanceOf(Class type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
        }
    }

    /**
     * @param superType
     * @param subType
     */
    public static void isAssignable(Class superType, Class subType) {
        isAssignable(superType, subType, "");
    }

    /**
     * @param superType
     * @param subType
     * @param message
     */
    public static void isAssignable(Class superType, Class subType, String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
        }
    }

    /**
     * @param expression
     * @param message
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * @param expression
     */
    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }
}
