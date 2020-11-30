package com.up.habit.kit;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO:
 * <p>
 *
 * @author 王剑洪 on 2019/11/15 14:33
 */
public class StrKit extends com.jfinal.kit.StrKit {

    public static final char[] charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXY0123456789".toCharArray();

    public static final char DOT='.';

    /**逗号*/
    public static final String COMMA=",";
    public static final String LINE="-";

    /**数字正则*/
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");

    public static String getRandomString(boolean isNumber, int length) {
        Random random = new Random(System.nanoTime());
        char[] randomChars = new char[length];
        int max = charArray.length - 1;
        int min = isNumber ? 51 : 0;
        for (int i = 0; i < randomChars.length; i++) {
            randomChars[i] = charArray[random.nextInt(max) % (max - min + 1) + min];
        }

        return String.valueOf(randomChars);
    }

    public static boolean hasBlank(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLengthScope(String str, int minLength, int maxLength) {
        if (isBlank(str)) {
            return false;
        }
        return str.length() >= minLength && str.length() <= maxLength;
    }

    /**
     * 字符串切割
     */
    public static String[] split(String strs) {
        return strs.split(",");
    }


    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        /**前置字符是否大写*/
        boolean preCharIsUpperCase = true;
        /**当前字符是否大写*/
        boolean curreCharIsUpperCase = true;
        /**下一字符是否大写*/
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append("_");
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 字符串转大写
     */
    public String toUpperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }


    public static int indexOf(String seq, String searchSeq) {
        return seq != null && searchSeq != null ? seq.indexOf(searchSeq) : -1;
    }


    public static String subWithoutLastOne(String str) {
        if (StrKit.isBlank(str)) {
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    public static String removeSqlOther(String sql) {
        if (StrKit.isBlank(sql)) {
            return "";
        }
        return sql.replaceAll("\n", "").replaceAll("\t", " ").replaceAll(" +", " ");
    }

    public static String toCase(String stringWithUnderline) {
        if (stringWithUnderline.indexOf('-') == -1) {
            return stringWithUnderline;
        }

        stringWithUnderline = stringWithUnderline.toLowerCase();
        char[] fromArray = stringWithUnderline.toCharArray();
        char[] toArray = new char[fromArray.length];
        int j = 0;
        for (int i = 0; i < fromArray.length; i++) {
            if (fromArray[i] == '-') {
                i++;
                if (i < fromArray.length) {
                    toArray[j++] = Character.toUpperCase(fromArray[i]);
                }
            } else {
                toArray[j++] = fromArray[i];
            }
        }
        return new String(toArray, 0, j);
    }


    public static String substringAfter(String str, String separator) {
        if (isBlank(str)) {
            return str;
        } else if (separator == null) {
            return "";
        } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? "" : str.substring(pos + separator.length());
        }
    }


    public static boolean notNullAndEmpty(String... paras) {
        if (paras == null) {
            return false;
        }
        for (String str : paras) {
            if (str == null) {
                return false;
            } else {
                for (int i = 0, len = str.length(); i < len; i++) {
                    if (str.charAt(i) > ' ') {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * TODO:判断字符串是否仅含字母
     * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
     * 数字：[0x30,0x39]（或十进制[48, 57]）
     * 小写字母：[0x61,0x7a]（或十进制[97, 122]）
     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        return str.matches("^[a-zA-Z]*");
    }

    public static boolean isLetterAndNumber(String str) {
        return str.matches("^[a-z0-9A-Z\\u4e00-\\u9fa5]+$");
    }


    /**
     * TODO:利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */

    public static boolean isNumeric(String str) {
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * TODO；邮箱正则表达式
     * @param str
     * @return
     */
    public static  boolean isEmail(String str){
        return str.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }
}
