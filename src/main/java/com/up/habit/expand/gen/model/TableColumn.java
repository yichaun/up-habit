package com.up.habit.expand.gen.model;

import com.jfinal.plugin.activerecord.IBean;
import com.up.habit.expand.db.model.HabitModel;

/**
 * 代码生成业务表字段 实体类
 * @author 王剑洪 on 2020-07-23 23:36:55
 */
@SuppressWarnings("serial")
public class TableColumn extends HabitModel<TableColumn> implements IBean {

	public static final TableColumn dao = new TableColumn().dao();

	public static TableColumn cond() {
		return new TableColumn();
	}

	/**
	 * 参数文档
	 * @Param(name = "id", des = "编号", required = false),
	 * @Param(name = "table_id", des = "归属表编号", required = false),
	 * @Param(name = "name", des = "列名称", required = false),
	 * @Param(name = "comment", des = "列描述", required = false),
	 * @Param(name = "business", des = "字段说明", required = false),
	 * @Param(name = "type", des = "列类型", required = false),
	 * @Param(name = "java_type", des = "JAVA类型", required = false),
	 * @Param(name = "nullable", des = "允许为空", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "def_value", des = "默认值", required = false),
	 * @Param(name = "pk", des = "是否主键", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "increment", des = "是否自增", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "required", des = "是否必填", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "inserted", des = "是否为插入字段", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "edit", des = "是否编辑字段", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "list", des = "是否列表字段", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "query", des = "是否查询字段", dataType = SwitchValidate.class, required = false),
	 * @Param(name = "query_type", des = "查询方式（等于、不等于、大于、小于、范围）", required = false),
	 * @Param(name = "html_type", des = "显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）", required = false),
	 * @Param(name = "dict_type", des = "字典类型", required = false),
	 * @Param(name = "create_by", des = "创建者", required = false),
	 * @Param(name = "create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "start_create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "end_create_time", des = "创建时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "modify_by", des = "更新者", required = false),
	 * @Param(name = "modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "start_modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false),
	 * @Param(name = "end_modify_time", des = "更新时间", dataType = DateTimeValidate.class, required = false)
	 */
    public final static String TABLE_INFO="id:-编号:|table_id:-归属表编号:|name:-列名称:|comment:-列描述:|business:-字段说明:|type:-列类型:|java_type:-JAVA类型:|nullable:-允许为空:|def_value:-默认值:|pk:-是否主键:|increment:-是否自增:|required:-是否必填:|inserted:-是否为插入字段:|edit:-是否编辑字段:|list:-是否列表字段:|query:-是否查询字段:|query_type:-查询方式（等于、不等于、大于、小于、范围）:|html_type:-显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）:|dict_type:-字典类型:|create_by:-创建者:|create_time:-创建时间:|modify_by:-更新者:|modify_time:-更新时间";

	/**表名*/
    public final static String TABLE_NAME="gen_table_column";

    /**主键*/
    public final static String TABLE_PKS="id";

    /**编号*/
    public final static String ID="id";

    /**归属表编号*/
    public final static String TABLE_ID="table_id";

    /**列名称*/
    public final static String NAME="name";

    /**列描述*/
    public final static String COMMENT="comment";

    /**字段说明*/
    public final static String BUSINESS="business";

    /**列类型*/
    public final static String TYPE="type";

    /**JAVA类型*/
    public final static String JAVA_TYPE="java_type";

    /**允许为空*/
    public final static String NULLABLE="nullable";

    /**默认值*/
    public final static String DEF_VALUE="def_value";

    /**是否主键*/
    public final static String PK="pk";

    /**是否自增*/
    public final static String INCREMENT="increment";

    /**是否必填*/
    public final static String REQUIRED="required";

    /**是否为插入字段*/
    public final static String INSERTED="inserted";

    /**是否编辑字段*/
    public final static String EDIT="edit";

    /**是否列表字段*/
    public final static String LIST="list";

    /**是否查询字段*/
    public final static String QUERY="query";

    /**查询方式（等于、不等于、大于、小于、范围）*/
    public final static String QUERY_TYPE="query_type";

    /**显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）*/
    public final static String HTML_TYPE="html_type";

    /**字典类型*/
    public final static String DICT_TYPE="dict_type";

    /**创建者*/
    public final static String CREATE_BY="create_by";

    /**创建时间*/
    public final static String CREATE_TIME="create_time";

    /**更新者*/
    public final static String MODIFY_BY="modify_by";

    /**更新时间*/
    public final static String MODIFY_TIME="modify_time";

    /**设置编号*/
	public TableColumn setId(Integer id) {
		set(ID, id);
		return this;
	}

	/**获取编号*/
	public Integer getId() {
		return getInt(ID);
	}

