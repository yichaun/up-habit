package com.up.habit.expand.gen.model;

import com.jfinal.plugin.activerecord.IBean;
import com.up.habit.expand.db.model.HabitModel;

/**
 * 代码生成业务表 实体类
 * @author 王剑洪 on 2020-07-23 23:36:55
 */
@SuppressWarnings("serial")
public class Table extends HabitModel<Table> implements IBean {

	public static final Table dao = new Table().dao();

	public static Table cond() {
		return new Table();
	}

	/**
	 * 参数文档
	 * @Param(name = "id", des = "编号", required = false),
	 * @Param(name = "name", des = "表名称", required = false),
	 * @Param(name = "comment", des = "表描述", required = false),
	 * @Param(name = "pks", des = "主键", required = false),
	 * @Param(name = "remark", des = "备注", required = false),
	 * @Param(name = "tpl", des = "使用的模板（crud单表操作 tree树表操作）", required = false),
	 * @Param(name = "author", des = "作者", required = false),
	 * @Param(name = "module", des = "目录名称", required = false),
	 * @Param(name = "module_path", des = "目录路径", required = false),
	 * @Param(name = "module_icon", des = "目录图标", required = false),
	 * @Param(name = "menu", des = "菜单名称", required = false),
	 * @Param(name = "menu_path", des = "菜单路径", required = false),
	 * @Param(name = "menu_iocn", des = "菜单图标", required = false),
	 * @Param(name = "model", des = "实体类名", required = false),
	 * @Param(name = "pkg", des = "基础包名", required = false),
	 * @Param(name = "create_by", des = "创建者", required = false),
	 * @Param(name = "create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "start_create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "end_create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "modify_by", des = "更新者", required = false),
	 * @Param(name = "modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "start_modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "end_modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false)
	 */
    public final static String TABLE_INFO="id:-编号:|name:-表名称:|comment:-表描述:|pks:-主键:|remark:-备注:|tpl:-使用的模板（crud单表操作 tree树表操作）:|author:-作者:|module:-目录名称:|module_path:-目录路径:|module_icon:-目录图标:|menu:-菜单名称:|menu_path:-菜单路径:|menu_iocn:-菜单图标:|model:-实体类名:|pkg:-基础包名:|create_by:-创建者:|create_time:-创建时间:|modify_by:-更新者:|modify_time:-更新时间";

	/**表名*/
    public final static String TABLE_NAME="gen_table";

    /**主键*/
    public final static String TABLE_PKS="id";

    /**编号*/
    public final static String ID="id";

    /**表名称*/
    public final static String NAME="name";

    /**表描述*/
    public final static String COMMENT="comment";

    /**主键*/
    public final static String PKS="pks";

    /**备注*/
    public final static String REMARK="remark";

    /**使用的模板（crud单表操作 tree树表操作）*/
    public final static String TPL="tpl";



    /**作者*/
    public final static String AUTHOR="author";

    /**目录名称*/
    public final static String MODULE="module";

    /**目录路径*/
    public final static String MODULE_PATH="module_path";

    /**目录图标*/
    public final static String MODULE_ICON="module_icon";

    /**菜单名称*/
    public final static String MENU="menu";

    /**菜单路径*/
    public final static String MENU_PATH="menu_path";

    /**菜单图标*/
    public final static String MENU_IOCN="menu_iocn";

    /**实体类名*/
    public final static String MODEL="model";

    /**基础包名*/
    public final static String PKG="pkg";

    /**创建者*/
    public final static String CREATE_BY="create_by";

    /**创建时间*/
    public final static String CREATE_TIME="create_time";

    /**更新者*/
    public final static String MODIFY_BY="modify_by";

    /**更新时间*/
    public final static String MODIFY_TIME="modify_time";

	/**使用的模板（crud单表操作 tree树表操作）*/
	public final static String TREE_ID="tree_id";
	public final static String TREE_LABEL="tree_label";
	public final static String TREE_ACTION="tree_action";

	/**使用的模板（crud单表操作 tree树表操作）*/
	public final static String TREE_P_ID="tree_p_id";

	/**使用的模板（crud单表操作 tree树表操作）*/
	public final static String TREE_P_ID_DEF="tree_p_id_def";

