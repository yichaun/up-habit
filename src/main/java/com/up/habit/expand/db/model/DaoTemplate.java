package com.up.habit.expand.db.model;

import com.jfinal.plugin.activerecord.*;
import com.jfinal.plugin.activerecord.cache.ICache;
import com.up.habit.expand.db.listener.BatchListener;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.StrKit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/28 11:33
 */
public class DaoTemplate<M extends HabitModel> extends com.jfinal.plugin.activerecord.DaoTemplate<M> {

    public DaoTemplate(HabitModel dao, String key, Map data) {
        super(dao, key, data);
        this.dao = dao;
    }

    public DaoTemplate(HabitModel dao, String key, Object... paras) {
        super(dao, key, paras);
    }

    public DaoTemplate(boolean byString, HabitModel dao, String content, Map data) {
        super(byString, dao, content, data);
        this.dao = dao;
    }

    public DaoTemplate(boolean byString, HabitModel dao, String content, Object... paras) {
        super(byString, dao, content, paras);
        this.dao = dao;
    }

    public DaoTemplate(HabitModel dao, SqlPara sqlPara) {
        super(dao,null);
        this.dao=dao;
        this.sqlPara=sqlPara;

    }

    public String getCacheName() {
        return ((HabitModel) dao)._getCacheName();
    }

    public String buildCacheKey() {
        return ((HabitModel) dao).buildCacheKey(this.sqlPara.getSql(), this.sqlPara.getPara());
    }

    public List<M> findByCache() {
        return super.findByCache(getCacheName(), buildCacheKey());
    }

    public M findFirstByCache() {
        return super.findFirstByCache(getCacheName(), buildCacheKey());
    }

    public Page<M> paginateByCache(int pageNumber, int pageSize) {
        return super.paginateByCache(getCacheName(), buildCacheKey(), pageNumber, pageSize);
    }

    public Page<M> paginateByCache(int pageNumber, int pageSize, boolean isGroupBySql) {
        return super.paginateByCache(getCacheName(), buildCacheKey(), pageNumber, pageSize, isGroupBySql);
    }

    public DbPro db() {
        return Db.use(CPI.getConfig(dao).getName());
    }

    public boolean update() {
        int count = db().update(sqlPara);
        return ((HabitModel) dao).removeAllCache(count >= 1);
    }


    /**
     * TODO:批量执行
     *
     * @return boolean
     * @Author 王剑洪 on 2020/3/28 16:16
     **/
    public boolean batch(BatchListener batch) {
        boolean res = db().tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                if (batch!=null){
                    boolean goOn = batch.batchBefore();
                    if (!goOn) {
                        return false;
                    }
                    goOn = batchNonTransaction();
                    if (!goOn) {
                        return false;
                    }
                    goOn = batch.batchAfter();
                    return goOn;
                }else {
                    return  batchNonTransaction();
                }

            }
        });
        return res;
    }

    public boolean batch() {
        return batch(new BatchListener() {
            @Override
            public boolean batchBefore() {
                return true;
            }

            @Override
            public boolean batchAfter() {
                return true;
            }
        });
    }

    public boolean batchNonTransaction() {
        String[] sqlArray = sqlPara.getSql().split(";");
        int index = 0;
        for (int i = 0; i < sqlArray.length; i++) {
            String sql = sqlArray[i];
            sql = sql.replaceAll("\n", "").replaceAll("\t", " ").replaceAll(" +", " ");
            if (StrKit.isBlank(sql)) {
                continue;
            }
            String tmp = sql.replace("?", "??");
            int paraCount = tmp.length() - sql.length();
            Object[] paras = Arrays.copyOfRange(sqlPara.getPara(), index, paraCount + index);
            int count = db().update(sqlArray[i], paras);
            index += paraCount;
            if (count < 0) {
                return false;
            }
        }
        return true;
    }


    public String queryStr() {
        return db().queryStr(sqlPara.getSql(), sqlPara.getPara());
    }

    public long queryLong() {
        Long tmp = db().queryLong(sqlPara.getSql(), sqlPara.getPara());
        if (tmp == null) {
            return 0;
        }
        return tmp.longValue();
    }

    public int queryInt() {
        Integer tmp = db().queryInt(sqlPara.getSql(), sqlPara.getPara());
        if (tmp == null) {
            return 0;
        }
        return tmp.intValue();
    }

    public BigDecimal queryBigDecimal() {
        return db().queryBigDecimal(sqlPara.getSql(), sqlPara.getPara());
    }

    public Date queryDate() {
        return db().queryDate(sqlPara.getSql(), sqlPara.getPara());
    }

    public <T> List<T> queryList() {
        return (List<T>) db().query(sqlPara.getSql(), sqlPara.getPara());
    }

    public <T> T getCache() {
        HabitModel model = (HabitModel) dao;
        return model._getCache().get(getCacheName(), buildCacheKey());
    }

    public <T> List<T> queryListByCache() {
        ICache cache = ((HabitModel) dao)._getCache();
        String key = buildCacheKey();
        List<T> result = cache.get(getCacheName(), key);
        if (result == null) {
            result = db().query(sqlPara.getSql(), sqlPara.getPara());
            cache.put(getCacheName(), key, result);
        }
        return result;
    }

    public List<Integer> queryIntList() {
        List list = db().query(sqlPara.getSql(), sqlPara.getPara());
        List<Integer> returnList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            returnList.add(Integer.parseInt(String.valueOf(list.get(i))));
        }
        return returnList;
    }

    public List<Integer> queryIntListByCache() {
        ICache cache = ((HabitModel) dao)._getCache();
        String key = buildCacheKey();
        List<Integer> result = cache.get(getCacheName(), key);
        if (result == null) {
            result = queryIntList();
            cache.put(getCacheName(), key, result);
        }
        return result;
    }

    public SqlPara getSql() {
        return this.sqlPara;
    }

}
