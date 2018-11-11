/**
 * Project: x-framework
 * Package Name: org.ike.core.engine
 * Author: Xuejia
 * Date Time: 2016/12/8 23:38
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.taglib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chim.altass.base.utils.type.NumberUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class Name: Functions
 * Create Date: 2016/12/8 23:38
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 页面脚本方法
 */
public class Functions {

    private static final Log logger = LogFactory.getLog(Functions.class);
    public static final int HIGHEST_SPECIAL = 62;
    public static char[][] specialCharactersRepresentation = new char[63][];

    public Functions() {
    }

    /**
     * 转化为字符串方法
     *
     * @param input 输入对象
     * @return 返回输入对象的字符串
     */
    public static String toString(Object input) throws SQLException, IOException {
        String returnString;
        if (input == null) {
            returnString = "";

        } else if (input instanceof String) {
            returnString = (String) input;

        } else if (input instanceof Clob) {
            // 处理Clob类型的输入
            Reader is = ((Clob) input).getCharacterStream();
            BufferedReader bufferedReader = new BufferedReader(is);
            String line = bufferedReader.readLine();
            StringBuffer stringBuffer;
            for (stringBuffer = new StringBuffer(); line != null; line = bufferedReader.readLine()) {
                stringBuffer.append(line).append("\r\n");
            }
            returnString = stringBuffer.toString();

        } else {
            returnString = input.toString();

        }

        return returnString;
    }

    /**
     * 将字符串转化为对应的大写形式
     *
     * @param input 输入
     * @return 返回转化后的字符串
     */
    public static String toUpperCase(String input) {
        return input.toUpperCase();
    }

    /**
     * 将字符串转化为小写形式
     *
     * @param input 输入
     * @return 返回转化后的字符串
     */
    public static String toLowerCase(String input) {
        return input.toLowerCase();
    }

    /**
     * 字符串索引
     *
     * @param input     字符串输入
     * @param substring 需要检测的子串
     * @return 返回子串在字符串的位置
     */
    public static int indexOf(String input, String substring) {
        if (input == null) {
            input = "";
        }

        if (substring == null) {
            substring = "";
        }

        return input.indexOf(substring);
    }

    /**
     * 判断子串是否包含在输入串中
     *
     * @param input     输入
     * @param substring 需要检测的子串
     * @return 如果包含在字符串中，那么返回值为true，否则返回值为false
     */
    public static boolean contains(String input, String substring) {
        return indexOf(input, substring) != -1;
    }

    /**
     * 忽略大小写的包含
     *
     * @param input     输入
     * @param substring 子串
     * @return 如果包含在字符串中，那么返回值为true，否则返回值为false
     */
    public static boolean containsIgnoreCase(String input, String substring) {
        if (input == null) {
            input = "";
        }

        if (substring == null) {
            substring = "";
        }

        String inputUC = input.toUpperCase();
        String substringUC = substring.toUpperCase();
        return indexOf(inputUC, substringUC) != -1;
    }

    /**
     * 判断是否以某一个字符串开头
     *
     * @param input     输入
     * @param substring 子串
     * @return 如果是以某个字符串开头，那么返回值为true，否则返回值为false
     */
    public static boolean startsWith(String input, String substring) {
        if (input == null) {
            input = "";
        }

        if (substring == null) {
            substring = "";
        }

        return input.startsWith(substring);
    }

    /**
     * 判断字符串是否以某一个字符串结束
     *
     * @param input     输入
     * @param substring 子串
     * @return 如果是以某一个字符串结束，那么返回值为true，否则返回值为false
     */
    public static boolean endsWith(String input, String substring) {
        if (input == null) {
            input = "";
        }

        if (substring == null) {
            substring = "";
        }

        int index = input.indexOf(substring);
        return index != -1 && (index == 0 && substring.length() == 0 || index == input.length() - substring.length());
    }

    /**
     * 截取字串
     *
     * @param input      输入
     * @param beginIndex 截取的开始索引
     * @param endIndex   截取的结束索引
     * @return 返回截取完成的字串
     */
    public static String substring(String input, int beginIndex, int endIndex) {
        if (input == null) {
            input = "";
        }

        if (beginIndex >= input.length()) {
            return "";
        } else {
            if (beginIndex < 0) {
                beginIndex = 0;
            }

            if (endIndex < 0 || endIndex > input.length()) {
                endIndex = input.length();
            }

            return endIndex < beginIndex ? "" : input.substring(beginIndex, endIndex);
        }
    }

