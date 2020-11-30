###代码生成model
package #(path.modelPackage);

import com.up.habit.expand.db.model.HabitModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * TODO:#(table.business) 实体类
 * @author 王剑洪 on #(dateTime)
 */
@SuppressWarnings("serial")
public class #(table.model) extends HabitModel<#(table.model)> implements IBean {

	public static final #(table.model) dao = new #(table.model)().dao();

	public static #(table.model) cond() {
		return new #(table.model)();
	}

##############################swagger文档数据生成##############################
    public final static String TABLE_INFO="#(para.tableInfo)";
##############################swagger文档数据生成##############################

##############################表字段##############################
	/**表名*/
    public final static String TABLE_NAME="#(table.name)";

    /**主键*/
    public final static String TABLE_PKS="#(table.pks)";
#for(cm : columns)

    #if(cm.comment)/**#(cm.comment)*/#end
    public final static String #(toUpperCase(toUnderScoreCase(cm.name)))="#(cm.name)";
#end
##############################表字段##############################
##############################属性设置##############################
#for(cm : columns)
    #if(notDefColumn(cm.name))

    #if(cm.business)/**设置#(cm.business)*/#end
	#set(argName = javaKeyword.contains(cm.name) ? '_' + toCamelCase(cm.name) : toCamelCase(cm.name))
	public #(table.model) set#(firstCharToUpperCase(toCamelCase(cm.name)))(#(cm.javaType) #(argName)) {
		set(#(toUpperCase(toUnderScoreCase(cm.name))), #(argName));
		return this;
	}

	#if(cm.business)/**获取#(cm.business)*/#end
	#set(getterOfModel = getterTypeMap.get(cm.javaType))
	#if (isBlank(getterOfModel))
		#set(getterOfModel = 'get')
	#end
	public #(cm.javaType) get#(firstCharToUpperCase(toCamelCase(cm.name)))() {
		return #(getterOfModel)(#(toUpperCase(toUnderScoreCase(cm.name))));
	}
    #end
#end

}

