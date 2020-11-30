package com.up.habit.expand.db.dialect;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/7/27 17:47
 */
public class Logic {
    public static final String LIKE = "like";
    public static final String GT = ">";
    public static final String GE = ">=";
    public static final String LT = "<";
    public static final String LE = "<=";
    public static final String EQUALS = "=";
    public static final String NOT_EQUALS = "!=";

    public static final String IS_NULL_OR_EMPTY = "is null or empty";
    public static final String IS_NULL = "is null";
    public static final String IS_NOT_NULL = "is not null";

    public static final String IN = "in";
    public static final String NOT_IN = "not in";

    public static final String BETWEEN = "between";
    public static final String NOT_BETWEEN = "not between";


    /**
     * KEY-查询返回列的
     */
    public static final String SET_KEY_COLUMNS = "__columns";
    /**
     * KEY-查询条件,LOGIC
     */
    public static final String SET_KEY_COND = "__cond_";

    /**
     * KEY-查询条件,额外的权限
     */
    public static final String SET_KEY_AUTH = "__auth_";

    /**
     * KEY-查询排序
     */
    public static final String SET_KEY_ORDER_BY = "__order_by";
    /**
     * KEY-查询条数限制
     */
    public static final String SET_KEY_LIMIT = "__limit";
}