    /**
     * 向后截取字符串
     *
     * @param input     输入
     * @param substring 需要截取的字串
     * @return 返回截取后的字符串
     */
    public static String substringAfter(String input, String substring) {
        if (input == null) {
            input = "";
        }

        if (input.length() == 0) {
            return "";
        } else {
            if (substring == null) {
                substring = "";
            }

            if (substring.length() == 0) {
                return input;
            } else {
                int index = input.indexOf(substring);
                return index == -1 ? "" : input.substring(index + substring.length());
            }
        }
    }

    /**
     * 向前截取字串
     *
     * @param input     输入
     * @param substring 需要截取的字串
     * @return 返回截取后的字符串
     */
    public static String substringBefore(String input, String substring) {
        if (input == null) {
            input = "";
        }

        if (input.length() == 0) {
            return "";
        } else {
            if (substring == null) {
                substring = "";
            }

            if (substring.length() == 0) {
                return "";
            } else {
                int index = input.indexOf(substring);
                return index == -1 ? "" : input.substring(0, index);
            }
        }
    }

    /**
     * xml转义
     *
     * @param buffer 需要转义的字符串
     * @return 返回转义后的字符串
     */
    public static String escapeXml(String buffer) {
        if (buffer == null) {
            return "";
        } else {
            int start = 0;
            int length = buffer.length();
            char[] arrayBuffer = buffer.toCharArray();
            StringBuffer escapedBuffer = null;

            for (int i = 0; i < length; ++i) {
                char c = arrayBuffer[i];
                if (c <= 62) {
                    char[] escaped = specialCharactersRepresentation[c];
                    if (escaped != null) {
                        if (start == 0) {
                            escapedBuffer = new StringBuffer(length + 5);
                        }

                        if (start < i) {
                            escapedBuffer.append(arrayBuffer, start, i - start);
                        }

                        start = i + 1;
                        escapedBuffer.append(escaped);
                    }
                }
            }

            if (start == 0) {
                return buffer;
            } else {
                if (start < length) {
                    escapedBuffer.append(arrayBuffer, start, length - start);
                }

                return escapedBuffer.toString();
            }
        }
    }

    /**
     * 首尾去空
     *
     * @param input 需要去空的字符串
     * @return 返回去空后的字符串
     */
    public static String trim(String input) {
        return input == null ? "" : input.trim();
    }

    /**
     * 字符串替换
     *
     * @param input           需要替换的字符串
     * @param substringBefore 替换前的字符串
     * @param substringAfter  替换后的字符串
     * @return 返回替换后的字符串
     */
    public static String replace(String input, String substringBefore, String substringAfter) {
        if (input == null) {
            input = "";
        }

        if (input.length() == 0) {
            return "";
        } else {
            if (substringBefore == null) {
                substringBefore = "";
            }

            return substringBefore.length() == 0 ? input : input.replaceAll(substringBefore, substringAfter);
        }
    }

    /**
     * 字符串切割函数
     *
     * @param input      需要切割的字符串
     * @param delimiters 切割的分隔符
     * @return 返回切割后的字符串数组
     */
    public static String[] split(String input, String delimiters) {
        if (input == null) {
            input = "";
        }

        String[] array;
        if (input.length() == 0) {
            array = new String[]{""};
            return array;
        } else {
            if (delimiters == null) {
                delimiters = "";
            }

            StringTokenizer tok = new StringTokenizer(input, delimiters);
            int count = tok.countTokens();
            array = new String[count];
            int i = 0;
            while (tok.hasMoreTokens()) {
                array[i++] = tok.nextToken();
            }

            return array;
        }
    }

