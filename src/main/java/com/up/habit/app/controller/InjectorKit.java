package com.up.habit.app.controller;

import com.jfinal.core.Controller;
import com.jfinal.core.converter.TypeConverter;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.up.habit.kit.GsonKit;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.StrKit;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/22 13:55
 */
public class InjectorKit {

    private static <T> T createInstance(Class<T> objClass) {
        try {
            return objClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO:getModel,把所有参数都集合到一个model里面
     *
     * @param modelClass
     * @param request
     * @param <T>
     * @return
     */
    public static final <T> T injectAllInModel(Class<T> modelClass, HttpServletRequest request) {
        Object temp = createInstance(modelClass);
        if (temp instanceof HabitModel == false) {
            throw new IllegalArgumentException("getModel only support class of HabitModel, using getBean for other class.");
        }
        HabitModel<?> model = (HabitModel<?>) temp;
        Table table = TableMapping.me().getTable(model.getClass());
        if (table == null) {
            throw new ActiveRecordException("The Table mapping of model: " + modelClass.getName() +
                    " not exists or the ActiveRecordPlugin not start.");
        }
        Map<String, String[]> parasMap = request.getParameterMap();
        TypeConverter converter = TypeConverter.me();
        // 对 paraMap进行遍历而不是对table.getColumnTypeMapEntrySet()进行遍历，以便支持 CaseInsensitiveContainerFactory
        // 以及支持界面的 attrName有误时可以感知并抛出异常避免出错
        for (Map.Entry<String, String[]> entry : parasMap.entrySet()) {
            String paraName = entry.getKey();
            String attrName = paraName;
            Class<?> colType = table.getColumnType(attrName);
            String[] paraValueArray = entry.getValue();
            String value = (paraValueArray != null && paraValueArray.length > 0) ? paraValueArray[0] : null;
            if (colType == null) {
                model.put(attrName, value);
            } else {
                try {
                    Object o = value != null ? converter.convert(colType, value) : null;
                    model.set(attrName, o);
                } catch (ParseException e) {
                    throw new RuntimeException("Can not convert parameter: " + paraName, e);
                }
            }
        }
        return (T) model;
    }

    /**
     * TODO:数组参数
     *
     * @param name
     * @param request
     * @return
     */
    private static TreeMap<Integer, String> injectArrayTreeMap(String name, HttpServletRequest request) {
        String regex = name + "\\[\\d*\\]";
        Pattern pattern = Pattern.compile(regex);
        TreeMap<Integer, String> treeMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return integer - t1;
            }
        });
        Enumeration<String> paraNames = request.getParameterNames();
        for (Enumeration<String> e = paraNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            if (pattern.matcher(thisName).matches()) {
                int index = Integer.parseInt(thisName.replace(name + "[", "").replace("]", ""));
                treeMap.put(index, request.getParameter(thisName));
            }
        }
        if (treeMap.size() == 0 && StrKit.notBlank(request.getParameter(name))) {
            String[] array = request.getParameter(name).split(",");
            for (int i = 0; i < array.length; i++) {
                treeMap.put(i, array[i]);
            }
        }
        return treeMap;
    }

    public static String[] injectStrArray(String name, HttpServletRequest request) {
        TreeMap<Integer, String> treeMap = injectArrayTreeMap(name, request);
        Iterator iter = treeMap.entrySet().iterator();
        String[] para = new String[treeMap.size()];
        while (iter.hasNext()) {
            Map.Entry<Integer, String> entry = (Map.Entry) iter.next();
            para[entry.getKey()] = entry.getValue();
        }
        return para;
    }


    public static Integer[] injectIntArray(String name, HttpServletRequest request) {
        TreeMap<Integer, String> treeMap = injectArrayTreeMap(name, request);
        Iterator iter = treeMap.entrySet().iterator();
        Integer[] para = new Integer[treeMap.size()];
        while (iter.hasNext()) {
            Map.Entry<Integer, String> entry = (Map.Entry) iter.next();
            try {
                para[entry.getKey()] = Integer.parseInt(entry.getValue());
            } catch (Exception e) {
                throw new IllegalArgumentException("name[" + entry.getKey() + "] is not integer");
            }

        }
        return para;
    }

    public static <T> T injectJsonToBean(String key, Type type, Controller controller) {
        String value = controller.get(key);
        if (StrKit.notBlank(value)) {
            return GsonKit.parse(key, type);
        }
        return null;
    }


}
