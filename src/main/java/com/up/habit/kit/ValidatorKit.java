package com.up.habit.kit;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO:验证
 * <p>
 *
 * @author 王剑洪 on 2019/11/27 11:02
 */
public class ValidatorKit {
    //邮箱正则
    protected static final String emailAddressPattern = "\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

    /**
     * TODO:判断是否手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return mobile.length() == 11;
    }

    public static boolean isEmail(String email) {
        return regex(email, emailAddressPattern, false);
    }


    /**
     * TODO:正则验证
     *
     * @param value           验证字符串
     * @param regExpression   正则表达式
     * @param isCaseSensitive 是否大小写敏感
     * @return
     */
    public static boolean regex(String value, String regExpression, boolean isCaseSensitive) {
        if (StrKit.isBlank(value)) {
            return false;
        }
        Pattern pattern = isCaseSensitive ? Pattern.compile(regExpression) : Pattern.compile(regExpression, 2);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }


    public static boolean isNull(Object o) {
        return o == null;
    }

    public static String genConditionIN(String str) {
        if (StrKit.isBlank(str)) {
            return "";
        }
        return str.replace(",", "','");
    }

    public static Boolean has(Object value, Object... values) {
        for (Object o : values) {
            if (o.toString().equals(value.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNull(Object... obs) {
        for (Object o : obs) {
            if (o instanceof String) {
                if (StrKit.isBlank((String) o)) {
                    return true;
                }
            } else {
                if (o == null) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * TODO:验证字符串相等。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equalStr(String str1, String str2) {
        if (str1 == null || str2 == null || (!str1.equals(str2))) {
            return false;
        }
        return true;
    }

    /**
     * TODO:验证整数相等
     *
     * @param i1
     * @param i2
     * @return
     */
    public static boolean equalInt(Integer i1, Integer i2) {
        if (i1 == null || i2 == null || (i1.intValue() != i2.intValue())) {
            return false;
        }
        return true;
    }

    /**
     * TODO:验证url
     *
     * @param value
     */
    public static boolean isUrl(String value) {
        try {
            value = value.trim();
            if (value.startsWith("https://")) {
                /* URL doesn't understand the https protocol, hack it*/
                value = "http://" + value.substring(8);
            }
            new URL(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDate(String value){
        try{
            Date temp = new SimpleDateFormat(DateKit.datePattern).parse(value.trim());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isDateTime(String value){
        try{
            Date temp = new SimpleDateFormat(DateKit.timeStampPattern).parse(value.trim());
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
