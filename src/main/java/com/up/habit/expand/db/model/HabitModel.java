package com.up.habit.expand.db.model;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.activerecord.cache.ICache;
import com.jfinal.plugin.activerecord.generator.ColumnMeta;
import com.up.habit.Habit;
import com.up.habit.app.config.LocalVariable;
import com.up.habit.expand.db.dialect.HabitDialect;
import com.up.habit.expand.db.dialect.Logic;
import com.up.habit.expand.safe.JsoupFilter;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.NumberKit;

import java.io.Serializable;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.locks.Condition;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 16:37
 */
public class HabitModel<M extends HabitModel<M>> extends Model<M> {

    private final static HabitModelConfig modelConfig = Habit.config(HabitModelConfig.class);

    /**
     * TODO:快捷创建条件对象
     *
     * @param <M>
     * @return
     */
    public static <M extends HabitModel<M>> M cond() {
        return (M) new HabitModel();
    }

    /**
     * TODO:查询返回的列
     *
     * @param columns
     * @return
     */
    public M loadColumns(String columns) {
        this.put(Logic.SET_KEY_COLUMNS, columns);
        return (M) this;
    }

    /**
     * TODO:查询返回的列
     *
     * @param columns
     * @return
     */
    public M loadColumnArray(String... columns) {
        String tmp = "";
        for (int i = 0; i < columns.length; i++) {
            tmp += columns[i] + ((i < columns.length - 1) ? "," : "");
        }
        this.put(Logic.SET_KEY_COLUMNS, tmp);
        return (M) this;
    }

    /**
     * TODO:获取返回列
     *
     * @return
     */
    public String loadColumns() {
        String columns = get(Logic.SET_KEY_COLUMNS);
        if (StrKit.isBlank(columns)) {
            return "*";
        } else {
            return columns.trim();
        }
    }

    /**
     * TODO:查询逻辑
     *
     * @param name
     * @param logic
     * @return M
     **/
    public M condition(String name, String logic) {
        this.put(Logic.SET_KEY_COND + name, logic);
        return (M) this;
    }

    /**
     * TODO:获取查询逻辑
     *
     * @param name
     * @return java.lang.String
     **/
    public String condition(String name) {
        return this.get(Logic.SET_KEY_COND + name);
    }


    /**
     * TODO:查询排序
     *
     * @param orderBy
     * @return
     */
    public M orderBy(String orderBy) {
        this.put(Logic.SET_KEY_ORDER_BY, orderBy);
        return (M) this;
    }

    /**
     * TODO:查询排序
     *
     * @param orderBy
     * @return
     */
    public M orderByAsc(String orderBy) {
        this.put(Logic.SET_KEY_ORDER_BY, orderBy + " asc");
        return (M) this;
    }

    /**
     * TODO:查询排序
     *
     * @param orderBy
     * @return
     */
    public M orderByDesc(String orderBy) {
        this.put(Logic.SET_KEY_ORDER_BY, orderBy + " desc");
        return (M) this;
    }

    /**
     * TODO:查询排序
     *
     * @return
     */
    public String orderBy() {
        String orderBy = get(Logic.SET_KEY_ORDER_BY);
        return StrKit.isBlank(orderBy) ? "" : orderBy;
    }


    public M auth(String key, String content) {
        Kv kv = auth();
        this.put(Logic.SET_KEY_AUTH, kv.set(key, content));
        return (M) this;
    }

    public Kv auth() {
        Kv kv = this.get(Logic.SET_KEY_AUTH);
        if (kv == null) {
            kv = Kv.create();
        }
        return kv;
    }

    /**
     * TODO:查询limit
     *
     * @param limit
     * @return
     */
    public M limit(String limit) {
        this.put(Logic.SET_KEY_LIMIT, limit);
        return (M) this;
    }

    /**
     * TODO:查询limit
     *
     * @return
     */
    public String limit() {
        String limit = get(Logic.SET_KEY_LIMIT);
        return StrKit.isBlank(limit) ? "" : limit;
    }

    public SqlPara sqlPara() {
        return _getDialect().query(_getTable(), this);
    }

    /**
     * TODO:数据源类型
     *
     * @return
     */
    public HabitDialect _getDialect() {
        Config config = _getConfig();
        return (HabitDialect) config.getDialect();
    }

    /**
     * TODO:获取缓存
     *
     * @return
     */
    public ICache _getCache() {
        return this._getConfig().getCache();
    }

    /**
     * TODO:获取表的缓存空间
     *
     * @param
     * @return
     * @author 王剑洪 on 2019/12/26 13:55
     **/
    public String _getCacheName() {
        return _getConfig().getName() + ":" + _getTable().getName();
    }

