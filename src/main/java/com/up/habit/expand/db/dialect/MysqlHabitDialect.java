package com.up.habit.expand.db.dialect;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.up.habit.Habit;
import com.up.habit.exception.HabitValidateException;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.expand.db.model.HabitModelConfig;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/19 23:37
 */
public class MysqlHabitDialect extends MysqlDialect implements HabitDialect {


    HabitModelConfig config = Habit.config(HabitModelConfig.class);
    private Set<String> dbKeyword = new HashSet<String>() {{
        this.add("group");
        this.add("to");
        this.add("read");
    }};

    /**
     * TODO:主键条件拼接
     *
     * @param table
     * @param ids
     * @return
     */
    private String appendCond(Table table, Object[]... ids) {
        String[] primaryKeys = table.getPrimaryKey();
        String cond = "";
        if (primaryKeys.length < 1) {
            throw new HabitValidateException("there is no primary key");
        } else if (primaryKeys.length == 1) {
            if (ids[0].length < 1) {
                throw new HabitValidateException("ids value error");
            }
            for (int i = 0; i < ids[0].length; i++) {
                if (StrKit.isBlank(String.valueOf(ids[0][i]))) {
                    throw new HabitValidateException("ids value error,can not null");
                }
                cond += "?,";
            }
            cond = primaryKeys[0] + " in(" + cond.substring(0, cond.length() - 1) + ")";
        } else {
            int count = -1;
            String item = "";
            if (ids.length != primaryKeys.length) {
                throw new HabitValidateException("ids value error");
            }
            for (int i = 0; i < primaryKeys.length; i++) {
                item += primaryKeys[i] + " = ? AND ";
                if (count != -1) {
                    if (ids[i].length != count) {
                        throw new HabitValidateException("ids length error");
                    } else {
                        if (ids.length < 1) {
                            throw new HabitValidateException("ids value error");
                        }
                        count = ids[i].length;
                    }
                }
            }
            item = "(" + item.substring(0, item.length() - 4) + ") OR ";
            for (int i = 0; i < count; i++) {
                cond += item;
            }
            cond = cond.substring(0, cond.length() - 4);
        }
        return cond;
    }

    @Override
    public Set<String> dbKeyword() {
        return dbKeyword;
    }

    @Override
    public String deleteInIds(Table table, Object[]... ids) {
        String sql = "delete from " + table.getName() + " where " + appendCond(table, ids);
        return sql;
    }


    @Override
    public String removeInIds(Table table, Object[]... ids) {
        if (!table.hasColumnLabel(config.del)) {
            throw new HabitValidateException("there is no column " + config.del);
        }
        String sql = "update " + table.getName() + " set " + config.del + "=1 where " + appendCond(table, ids);
        return sql;
    }

    @Override
    public <M extends HabitModel> SqlPara query(Table table, M m) {
        SqlPara sqlPara = new SqlPara();
        String limit = StrKit.isBlank(m.limit()) ? "" : (" limit " + m.limit());
        String orderBy = StrKit.isBlank(m.orderBy()) ? "" : ("order by " + m.orderBy());
        String sql = "select " + m.loadColumns() + " from " + table.getName() + " {where} " + orderBy + limit;
        Map<String, Class<?>> columns = table.getColumnTypeMap();
        Set<String> conditionNames = columns.keySet();
        List<String> primaryKeyList = Arrays.asList(table.getPrimaryKey());
        String conditionContent = "";
        /*条件*/
        for (String name : conditionNames) {
            String value = m.getStr(name);
            String start = StrKit.isBlank(m.getStr("start_" + name)) ? m.getStr(name + "[0]") : m.getStr("start_" + name);
            String end = StrKit.isBlank(m.getStr("end_" + name)) ? m.getStr(name + "[1]") : m.getStr("end_" + name);
            String logic = m.condition(name);
            if (StrKit.isBlank(value) && StrKit.isBlank(start) && StrKit.isBlank(end) && StrKit.isBlank(logic)) {
                continue;
            }
            conditionContent += buildConditionItem(m, name, primaryKeyList.contains(name), columns.get(name).getName(), sqlPara);
        }
        conditionContent += buildAuth(m);
        if (StrKit.notBlank(conditionContent)) {
            conditionContent = conditionContent.replaceFirst("and", "where");
        }
        sql = sql.replace("{where}", conditionContent);
        sqlPara.setSql(sql);
        return sqlPara;
    }