    /**
     * 获得对象的长度
     *
     * @param obj 需要检测长度的对象
     * @return 返回对象的长度
     */
    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        } else if (obj instanceof String) {
            return ((String) obj).length();
        } else if (obj instanceof Collection) {
            return ((Collection) obj).size();
        } else if (obj instanceof Map) {
            return ((Map) obj).size();
        } else {
            int var4;
            if (obj instanceof Iterator) {
                Iterator var5 = (Iterator) obj;
                var4 = 0;

                while (var5.hasNext()) {
                    ++var4;
                    var5.next();
                }

                return var4;
            } else if (!(obj instanceof Enumeration)) {
                try {
                    var4 = Array.getLength(obj);
                    return var4;
                } catch (IllegalArgumentException var3) {
                    throw new IllegalArgumentException("FOREACH_BAD_ITEMS");
                }
            } else {
                Enumeration ex = (Enumeration) obj;
                var4 = 0;

                while (ex.hasMoreElements()) {
                    ++var4;
                    ex.nextElement();
                }

                return var4;
            }
        }
    }

    /**
     * 数组的连接操作
     *
     * @param array     需要进行连接操作的字符串
     * @param separator 连接的分隔符
     * @return 返回字符串数组连接后的字符串
     */
    public static String join(String[] array, String separator) {
        if (array == null) {
            return "";
        } else {
            if (separator == null) {
                separator = "";
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < array.length; ++i) {
                stringBuilder.append(array[i]);
                if (i < array.length - 1) {
                    stringBuilder.append(separator);
                }
            }

            return stringBuilder.toString();
        }
    }

    /**
     * 根据日期格式格式化当前时间
     *
     * @param format 需要格式化的时间格式
     * @return 返回格式化后的时间字符串
     */
    public static String getDate(String format) {
        return (new SimpleDateFormat(format)).format(new Date());
    }

    /**
     * 将json字符串转化为对象
     *
     * @param json 需要进行转化的json字符串数据
     * @return 返回转化后的对象
     */
    public static Object json2java(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (json.indexOf(91) == 0) {
                return mapper.readValue(json, List.class);
            }

            if (json.indexOf(123) == 0) {
                return mapper.readValue(json, Map.class);
            }
        } catch (IOException var5) {
            logger.error("json字符串转化成java对象出错：" + var5.getMessage());
        }

        return null;
    }

    /**
     * 将对象转化为json字符串
     *
     * @param obj 需要进行转化的对象
     * @return 返回转化后的json字符串
     */
    public static String java2json(Object obj) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException var3) {
            logger.error("java对象转化成json字符串出错：" + var3.getMessage());
            return null;
        }
    }

    /**
     * 格式化数字
     *
     * @param number 需要格式化的数字
     * @return 返回格式化后的数字
     */
    public static String getNumberInstance(String number) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return nf.format(NumberUtil.createNumber(number));
    }

    /**
     * 获得子串出现的次数
     *
     * @param input     输入
     * @param substring 需要检测的子串
     * @return 返回子串出现的次数
     */
    public static int numberOf(String input, String substring) {
        int count = 0;
        int index = 0;
        while (input.indexOf(substring, index) + 1 != 0) {
            ++count;
        }

        return count;
    }

    /**
     * 将url进行encode
     *
     * @param input 需要进行编码的url
     * @param enc   编码
     * @return 返回编码后的url
     */
    public static String urlEncode(String input, String enc) {
        try {
            return URLEncoder.encode(input, enc);
        } catch (UnsupportedEncodingException var3) {
            logger.error("encode编码失败：" + var3.getMessage());
            return input;
        }
    }

    /**
     * 将url进行decode
     *
     * @param input 需要进行解码的url
     * @param enc   编码
     * @return 返回解码后的url
     */
    public static String urlDecode(String input, String enc) {
        try {
            return URLDecoder.decode(input, enc);
        } catch (UnsupportedEncodingException var3) {
            logger.error("decode解码失败：" + var3.getMessage());
            return input;
        }
    }

    /**
     * 中文解码
     *
     * @param input 需要解码的字符串
     * @return 返回中文解码后的字符串
     */
    public static String isoDecode(String input) {
        try {
            return new String(input.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            logger.error("iso解码失败：" + var2.getMessage());
            return input;
        }
    }

    /**
     * 判断对象是否为空对象
     *
     * @param input 需要判定的对象
     * @return 如果对象是null或者为空字符串，或者空的数组对象，或者空的集合，或者空的迭代，或者空的map，或者空的枚举，那么返回值为true，否则返回值为false
     */
    public static boolean empty(Object input) {
        return input == null || (
                input instanceof String ? "".equals(input.toString()) :                                                                         // 字符串对象
                        input instanceof Object[] ? ((Object[]) input).length == 0 :                                                            // 对象数组
                                input instanceof Collection ? ((Collection) input).size() == 0 :                                                // 集合
                                        input instanceof Iterator ? !((Iterator) input).hasNext() :                                             // 迭代器
                                                input instanceof Map ? ((Map) input).isEmpty() :                                                // map对象
                                                        input instanceof Enumeration ? !((Enumeration) input).hasMoreElements() :               // 集合对象
                                                                input instanceof char[] ? ((char[]) input).length == 0 :                        // 字符数组
                                                                        input instanceof int[] ? ((int[]) input).length == 0 :                  // 整型数组
                                                                                input instanceof double[] && ((double[]) input).length == 0);   // 双浮点型数组
    }

    static {
        specialCharactersRepresentation[38] = "&amp;".toCharArray();
        specialCharactersRepresentation[60] = "&lt;".toCharArray();
        specialCharactersRepresentation[62] = "&gt;".toCharArray();
        specialCharactersRepresentation[34] = "&#034;".toCharArray();
        specialCharactersRepresentation[39] = "&#039;".toCharArray();
    }

}