    /**设置编号*/
	public Table setId(Integer id) {
		set(ID, id);
		return this;
	}

	/**获取编号*/
	public Integer getId() {
		return getInt(ID);
	}

    /**设置表名称*/
	public Table setName(String name) {
		set(NAME, name);
		return this;
	}

	/**获取表名称*/
	public String getName() {
		return getStr(NAME);
	}

    /**设置表描述*/
	public Table setComment(String comment) {
		set(COMMENT, comment);
		return this;
	}

	/**获取表描述*/
	public String getComment() {
		return getStr(COMMENT);
	}

    /**设置主键*/
	public Table setPks(String pks) {
		set(PKS, pks);
		return this;
	}

	/**获取主键*/
	public String getPks() {
		return getStr(PKS);
	}

    /**设置备注*/
	public Table setRemark(String remark) {
		set(REMARK, remark);
		return this;
	}

	/**获取备注*/
	public String getRemark() {
		return getStr(REMARK);
	}

    /**设置使用的模板（crud单表操作 tree树表操作）*/
	public Table setTpl(String tpl) {
		set(TPL, tpl);
		return this;
	}

	/**获取使用的模板（crud单表操作 tree树表操作）*/
	public String getTpl() {
		return getStr(TPL);
	}

    /**设置作者*/
	public Table setAuthor(String author) {
		set(AUTHOR, author);
		return this;
	}

	/**获取作者*/
	public String getAuthor() {
		return getStr(AUTHOR);
	}

    /**设置目录名称*/
	public Table setModule(String module) {
		set(MODULE, module);
		return this;
	}

	/**获取目录名称*/
	public String getModule() {
		return getStr(MODULE);
	}

    /**设置目录路径*/
	public Table setModulePath(String modulePath) {
		set(MODULE_PATH, modulePath);
		return this;
	}

	/**获取目录路径*/
	public String getModulePath() {
		return getStr(MODULE_PATH);
	}

    /**设置目录图标*/
	public Table setModuleIcon(String moduleIcon) {
		set(MODULE_ICON, moduleIcon);
		return this;
	}

	/**获取目录图标*/
	public String getModuleIcon() {
		return getStr(MODULE_ICON);
	}

    /**设置菜单名称*/
	public Table setMenu(String menu) {
		set(MENU, menu);
		return this;
	}

	/**获取菜单名称*/
	public String getMenu() {
		return getStr(MENU);
	}

    /**设置菜单路径*/
	public Table setMenuPath(String menuPath) {
		set(MENU_PATH, menuPath);
		return this;
	}

	/**获取菜单路径*/
	public String getMenuPath() {
		return getStr(MENU_PATH);
	}

    /**设置菜单图标*/
	public Table setMenuIocn(String menuIocn) {
		set(MENU_IOCN, menuIocn);
		return this;
	}

	/**获取菜单图标*/
	public String getMenuIocn() {
		return getStr(MENU_IOCN);
	}

    /**设置实体类名*/
	public Table setModel(String model) {
		set(MODEL, model);
		return this;
	}

	/**获取实体类名*/
	public String getModel() {
		return getStr(MODEL);
	}

    /**设置基础包名*/
	public Table setPkg(String pkg) {
		set(PKG, pkg);
		return this;
	}

	/**获取基础包名*/
	public String getPkg() {
		return getStr(PKG);
	}

	public Table setTreeId(String treeId) {
		set(TREE_ID, treeId);
		return this;
	}

	public String getTreeId() {
		return getStr(TREE_ID);
	}

	public Table setTreeLabel(String treeLabel) {
		set(TREE_LABEL, treeLabel);
		return this;
	}

	public String getTreeLabel() {
		return getStr(TREE_LABEL);
	}

	public Table setTreeAction(String treeAction) {
		set(TREE_ACTION, treeAction);
		return this;
	}

	public String getTreeAction() {
		return getStr(TREE_ACTION);
	}



	public Table setTreePId(String treePId) {
		set(TREE_P_ID, treePId);
		return this;
	}

	public String getTreePId() {
		return getStr(TREE_P_ID);
	}

	public Table setTreePIdDef(String treePIdDef) {
		set(TREE_P_ID_DEF, treePIdDef);
		return this;
	}

	public String getTreePIdDef() {
		return getStr(TREE_P_ID_DEF);
	}




}
