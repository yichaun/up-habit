package com.up.habit.kit;

import java.util.*;

/**
 * TODO:
 * create by 王剑洪 on 2019/10/27 21:39
 */
public class ArrayKit {

    public static boolean isNotEmpty(Collection list) {
        return list != null && list.size() > 0;
    }

    public static boolean isNotEmpty(Map map) {
        return map != null && map.size() > 0;
    }

    public static boolean isNotEmpty(Object[] objects) {
        return objects != null && objects.length > 0;
    }

    public static boolean isNullOrEmpty(Collection list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNullOrEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    public static <T> T[] concat(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * TODO:转换为String数组
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return java.lang.String[]
     * @Author 王剑洪 on 2019/12/15 22:41
     **/
    public static String[] toStrArray(String str, String split) {
        if (StrKit.isBlank(str)) {
            return new String[0];
        }
        return str.split(split);
    }

    public static String[] toStrArray(String str) {
        return toStrArray(str, ",");
    }

    public static Object[] two2OneArray(Object[]... objects) {
        int len = 0;
        for (Object[] obj : objects) {
            len += obj.length;
        }
        Object[] tmp = new Object[len];
        int index = 0;
        for (Object[] obj : objects) {
            for (Object o : obj) {
                tmp[index++] = o;
            }
        }
        return tmp;
    }


    /**
     * TODO:移除列表中的null或者空值
     *
     * @param list
     * @return java.util.List<T>
     * @Author 王剑洪 on 2020/7/18 11:20
     **/
    public static <T> List<T> removeNullAndEmpty(List<T> list) {
        for (Iterator<T> iter = list.iterator(); iter.hasNext(); ) {
            T t = iter.next();
            boolean isNull = t == null;
            boolean isEmpty=t instanceof String&&StrKit.isBlank((String) t);

            if (t == null||isEmpty) {
                iter.remove();
            }
        }
        return list;
    }

    /**
     * TODO:替换列表中的元素
     *
     * @param list
     * @param oldValue
     * @param newValue
     * @param onlyOne
     * @return java.util.List<T>
     * @Author 王剑洪 on 2020/7/18 11:23
     **/
    public static <T> List<T> replace(List<T> list,T oldValue,T newValue,boolean onlyOne) {
        for (int i=0;i<list.size();i++){
            if (oldValue.equals(list.get(i))){
                list.set(i,newValue);
                if (onlyOne){
                    break;
                }
            }
        }
        return list;
    }

}
