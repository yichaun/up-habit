package com.up.habit.kit;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/11/27 10:30
 */
public class NumberKit {
    /**
     * @param number
     * @param min
     * @param max
     * @return
     */
    public static boolean isScope(Integer number, int min, int max) {
        if (number == null) {
            return false;
        }
        return number.intValue() >= min && number.intValue() <= max;
    }


    /**
     * @param number
     * @return
     */
    public static boolean isSwitch(Integer number) {
        if (number == null) {
            return false;
        }
        return number.intValue() >= 0 && number.intValue() <= 1;
    }

    /**
     * TODO:判断number是否是指定的value值
     *
     * @param number
     * @param values
     * @return
     */
    public static boolean has(Integer number, int... values) {
        if (number == null) {
            return false;
        }
        for (int value : values) {
            if (value == number.intValue()) {
                return true;
            }
        }
        return false;
    }
}
