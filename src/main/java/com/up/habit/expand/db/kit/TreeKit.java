package com.up.habit.expand.db.kit;

import com.jfinal.kit.Kv;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.util.*;

/**
 * TODO:树
 *
 * @author 王剑洪 on 2020/1/3 14:02
 */
public class TreeKit {

    public static final String HAS = "hasChildren";
    public static final String CHILDREN = "children";


    private static <T extends HabitModel> Kv buildMapAndList(List<T> list, String pIdKey, String idKey) {
        TreeMap<Object, List<T>> map = new TreeMap();
        List<Object> tmpList = new ArrayList<>();
        for (T model : list) {
            tmpList.add(model.get(idKey));
            Object key = model.get(pIdKey);
            if (key != null && (StrKit.notBlank(String.valueOf(key)))) {
                List<T> tmp = map.get(key) == null ? new ArrayList<>() : map.get(key);
                tmp.add(model);
                map.put(key, tmp);
            }
        }
        return Kv.by("map", map).set("tempList", tmpList);
    }

    /**
     * TODO:list转换成树list
     *
     * @param list
     * @param pIdKey
     * @param idKey
     * @param <T>
     * @return
     */
    public static <T extends HabitModel> List<T> buildTree(List<T> list, String pIdKey, String idKey) {
        Kv kv = buildMapAndList(list, pIdKey, idKey);
        TreeMap<Object, List<T>> map = (TreeMap<Object, List<T>>) kv.get("map");
        List<Object> tmpList = (List<Object>) kv.get("tempList");
        List<T> rootList = new ArrayList<>();
        for (Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            T model = iterator.next();
            if (!tmpList.contains(model.get(pIdKey))) {
                rootList.add(model);
            }
        }
        rootList = getNextList(rootList, map, idKey);
        return rootList;
    }

    /**
     * TODO:递归获取下级模块
     *
     * @param list
     * @param map
     **/
    private static <T extends HabitModel> List<T> getNextList(List<T> list, TreeMap<Object, List<T>> map, String idKey) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            List<T> childrenList = map.get(list.get(i).get(idKey));
            childrenList = getNextList(childrenList, map, idKey);
            boolean hasChildren = !childrenList.isEmpty();
            list.get(i).put(HAS, hasChildren);
            if (hasChildren) {
                list.get(i).put(CHILDREN, childrenList);
            }
        }
        return list;
    }


    /**
     * TODO:对应ID列表是否有字节点
     *
     * @param idList
     * @param list
     * @param pIdKey
     * @param idKey
     * @param <T>
     * @return
     */
    public static <T extends HabitModel> boolean hasChildren(List<Object> idList, List<T> list, String pIdKey, String idKey) {
        List<Object> idsChildren = new ArrayList<>();
        for (T model : list) {
            if (idList.contains(model.get(pIdKey))&&!idList.contains(model.get(idKey))) {
                idsChildren.add(model.get(idKey));
            }
        }
        return idsChildren.size() > 0;
    }
}
