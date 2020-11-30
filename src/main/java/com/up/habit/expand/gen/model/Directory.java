package com.up.habit.expand.gen.model;

import com.jfinal.plugin.activerecord.IBean;
import com.up.habit.expand.db.model.HabitModel;

/**
 *  实体类
 * @author 王剑洪 on 2020-07-23 23:36:55
 */
@SuppressWarnings("serial")
public class Directory extends HabitModel<Directory> implements IBean {

	public static final Directory dao = new Directory().dao();

	public static Directory cond() {
		return new Directory();
	}

	/**
	 * 参数文档
	 * @Param(name = "id", des = "", required = false),
	 * @Param(name = "path", des = "目录路径", required = false),
	 * @Param(name = "name", des = "目录名称", required = false),
	 * @Param(name = "icon", des = "目录图标", required = false),
	 * @Param(name = "prefix", des = "数据库表前缀", required = false),
	 * @Param(name = "create_by", des = "创建者", required = false),
	 * @Param(name = "create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "start_create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "end_create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "modify_by", des = "更新者", required = false),
	 * @Param(name = "modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "start_modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "end_modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false)
	 */
    public final static String TABLE_INFO="id:-:|path:-目录路径:|name:-目录名称:|icon:-目录图标:|prefix:-数据库表前缀:|create_by:-创建者:|create_time:-创建时间:|modify_by:-更新者:|modify_time:-更新时间";

	/**表名*/
    public final static String TABLE_NAME="gen_directory";

    /**主键*/
    public final static String TABLE_PKS="id";

    
    public final static String ID="id";

    /**目录路径*/
    public final static String PATH="path";

    /**目录名称*/
    public final static String NAME="name";

    /**目录图标*/
    public final static String ICON="icon";

    /**数据库表前缀*/
    public final static String PREFIX="prefix";

    /**创建者*/
    public final static String CREATE_BY="create_by";

    /**创建时间*/
    public final static String CREATE_TIME="create_time";

    /**更新者*/
    public final static String MODIFY_BY="modify_by";

    /**更新时间*/
    public final static String MODIFY_TIME="modify_time";

    
	public Directory setId(Integer id) {
		set(ID, id);
		return this;
	}


	public Integer getId() {
		return getInt(ID);
	}

    /**设置目录路径*/
	public Directory setPath(String path) {
		set(PATH, path);
		return this;
	}

	/**获取目录路径*/
	public String getPath() {
		return getStr(PATH);
	}

    /**设置目录名称*/
	public Directory setName(String name) {
		set(NAME, name);
		return this;
	}

	/**获取目录名称*/
	public String getName() {
		return getStr(NAME);
	}

    /**设置目录图标*/
	public Directory setIcon(String icon) {
		set(ICON, icon);
		return this;
	}

	/**获取目录图标*/
	public String getIcon() {
		return getStr(ICON);
	}

    /**设置数据库表前缀*/
	public Directory setPrefix(String prefix) {
		set(PREFIX, prefix);
		return this;
	}

	/**获取数据库表前缀*/
	public String getPrefix() {
		return getStr(PREFIX);
	}


}