    /**
     * TODO:获取缓存key
     *
     * @param sql
     * @param paras
     * @return
     * @author 王剑洪 on 2019/12/26 13:55
     **/
    public String buildCacheKey(String sql, Object... paras) {
        String key = "sql:" + sql;
        String cond = "";
        if (paras != null && paras.length > 0) {
            for (int i = 0; i < paras.length; i++) {
                cond += paras[i];
                if (i < paras.length - 1) {
                    cond += ",";
                }
            }
        }
        key = key.replaceAll("\n", "")
                .replaceAll("\t", " ")
                .replaceAll(" +", " ");
        return key + ":[" + cond + "]";
    }

    /**
     * TODO:移除缓存
     *
     * @param res
     * @return
     */
    public boolean removeAllCache(boolean res) {
        /**日志类缓存不处理*/
        String tableName = _getTable().getName();
        if (res && (!tableName.startsWith("log") && !tableName.endsWith("log"))) {
            Habit.getCache().removeAll(_getCacheName());
        }
        return res;
    }

    /**
     * TODO:过滤html标签的字段
     *
     * @param
     * @return
     * @author 王剑洪 on 2019/12/26 13:43
     **/
    @Override
    protected void filter(int filterBy) {
        if (StrKit.notBlank(modelConfig.getFilterHtml())) {
            String[] columnNameArray = ArrayKit.toStrArray(modelConfig.getFilterHtml());
            for (String name : columnNameArray) {
                if (_getTable().hasColumnLabel(name)) {
                    if (get(name) != null) {
                        set(name, JsoupFilter.getText(get(name)));
                    }

                }
            }
        }
    }

    @Override
    public boolean save() {
        remove(modelConfig.getCreated(), modelConfig.getCreatedId(), modelConfig.getModified(), modelConfig.getModifyId());
        if (_getTable().hasColumnLabel(modelConfig.getDel())) {
            if (get(modelConfig.getDel()) == null) {
                setDel(false);
            }
        }
        if (_getTable().hasColumnLabel(modelConfig.getDel())) {
            set(modelConfig.getDel(), false);
        }
        if (_getTable().hasColumnLabel(modelConfig.getCreated())) {
            set(modelConfig.getCreated(), new Date());
        }
        if (_getTable().hasColumnLabel(modelConfig.getCreatedId())) {
            set(modelConfig.getCreatedId(), LocalVariable.ME.getAdmin());
        }
        if (_getTable().hasColumnLabel(modelConfig.getSort())) {
            Integer sort = getInt(modelConfig.getSort());
            sort = NumberKit.isScope(sort, 1, 99999) ? sort : 1;
            set(modelConfig.getSort(), sort);
        }
        /**主键未设置,默认成uuid*/
        String[] primaryKeyArray = _getTable().getPrimaryKey();
        if (primaryKeyArray != null && primaryKeyArray.length == 1) {
            if (_getTable().getColumnTypeMap().get(primaryKeyArray[0]).isAssignableFrom(String.class) && StrKit.isBlank(get(primaryKeyArray[0]))) {
                set(primaryKeyArray[0], StrKit.getRandomUUID());
            }
        }
        return removeAllCache(super.save());
    }

    @Override
    public boolean delete() {
        return removeAllCache(super.delete());
    }

    @Override
    public boolean deleteById(Object idValue) {
        return removeAllCache(super.deleteById(idValue));
    }

    @Override
    public boolean deleteByIds(Object... idValues) {
        return removeAllCache(super.deleteByIds(idValues));
    }

    /**
     * TODO:批量删除
     *
     * @param ids
     * @return
     */
    public boolean deleteInIds(Object[]... ids) {
        if (ids == null || ids.length < 1) {
            throw new IllegalArgumentException("ids value error,need length >=1");
        }
        String sql = _getDialect().deleteInIds(_getTable(), ids);
        return removeAllCache(Db.use(_getConfig().getName()).update(sql, ArrayKit.two2OneArray(ids)) >= 1);
    }


    /**
     * TODO:批量伪删除
     *
     * @param ids
     * @return
     */
    public boolean removeInIds(Object[]... ids) {
        if (ids == null || ids.length < 1) {
            throw new IllegalArgumentException("ids value error,need length >=1");
        }
        String sql = _getDialect().removeInIds(_getTable(), ids);
        return removeAllCache(Db.use(_getConfig().getName()).update(sql, ArrayKit.two2OneArray(ids)) >= 1);
    }

