/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/8 23:18
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.type;

import org.apache.commons.lang3.StringUtils;
import org.chim.altass.base.script.Script;
import org.chim.altass.base.script.ScriptContext;
import org.chim.altass.base.utils.AssertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class Name: StringUtil
 * Create Date: 2016/12/8 23:18
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 字符串工具类
 */
@SuppressWarnings("Duplicates")
public class StringUtil {
    private static final Script SCRIPT = new Script();              // 脚本执行器
    public static final String SPACE = " ";                         // 空格符号
    public static final String EMPTY = "";                          // 空字符串
    public static final int INDEX_NOT_FOUND = -1;                   // 未找到索引

    public StringUtil() {
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 需要判断的字符串对象
     * @return 如果字符串为空，那么返回值为true，否则返回值为false
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串是否非空
     *
     * @param cs 需要判断的字符串对象
     * @return 如果字符串为非空，那么返回值为true，否则返回值为false
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 判断字符串中是否是空白字符
     *
     * @param cs 需要判断的字符串对象
     * @return 如果是空白字符串，那么返回值为true，否则返回值为false
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断一个字符串是否是非空白字符串
     *
     * @param cs 需要判断的字符串对象
     * @return 如果是非空白字符串，那么返回值为true，否则返回值为false
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 去除字符串两端空白字符（如果为空，那么返回值为null）
     *
     * @param str 需要去除的字符串
     * @return 返回去除两端空白字符串后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 判断是否和参数中的字符串相同
     *
     * @param str        检测的值
     * @param equalsStrs 需要判断的对象
     * @return 只要发现有一个字符串相同，那么返回值为true，否则返回值为false
     */
    public static boolean equals(String str, String... equalsStrs) {
        AssertUtil.notNull(equalsStrs, "equalsStrs must not be null");
        if (equalsStrs.length > 0) {
            for (String equalsStr : equalsStrs) {
                if (equalsStr.equals(str)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param str
     * @param stripChars
     * @return
     */
    public static String strip(String str, String stripChars) {
        return StringUtils.strip(str, stripChars);
    }

    public static String stripStart(String str, String stripChars) {
        return StringUtils.stripStart(str, stripChars);
    }

    public static String stripEnd(String str, String stripChars) {
        return StringUtils.stripEnd(str, stripChars);
    }

    public static int indexOf(String str, String searchStr, String afterStr) {
        return str != null && searchStr != null ? (afterStr != null && afterStr.length() != 0 && str.indexOf(afterStr) != -1 ? (str.indexOf(afterStr) > str.indexOf(searchStr) ? str.indexOf(searchStr) : -1) : str.indexOf(searchStr)) : -1;
    }

    public static int lastIndexOf(String str, String searchStr, String afterStr) {
        return str != null && searchStr != null ? (afterStr != null && afterStr.length() != 0 && str.lastIndexOf(afterStr) != -1 ? (str.lastIndexOf(afterStr) < str.lastIndexOf(searchStr) ? str.lastIndexOf(searchStr) : -1) : str.lastIndexOf(searchStr)) : -1;
    }

    public static String substringBefore(String str, String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    public static String substringAfter(String str, String separator) {
        return StringUtils.substringAfter(str, separator);
    }

    public static String substringBeforeLast(String str, String separator) {
        return StringUtils.substringBeforeLast(str, separator);
    }

    public static String substringAfterLast(String str, String separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    public static String substringBetween(String str, String tag) {
        return StringUtils.substringBetween(str, tag);
    }

    public static String substringBetween(String str, String open, String close) {
        return StringUtils.substringBetween(str, open, close);
    }

    public static String[] substringsBetween(String str, String open, String close) {
        return StringUtils.substringsBetween(str, open, close);
    }

    public static String replaceBetween(String str, String open, String close, String replacement) {
        if (str != null && !isEmpty(open) && !isEmpty(close)) {
            int strLen = str.length();
            if (strLen == 0) {
                return str;
            } else {
                int openLen = open.length();
                int closeLen = close.length();
                StringBuilder sb = new StringBuilder();

                int pos;
                int end;
                for (pos = 0; pos < strLen - closeLen; pos = end + closeLen) {
                    int start = str.indexOf(open, pos);
                    if (start < 0) {
                        break;
                    }

                    start += openLen;
                    end = str.indexOf(close, start);
                    if (end < 0) {
                        break;
                    }

                    sb.append(str.substring(pos, start - openLen));
                    String content = str.substring(start, end);
                    if (isNotBlank(content)) {
                        sb.append(replacement);
                    }
                }

                sb.append(str.substring(pos));
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    public static String replaceBetween(String str, String open, String close, Map<String, Object> replacement) {
        if (str != null && !isEmpty(open) && !isEmpty(close)) {
            int strLen = str.length();
            if (strLen == 0) {
                return str;
            } else {
                AssertUtil.notNull(replacement, "replacement must not be null");
                ScriptContext scriptContext = SCRIPT.getScriptContext(replacement);
                int openLen = open.length();
                int closeLen = close.length();
                StringBuilder sb = new StringBuilder();

                int pos;
                int end;
                for (pos = 0; pos < strLen - closeLen; pos = end + closeLen) {
                    int start = str.indexOf(open, pos);
                    if (start < 0) {
                        break;
                    }

                    start += openLen;
                    end = str.indexOf(close, start);
                    if (end < 0) {
                        break;
                    }

                    sb.append(str.substring(pos, start - openLen));
                    String exception = str.substring(start, end);
                    if (isNotBlank(exception)) {
                        sb.append((Object) SCRIPT.evaluateScript(exception, scriptContext));
                    }
                }

                sb.append(str.substring(pos));
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    public static String replaceAndSubStringsBetween(String str, String open, String close, String replacement, List<String> substrings) {
        if (str != null && !isEmpty(open) && !isEmpty(close)) {
            int strLen = str.length();
            if (strLen == 0) {
                return str;
            } else {
                AssertUtil.notNull(substrings, "substrings must not be null");
                substrings.clear();
                int openLen = open.length();
                int closeLen = close.length();
                StringBuilder sb = new StringBuilder();

                int pos;
                int end;
                for (pos = 0; pos < strLen - closeLen; pos = end + closeLen) {
                    int start = str.indexOf(open, pos);
                    if (start < 0) {
                        break;
                    }

                    start += openLen;
                    end = str.indexOf(close, start);
                    if (end < 0) {
                        break;
                    }

                    sb.append(str.substring(pos, start - openLen));
                    String content = str.substring(start, end);
                    if (isNotBlank(content)) {
                        sb.append(replacement);
                        substrings.add(content);
                    }
                }

                sb.append(str.substring(pos));
                return sb.toString();
            }
        } else {
            return null;
        }
    }

    public static Map<String, String> str2map(String input, String marker, String delimiter) {
        if (input == null || input.length() == 0) {
            return null;
        }

        HashMap<String, String> map = new HashMap<>();
        String[] keyValues = input.trim().split(delimiter);

        for (String keyValue : keyValues) {
            map.put(keyValue.substring(0, keyValue.indexOf(marker)).trim(), keyValue.substring(keyValue.indexOf(marker) + 1).trim());
        }

        return map;
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    public static void main(String[] args) {
        new ArrayList();
        String a = "${name} www ${pwd} ee ${age} 11}";
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "zfd");
        map.put("pwd", "123pwd");
        map.put("age", 25);
        System.err.println(replaceBetween(a, "${", "}", map));
        String sqlsta = "select user_no,area_no,city_no from user info where area_no=#area# {and city_no=#age#} {and acct_date=#pwd#}";
        Script escript = new Script();
        ScriptContext context = escript.getScriptContext(map);
        List<String> conditionalExpressions = new ArrayList<>();
        String sql = replaceAndSubStringsBetween(sqlsta, "{", "}", "$sqlConditionalExpression", conditionalExpressions);

        System.err.println(sql);

        System.out.println(conditionalExpressions);
        for (Object conditionalExpression : conditionalExpressions) {
            boolean conditionValue = true;
            String[] sqlParameterExceptions = substringsBetween((String) conditionalExpression, "#", "#");
            if (sqlParameterExceptions != null) {
                int i = 0;

                for (int len = sqlParameterExceptions.length; i < len; ++i) {
                    System.err.println((Object) escript.evaluateScript(sqlParameterExceptions[i], context));
                    if ("".equals(escript.evaluateScript(sqlParameterExceptions[i], context))) {
                        conditionValue = false;
                        break;
                    }
                }
            }

            if (conditionValue) {
                sql = sql.replaceFirst("\\$sqlConditionalExpression", (String) conditionalExpression);
            } else {
                sql = sql.replaceFirst("\\$sqlConditionalExpression", "");
            }
        }

        System.out.println(sql);
    }

    /**
     * 将一个以 camel 命名的名称使用切割符号进行切分
     *
     * 如：getFooBar，分隔符为"-"，转化的结果为：get-foo-bar
     *
     * @param camelName 使用 camel 命名法的名称
     * @param split     分隔符
     * @return 返回切割后以指定分割符处理的结果
     */
    public static String camelToSplitName(String camelName, String split) {
        if (camelName == null || camelName.length() == 0) {
            return camelName;
        }
        StringBuilder buf = null;
        for (int i = 0; i < camelName.length(); i++) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (buf == null) {
                    buf = new StringBuilder();
                    if (i > 0) {
                        buf.append(camelName.substring(0, i));
                    }
                }
                if (i > 0) {
                    buf.append(split);
                }
                buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
                buf.append(ch);
            }
        }
        return buf == null ? camelName : buf.toString();
    }


}
