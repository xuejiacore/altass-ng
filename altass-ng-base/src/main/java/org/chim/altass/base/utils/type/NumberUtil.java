/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/8 23:39
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Class Name: NumberUtil
 * Create Date: 2016/12/8 23:39
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class NumberUtil {
    public NumberUtil() {
    }

    public static String format(String format, int number) {
        return (new DecimalFormat(format).format(number));
    }

    public static int stringToInt(String str) {
        return stringToInt(str, 0);
    }

    public static int stringToInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException var3) {
            return defaultValue;
        }
    }

    public static Number createNumber(String val) throws NumberFormatException {
        if (val == null) {
            return null;
        } else if (val.length() == 0) {
            throw new NumberFormatException("\"\" is not a valid number.");
        } else if (val.length() == 1 && !Character.isDigit(val.charAt(0))) {
            throw new NumberFormatException(val + " is not a valid number.");
        } else if (val.startsWith("--")) {
            return null;
        } else if (!val.startsWith("0x") && !val.startsWith("-0x")) {
            char lastChar = val.charAt(val.length() - 1);
            int decPos = val.indexOf(46);
            int expPos = val.indexOf(101) + val.indexOf(69) + 1;
            String mant;
            String dec;
            if (decPos > -1) {
                if (expPos > -1) {
                    if (expPos < decPos) {
                        throw new NumberFormatException(val + " is not a valid number.");
                    }

                    dec = val.substring(decPos + 1, expPos);
                } else {
                    dec = val.substring(decPos + 1);
                }

                mant = val.substring(0, decPos);
            } else {
                if (expPos > -1) {
                    mant = val.substring(0, expPos);
                } else {
                    mant = val;
                }

                dec = null;
            }

            String exp;
            if (!Character.isDigit(lastChar)) {
                if (expPos > -1 && expPos < val.length() - 1) {
                    exp = val.substring(expPos + 1, val.length() - 1);
                } else {
                    exp = null;
                }

                String allZeros1 = val.substring(0, val.length() - 1);
                boolean nfe2 = isAllZeros(mant) && isAllZeros(exp);
                switch (lastChar) {
                    case 'D':
                    case 'd':
                        break;
                    case 'F':
                    case 'f':
                        try {
                            Float e = createFloat(allZeros1);
                            if (e.isInfinite() || e.floatValue() == 0.0F && !nfe2) {
                                break;
                            }

                            return e;
                        } catch (NumberFormatException var15) {
                            break;
                        }
                    case 'L':
                    case 'l':
                        if (dec == null && exp == null && (allZeros1.charAt(0) == 45 && isDigits(allZeros1.substring(1)) || isDigits(allZeros1))) {
                            try {
                                return createLong(allZeros1);
                            } catch (NumberFormatException var11) {
                                return createBigInteger(allZeros1);
                            }
                        } else {
                            throw new NumberFormatException(val + " is not a valid number.");
                        }
                    default:
                        throw new NumberFormatException(val + " is not a valid number.");
                }

                try {
                    Double e1 = createDouble(allZeros1);
                    if (!e1.isInfinite() && ((double) e1.floatValue() != 0.0D || nfe2)) {
                        return e1;
                    }
                } catch (NumberFormatException var14) {
                    ;
                }

                try {
                    return createBigDecimal(allZeros1);
                } catch (NumberFormatException var13) {
                    throw new NumberFormatException(val + " is not a valid number.");
                }
            } else {
                if (expPos > -1 && expPos < val.length() - 1) {
                    exp = val.substring(expPos + 1, val.length());
                } else {
                    exp = null;
                }

                if (dec == null && exp == null) {
                    try {
                        return createInteger(val);
                    } catch (NumberFormatException var12) {
                        try {
                            return createLong(val);
                        } catch (NumberFormatException var10) {
                            return createBigInteger(val);
                        }
                    }
                } else {
                    boolean allZeros = isAllZeros(mant) && isAllZeros(exp);

                    try {
                        Float nfe = createFloat(val);
                        if (!nfe.isInfinite() && (nfe.floatValue() != 0.0F || allZeros)) {
                            return nfe;
                        }
                    } catch (NumberFormatException var17) {
                        ;
                    }

                    try {
                        Double nfe1 = createDouble(val);
                        if (!nfe1.isInfinite() && (nfe1.doubleValue() != 0.0D || allZeros)) {
                            return nfe1;
                        }
                    } catch (NumberFormatException var16) {
                        ;
                    }

                    return createBigDecimal(val);
                }
            }
        } else {
            return createInteger(val);
        }
    }

    private static boolean isAllZeros(String s) {
        if (s == null) {
            return true;
        } else {
            for (int i = s.length() - 1; i >= 0; --i) {
                if (s.charAt(i) != 48) {
                    return false;
                }
            }

            return s.length() > 0;
        }
    }

    public static Float createFloat(String val) {
        return Float.valueOf(val);
    }

    public static Double createDouble(String val) {
        return Double.valueOf(val);
    }

    public static Integer createInteger(String val) {
        return Integer.decode(val);
    }

    public static Long createLong(String val) {
        return Long.valueOf(val);
    }

    public static BigInteger createBigInteger(String val) {
        BigInteger bi = new BigInteger(val);
        return bi;
    }

    public static BigDecimal createBigDecimal(String val) {
        BigDecimal bd = new BigDecimal(val);
        return bd;
    }

    public static long minimum(long a, long b, long c) {
        if (b < a) {
            a = b;
        }

        if (c < a) {
            a = c;
        }

        return a;
    }

    public static int minimum(int a, int b, int c) {
        if (b < a) {
            a = b;
        }

        if (c < a) {
            a = c;
        }

        return a;
    }

    public static long maximum(long a, long b, long c) {
        if (b > a) {
            a = b;
        }

        if (c > a) {
            a = c;
        }

        return a;
    }

    public static int maximum(int a, int b, int c) {
        if (b > a) {
            a = b;
        }

        if (c > a) {
            a = c;
        }

        return a;
    }

    public static int compare(double lhs, double rhs) {
        if (lhs < rhs) {
            return -1;
        } else if (lhs > rhs) {
            return 1;
        } else {
            long lhsBits = Double.doubleToLongBits(lhs);
            long rhsBits = Double.doubleToLongBits(rhs);
            return lhsBits == rhsBits ? 0 : (lhsBits < rhsBits ? -1 : 1);
        }
    }

    public static int compare(float lhs, float rhs) {
        if (lhs < rhs) {
            return -1;
        } else if (lhs > rhs) {
            return 1;
        } else {
            int lhsBits = Float.floatToIntBits(lhs);
            int rhsBits = Float.floatToIntBits(rhs);
            return lhsBits == rhsBits ? 0 : (lhsBits < rhsBits ? -1 : 1);
        }
    }

    public static boolean isDigits(String str) {
        if (str != null && str.length() != 0) {
            for (int i = 0; i < str.length(); ++i) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumber(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        } else {
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] == 45 ? 1 : 0;
            int i;
            if (sz > start + 1 && chars[start] == 48 && chars[start + 1] == 120) {
                i = start + 2;
                if (i == sz) {
                    return false;
                } else {
                    while (i < chars.length) {
                        if ((chars[i] < 48 || chars[i] > 57) && (chars[i] < 97 || chars[i] > 102) && (chars[i] < 65 || chars[i] > 70)) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;

                for (i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if (chars[i] >= 48 && chars[i] <= 57) {
                        foundDigit = true;
                        allowSigns = false;
                    } else if (chars[i] == 46) {
                        if (hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if (chars[i] != 101 && chars[i] != 69) {
                        if (chars[i] != 43 && chars[i] != 45) {
                            return false;
                        }

                        if (!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if (hasExp) {
                            return false;
                        }

                        if (!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }

                if (i < chars.length) {
                    if (chars[i] >= 48 && chars[i] <= 57) {
                        return true;
                    } else if (chars[i] != 101 && chars[i] != 69) {
                        if (!allowSigns && (chars[i] == 100 || chars[i] == 68 || chars[i] == 102 || chars[i] == 70)) {
                            return foundDigit;
                        } else if (chars[i] != 108 && chars[i] != 76) {
                            return false;
                        } else {
                            return foundDigit && !hasExp;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return !allowSigns && foundDigit;
                }
            }
        }
    }

}
