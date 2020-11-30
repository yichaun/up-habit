package com.up.habit.expand.gen;

import com.up.habit.Habit;
import com.up.habit.expand.gen.model.Table;
import com.up.habit.expand.gen.model.TableColumn;
import com.up.habit.expand.db.model.HabitModelConfig;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/18 15:07
 */
public class GeneratorKit {
    static HabitModelConfig modelConfig = Habit.config(HabitModelConfig.class);
    ;
    static Set<String> defColumns = new HashSet<String>() {{
        this.add(modelConfig.getCreated());
        this.add(modelConfig.getCreatedId());
        this.add(modelConfig.getModified());
        this.add(modelConfig.getModifyId());
        this.add(modelConfig.getDel());
        this.add(modelConfig.getState());
        this.add(modelConfig.getSort());
    }};

    /**
     * TODO:是否默认字段
     *
     * @param columnName
     * @return
     */
    public static boolean notDefColumn(String columnName) {
        return !defColumns.contains(columnName);
    }







}
