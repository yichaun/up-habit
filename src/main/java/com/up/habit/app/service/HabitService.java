package com.up.habit.app.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.*;
import com.up.habit.app.controller.render.To;
import com.up.habit.exception.HabitValidateException;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.StrKit;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/26 1:25
 */
public class HabitService<M extends HabitModel<M>> {
    protected M dao = null;

    public HabitService() {
        dao = init();
    }

    /**
     * TODO:初始化Dao
     *
     * @return
     */
    protected M init() {
        Type type = ClassKit.getUsefulClass(getClass()).getGenericSuperclass();
        Class<M> modelClass = (Class<M>) ((ParameterizedType) type).getActualTypeArguments()[0];
        if (modelClass == null) {
            throw new RuntimeException("can not get model class name in HabitService");
        }
        return ClassKit.newInstance(modelClass, false).dao();
    }

    public M dao() {
        return this.dao;
    }



    public DbPro db() {
        return Db.use(CPI.getConfig(dao).getName());
    }

    public DbPro db(String config) {
        return Db.use(config);
    }

    /**
     * TODO:新增
     *
     * @param m
     * @return
     */
    public To add(M m) {
        return To.to(m.save());
    }

    /**
     * TODO:批量删除
     *
     * @param ids
     * @return
     */
    public To delete(Object[]... ids) {
        if (ids.length < 1) {
            return To.fail("删除对象不能为空");
        }
        return To.to(dao.deleteInIds(ids));
    }



    /**
     * TODO:批量伪删除
     *
     * @param ids
     * @return
     */
    public To remove(Object[]... ids) {
        if (ids.length < 1) {
            return To.fail("删除对象不能为空");
        }
        return To.to(dao.removeInIds(ids));
    }

    /**
     * TODO:更新
     *
     * @param m
     * @return
     */
    public To edit(M m) {
        M old = dao.findById(m);
        if (old == null) {
            return To.fail("记录不存在");
        }
        return To.to(m.update());
    }

    /**
     * Todo:分页查找
     *
     * @param m
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<M> page(M m, int pageNo, int pageSize) {
        return dao.paginate(pageNo, pageSize, m);
    }

    /**
     * TODO:列表查询
     *
     * @param m
     * @return
     */
    public List<M> list(M m) {
        return dao.find(m);
    }

    /**
     * TODO:获取详情
     *
     * @param ids
     * @return
     */
    public M info(Object... ids) {
        if (ids.length > 1) {
            return dao.findByIds(ids);
        } else {
            return dao.findById(ids[0]);
        }
    }

}
