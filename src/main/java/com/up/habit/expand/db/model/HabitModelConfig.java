package com.up.habit.expand.db.model;

import com.up.habit.expand.config.Config;

import java.io.Serializable;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 16:26
 */
@Config(prefix = "db.model")
public class HabitModelConfig implements Serializable {
    /**
     * 创建时间
     */
    public String created="create_time";
    /**
     * 创建者
     */
    public String createdId="create_by";
    /**
     * 修改时间
     */
    public String modified="modify_time";
    /**
     * 修改者
     */
    public String modifyId="modify_by";
    /**
     * 排序字段
     */
    public String sort="sort";

    /**
     * 删除标识
     */
    public String state="state";


    /**
     * 删除标识
     */
    public String del="del";
    /**
     * 需要过滤HTML标签的字段
     */
    public String filterHtml;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getFilterHtml() {
        return filterHtml;
    }

    public void setFilterHtml(String filterHtml) {
        this.filterHtml = filterHtml;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