    @Override
    public boolean update() {
        remove(modelConfig.getCreated(), modelConfig.getCreatedId(), modelConfig.getModified(), modelConfig.getModifyId());
        if (_getTable().hasColumnLabel(modelConfig.getModified())) {
            set(modelConfig.getModified(), new Date());
        }
        if (_getTable().hasColumnLabel(modelConfig.getModifyId())) {
            set(modelConfig.getModifyId(), LocalVariable.ME.getAdmin());
        }
        if (_getTable().hasColumnLabel(modelConfig.getSort())) {
            Integer sort = getInt(modelConfig.getSort());
            sort = NumberKit.isScope(sort, 1, 99999) ? sort : 1;
            set(modelConfig.getSort(), sort);
        }
        return removeAllCache(super.update());
    }

    public M findById(M m) {
        return findByIdLoadColumns(m, "*");
    }

    public M findByIdLoadColumns(M m, String columns) {
        Table table = _getTable();
        Object[] idValues = new Object[table.getPrimaryKey().length];
        for (int i = 0; i < idValues.length; i++) {
            if (table.getPrimaryKey()[i] == null) {
                throw new IllegalArgumentException("id values error, need " + table.getPrimaryKey().length + " id value");
            }
            idValues[i] = m.get(table.getPrimaryKey()[i]);
        }
        Config config = _getConfig();
        String sql = config.getDialect().forModelFindById(table, columns);
        List<M> result = find(config, sql, idValues);
        return result.size() > 0 ? result.get(0) : null;
    }

    public M findByIdByCache(Object idValue) {
        ICache cache = this._getConfig().getCache();
        String key = buildCacheKey("id", idValue);
        M result = cache.get(_getCacheName(), key);
        if (result == null) {
            result = this.findById(idValue);
            cache.put(_getCacheName(), key, result);
        }
        return result;
    }

    public M findByIdsByCache(Object... idValues) {
        ICache cache = this._getConfig().getCache();
        String key = buildCacheKey("id", idValues);
        M result = cache.get(_getCacheName(), key);
        if (result == null) {
            result = this.findByIds(idValues);
            cache.put(_getCacheName(), key, result);
        }
        return result;
    }

    public M findFirst() {
        this.limit("1");
        return dao().findFirst(_getDialect().query(_getTable(), this));
    }

    public M findFirstByCache() {
        this.limit("1");
        return dao().findFirstByCache(_getDialect().query(_getTable(), this));
    }

    public M findFirst(M m) {
        m.limit("1");
        return super.findFirst(_getDialect().query(_getTable(), m));
    }

    public M findFirstByCache(M m) {
        m.limit("1");
        return findFirstByCache(_getDialect().query(_getTable(), m));
    }

    public M findFirstByCache(String sql) {
        return super.findFirstByCache(_getCacheName(), buildCacheKey(sql, new Object[0]), sql);
    }

    public M findFirstByCache(String sql, Object... paras) {
        return super.findFirstByCache(_getCacheName(), buildCacheKey(sql, paras), sql, paras);
    }

    public M findFirstByCache(SqlPara sqlPara) {
        return super.findFirstByCache(_getCacheName(), buildCacheKey(sqlPara.getSql(), sqlPara.getPara()), sqlPara.getSql(), sqlPara.getPara());
    }

    public List<M> find() {
        return dao().find(_getDialect().query(_getTable(), this));
    }

    public List<M> findByCache() {
        return dao().findByCache(_getDialect().query(_getTable(), this));
    }

    public List<M> find(M m) {
        return super.find(_getDialect().query(_getTable(), m));
    }

    public List<M> findByCache(M m) {
        return findByCache(_getDialect().query(_getTable(), m));
    }

    public List<M> findByCache(String sql) {
        return super.findByCache(_getCacheName(), buildCacheKey(sql, new Object[0]), sql);
    }

    public List<M> findByCache(String sql, Object... paras) {
        return super.findByCache(_getCacheName(), buildCacheKey(sql, paras), sql, paras);
    }

    public List<M> findByCache(SqlPara sqlPara) {
        return super.findByCache(_getCacheName(), buildCacheKey(sqlPara.getSql(), sqlPara.getPara()), sqlPara.getSql(), sqlPara.getPara());
    }