    /**设置归属表编号*/
	public TableColumn setTableId(Integer tableId) {
		set(TABLE_ID, tableId);
		return this;
	}

	/**获取归属表编号*/
	public Integer getTableId() {
		return getInt(TABLE_ID);
	}

    /**设置列名称*/
	public TableColumn setName(String name) {
		set(NAME, name);
		return this;
	}

	/**获取列名称*/
	public String getName() {
		return getStr(NAME);
	}

    /**设置列描述*/
	public TableColumn setComment(String comment) {
		set(COMMENT, comment);
		return this;
	}

	/**获取列描述*/
	public String getComment() {
		return getStr(COMMENT);
	}

    /**设置字段说明*/
	public TableColumn setBusiness(String business) {
		set(BUSINESS, business);
		return this;
	}

	/**获取字段说明*/
	public String getBusiness() {
		return getStr(BUSINESS);
	}

    /**设置列类型*/
	public TableColumn setType(String type) {
		set(TYPE, type);
		return this;
	}

	/**获取列类型*/
	public String getType() {
		return getStr(TYPE);
	}

    /**设置JAVA类型*/
	public TableColumn setJavaType(String javaType) {
		set(JAVA_TYPE, javaType);
		return this;
	}

	/**获取JAVA类型*/
	public String getJavaType() {
		return getStr(JAVA_TYPE);
	}

    /**设置允许为空*/
	public TableColumn setNullable(Boolean nullable) {
		set(NULLABLE, nullable);
		return this;
	}

	/**获取允许为空*/
	public Boolean getNullable() {
		return _get(NULLABLE);
	}

    /**设置默认值*/
	public TableColumn setDefValue(String defValue) {
		set(DEF_VALUE, defValue);
		return this;
	}

	/**获取默认值*/
	public String getDefValue() {
		return getStr(DEF_VALUE);
	}

    /**设置是否主键*/
	public TableColumn setPk(Boolean pk) {
		set(PK, pk);
		return this;
	}

	/**获取是否主键*/
	public Boolean getPk() {
		return _get(PK);
	}

    /**设置是否自增*/
	public TableColumn setIncrement(Boolean increment) {
		set(INCREMENT, increment);
		return this;
	}

	/**获取是否自增*/
	public Boolean getIncrement() {
		return _get(INCREMENT);
	}

    /**设置是否必填*/
	public TableColumn setRequired(Boolean required) {
		set(REQUIRED, required);
		return this;
	}

	/**获取是否必填*/
	public Boolean getRequired() {
		return _get(REQUIRED);
	}

    /**设置是否为插入字段*/
	public TableColumn setInserted(Boolean inserted) {
		set(INSERTED, inserted);
		return this;
	}

	/**获取是否为插入字段*/
	public Boolean getInserted() {
		return _get(INSERTED);
	}

    /**设置是否编辑字段*/
	public TableColumn setEdit(Boolean edit) {
		set(EDIT, edit);
		return this;
	}

	/**获取是否编辑字段*/
	public Boolean getEdit() {
		return _get(EDIT);
	}

    /**设置是否列表字段*/
	public TableColumn setList(Boolean list) {
		set(LIST, list);
		return this;
	}

	/**获取是否列表字段*/
	public Boolean getList() {
		return _get(LIST);
	}

    /**设置是否查询字段*/
	public TableColumn setQuery(Boolean query) {
		set(QUERY, query);
		return this;
	}

	/**获取是否查询字段*/
	public Boolean getQuery() {
		return _get(QUERY);
	}

    /**设置查询方式（等于、不等于、大于、小于、范围）*/
	public TableColumn setQueryType(String queryType) {
		set(QUERY_TYPE, queryType);
		return this;
	}

	/**获取查询方式（等于、不等于、大于、小于、范围）*/
	public String getQueryType() {
		return getStr(QUERY_TYPE);
	}

    /**设置显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）*/
	public TableColumn setHtmlType(String htmlType) {
		set(HTML_TYPE, htmlType);
		return this;
	}

	/**获取显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）*/
	public String getHtmlType() {
		return getStr(HTML_TYPE);
	}

    /**设置字典类型*/
	public TableColumn setDictType(String dictType) {
		set(DICT_TYPE, dictType);
		return this;
	}

	/**获取字典类型*/
	public String getDictType() {
		return getStr(DICT_TYPE);
	}


	public Boolean _get(String columnName) {
		Object o = get(columnName);
		if (o == null) {
			return false;
		}
		if (o instanceof Long) {
			long oValue = (long) o;
			return oValue != 0;
		} else if (o instanceof Integer) {
			int oValue = (int) o;
			return oValue != 0;
		} else if (o instanceof Boolean){
			return (Boolean) o;
		}else {
			return false;
		}
	}


}
