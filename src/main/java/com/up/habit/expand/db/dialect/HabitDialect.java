package com.up.habit.expand.db.dialect;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.up.habit.expand.db.model.HabitModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/19 22:53
 */
public interface HabitDialect {


    /**
     * 数据源关键字
     *
     * @return
     */
    Set<String> dbKeyword();

    /**
     * TODO:批量删除
     *
     * @param table
     * @param ids
     * @return java.lang.String
     * @Author 王剑洪 on 2020/5/19 23:36
     **/
    String deleteInIds(Table table, Object[]... ids);


    /**
     * TODO:批量伪删除
     *
     * @param table
     * @param ids
     * @return java.lang.String
     * @Author 王剑洪 on 2020/5/19 23:36
     **/
    String removeInIds(Table table, Object[]... ids);

    /**
     * TODO:查询
     *
     * @param table
     * @param m
     * @param <M>
     * @return
     */
    <M extends HabitModel> SqlPara query(Table table, M m);


}