    public <M extends HabitModel> String buildAuth(M m) {
        Kv auth =  m.auth();
        Set<String> keys = auth.keySet();
        String authSql = "";
        for (String key : keys) {
            String content = auth.getStr(key);
            if (!content.equals("_all")) {
                String name = dbKeyword().contains(key) ? (CPI.getTable(m).getName() + "." + key) : key;
                authSql += "and " + name + " in (" + content + ")";
            }
        }
        return authSql;

    }

    /**
     * TODO:条件组装
     *
     * @param m
     * @param name
     * @param isPk
     * @param columnType
     * @param sqlPara
     * @return java.lang.String
     * @Author 王剑洪 on 2020/7/2 2:34
     **/
    public <M extends HabitModel> String buildConditionItem(M m, String name, boolean isPk, String columnType, SqlPara sqlPara) {
        String key = dbKeyword().contains(name) ? (CPI.getTable(m).getName() + "." + name) : name;
        Object value = m.get(name);
        String startValue = StrKit.isBlank(m.getStr("start_" + name)) ? m.getStr(name + "[0]") : m.getStr("start_" + name);
        String endValue = StrKit.isBlank(m.getStr("end_" + name)) ? m.getStr(name + "[1]") : m.getStr("end_" + name);
        boolean hasValue = (value instanceof String && StrKit.notBlank((String) value)) || value != null;
        String logic = m.condition(name);
        /**列值为空,则不进行查询*/
        if (!hasValue && StrKit.isBlank(startValue) && StrKit.isBlank(endValue)) {
            boolean notNeedValueLogic = Logic.IS_NULL_OR_EMPTY.equals(logic) || Logic.IS_NULL.equals(logic) || Logic.IS_NOT_NULL.equals(logic);
            if (!notNeedValueLogic) {
                return "";
            }
        }
        if (value instanceof Boolean) {
            value = m.getBoolean(name) ? "1" : "0";
        }
        if (StrKit.isBlank(logic) && value != null && value.getClass().isArray()) {
            logic = Logic.IN;
        }
        String conditionItem = "";
        if (Logic.LIKE.equals(logic)) {
            sqlPara.addPara(value);
            conditionItem = key + " like concat('%',?, '%') ";
        } else if (Logic.GT.equals(logic) || Logic.GE.equals(logic) || Logic.LT.equals(logic)
                || Logic.LE.equals(logic) || Logic.EQUALS.equals(logic) || Logic.NOT_EQUALS.equals(logic)) {
            sqlPara.addPara(value);
            conditionItem = key + " " + logic + " ? ";
        } else if (Logic.IS_NULL_OR_EMPTY.equals(logic)) {
            conditionItem = "(" + key + " is null or " + name + " = '')";
        } else if (Logic.IS_NULL.equals(logic) || Logic.IS_NOT_NULL.equals(logic)) {
            conditionItem = key + " " + logic + " ";
        } else if (Logic.IN.equals(logic) || Logic.NOT_IN.equals(logic)) {
            String in = "";
            if (value instanceof Object[]) {
                Object[] tmp = (Object[]) value;
                for (int i = 0; i < tmp.length; i++) {
                    sqlPara.addPara(tmp[i]);
                    in += "?,";
                }
            } else {
                String[] tmp = ArrayKit.toStrArray((String) value);
                for (int i = 0; i < tmp.length; i++) {
                    sqlPara.addPara(tmp[i]);
                    in += "?,";
                }
            }
            conditionItem = key + " " + logic + " (" + in.substring(0, in.length() - 1) + ") ";
        } else if (Logic.BETWEEN.equals(logic) || Logic.NOT_BETWEEN.equals(logic)) {
            if (StrKit.notBlank(startValue, endValue)) {
                sqlPara.addPara(startValue);
                sqlPara.addPara(endValue);
                conditionItem = key + " " + logic + " ? and ? ";
            }
        } else {
            /**字段类型是字符串,且不是主键,则like,否则=*/
            if (hasValue) {
                logic = "java.lang.String".equals(columnType) && !isPk ? Logic.LIKE : Logic.EQUALS;
            } else {
                boolean hasStart = StrKit.notBlank(startValue);
                boolean hasEnd = StrKit.notBlank(endValue);
                if (hasStart && hasEnd) {
                    logic = Logic.BETWEEN;
                    m.remove(name);
                } else if (hasStart) {
                    logic = Logic.GE;
                    m.set(name, startValue);
                } else if (hasEnd) {
                    logic = Logic.LE;
                    m.set(name, endValue);
                }
            }
            m.condition(name, logic);
            return buildConditionItem(m, name, isPk, columnType, sqlPara);
        }
        return StrKit.notBlank(conditionItem) ? "and " + conditionItem : "";
    }
}