    /**
     * TODO:查询列表
     *
     * @param m
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<M> paginate(int pageNo, int pageSize, M m) {
        SqlPara sqlPara = _getDialect().query(_getTable(), m);
        return paginate(pageNo, pageSize, sqlPara);
    }

    /**
     * TODO:查询列表
     *
     * @param m
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<M> paginateByCache(M m, int pageNo, int pageSize) {
        SqlPara sqlPara = _getDialect().query(_getTable(), m);
        String[] sqls = PageSqlKit.parsePageSql(sqlPara.getSql());
        return paginateByCache(pageNo, pageSize, sqls[0], sqls[1], sqlPara.getPara());
    }

    public Page<M> paginateByCache(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        return super.paginateByCache(_getCacheName(), buildCacheKey(select + sqlExceptSelect + ":page:" + pageNumber + ":" + pageSize, new Object[0]),
                pageNumber, pageSize, select, sqlExceptSelect);
    }

    public Page<M> paginateByCache(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        return super.paginateByCache(_getCacheName(), buildCacheKey(select + sqlExceptSelect + ":page:" + pageNumber + ":" + pageSize, paras), pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public Page<M> paginateByCache(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras) {
        return super.paginateByCache(_getCacheName(), buildCacheKey(select + sqlExceptSelect + ":page:" + pageNumber + ":isGroupBySql" + pageSize, paras), pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    public String templateKey(String key) {
        return key.contains(".") ? key : (_getTable().getName() + "." + key);
    }

    @Override
    public DaoTemplate<M> template(String key, Map data) {
        key = key.contains(".") ? key : (_getTable().getName() + "." + key);
        return new DaoTemplate(this, key, data);
    }

    @Override
    public DaoTemplate<M> template(String key, Object... paras) {
        key = key.contains(".") ? key : (_getTable().getName() + "." + key);
        return new DaoTemplate(this, key, paras);
    }

    @Override
    public DaoTemplate<M> template(String key, Model model) {
        key = key.contains(".") ? key : (_getTable().getName() + "." + key);
        if (model == null) {
            return this.template(key);
        }
        return (DaoTemplate<M>) super.template(key, model);
    }

    @Override
    public DaoTemplate<M> templateByString(String content, Map data) {
        if (data == null) {
            return this.templateByString(content);
        }
        return new DaoTemplate(true, this, content, data);
    }

    @Override
    public DaoTemplate<M> templateByString(String content, Object... paras) {
        return new DaoTemplate(true, this, content, paras);
    }

    @Override
    public DaoTemplate<M> templateByString(String content, Model model) {
        if (model == null) {
            return this.templateByString(content);
        }
        return (DaoTemplate<M>) super.templateByString(content, model);
    }

    public DaoTemplate<M> template(HabitModel m) {
        SqlPara sqlPara = m._getDialect().query(m._getTable(), m);
        return new DaoTemplate<M>(this, sqlPara);
    }

    /*****************************************************************通用字段*****************************************************************/
    /**
     * 设置排序
     */
    public M setSort(java.lang.Integer sort) {
        set(modelConfig.getSort(), sort);
        return (M) this;
    }

    /**
     * 获取排序
     */
    public java.lang.Integer getSort() {
        return getInt(modelConfig.getSort());
    }

    /**
     * 设置状态
     */
    public M setState(java.lang.String state) {
        set(modelConfig.getState(), state);
        return (M) this;
    }

    /**
     * 获取状态
     */
    public java.lang.String getState() {
        return getStr(modelConfig.getState());
    }

    /**
     * 是否删除
     */
    public M setDel(java.lang.Boolean isDel) {
        set(modelConfig.getDel(), isDel);
        return (M) this;
    }

    /**
     * 是否删除
     */
    public java.lang.Boolean getDel() {
        return getBoolean(modelConfig.getDel());
    }

    /**
     * 设置创建者
     */
    public M setCreateBy(java.lang.Integer createBy) {
        set(modelConfig.getCreatedId(), createBy);
        return (M) this;
    }

    /**
     * 获取创建者
     */
    public java.lang.Integer getCreateBy() {
        return getInt(modelConfig.getCreatedId());
    }

    /**
     * 设置创建时间
     */
    public M setCreateTime(java.util.Date createTime) {
        set(modelConfig.getCreated(), createTime);
        return (M) this;
    }

    /**
     * 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return get(modelConfig.getCreated());
    }

    /**
     * 设置更新者
     */
    public M setModifyBy(java.lang.Integer modifyBy) {
        set(modelConfig.getModifyId(), modifyBy);
        return (M) this;
    }

    /**
     * 获取更新者
     */
    public java.lang.Integer getModifyBy() {
        return getInt(modelConfig.getModifyId());
    }

    /**
     * 设置更新时间
     */
    public M setModifyTime(java.util.Date modifyTime) {
        set(modelConfig.getModified(), modifyTime);
        return (M) this;
    }

    /**
     * 获取更新时间
     */
    public java.util.Date getModifyTime() {
        return get(modelConfig.getModified());
    }
    /*****************************************************************通用字段*****************************************************************/
}
