###Controller模版
package #(path.controllerPackage);

import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Page;
import #(path.modelPackage+'.'+table.model);
import #(path.servicePackage+'.'+table.model)Service;
import com.up.habit.expand.route.anno.*;
import com.up.app.controller.admin.AdminController;
import com.up.habit.expand.route.perm.Auth;
import com.up.habit.app.controller.validator.type.*;
import com.up.habit.app.controller.render.To;
import com.up.habit.expand.db.dialect.Logic;
import java.util.List;
import com.up.habit.expand.db.kit.TreeKit;
import com.up.app.controller.admin.cm.valitade.*;

/**
 * TODO:#(table.comment)控制器
 *
 * @author #(table.author) on #(dateTime)
 */
@Directory(value = "#(table.module)", icon = "#(table.module_icon)")
@Ctr(name = "#(table.menu)", icon = "#(table.menu_iocn)"#if(firstCharToLowerCase(table.model)!=table.menu_path), value="#(table.menu_path)"#end)
public class #(table.model)Controller extends AdminController {

    @Inject
    #(table.model)Service srv;

###增
    @Api(value = "添加", auth = Auth.BUTTON)
    #(para.add)
    public To add() {
        #(table.model) #(firstCharToLowerCase(table.model)) = getModel(#(table.model).class);
        #if(para.addRemove)
        #(firstCharToLowerCase(table.model)).remove(#(para.addRemove));
        #end
        #if(para.isFake)
        #(firstCharToLowerCase(table.model)).setDel(false);
        #end
        return srv.add(#(firstCharToLowerCase(table.model)));
    }

###删
    @Api(value = "删除", auth = Auth.BUTTON)
    #(para.delete)
    public To delete() {
        #if(para.isFake)
        return srv.remove(#(para.deleteGet));
        #else
        return srv.delete(#(para.deleteGet));
        #end
    }

###改
    @Api(value = "修改", auth = Auth.BUTTON)
    #(para.edit)
    public To edit() {
        #(table.model) #(firstCharToLowerCase(table.model)) = getModel(#(table.model).class);
        #if(para.editRemove)
        #(firstCharToLowerCase(table.model)).remove(#(para.editRemove));
        #end
        return srv.edit(#(firstCharToLowerCase(table.model)));
    }

###查
    @Api("列表-分页")
    #(para.query)
    public Page<#(table.model)> page() {
        #(table.model) cond=getAllInModel(#(table.model).class);
        #if(para.list)
        cond.loadColumnArray(#(para.list));
        #end
        #if(para.isFake)
        cond.setDel(false);
        #end
        #if(table.tpl=='treeTable')
        cond.set(#(table.model).#(toUpperCase(toUnderScoreCase(table.tree_p_id))), "#(table.tree_p_id_def)").condition(#(table.model).#(toUpperCase(toUnderScoreCase(table.tree_p_id))),#if(table.tree_p_id_def) Logic.EQUALS#else Logic.IS_NULL_OR_EMPTY#end);
        #end
        return #('srv.page(cond, num(), size()'));
    }

    @Api("详情")
    #(para.info)
    public #(table.model) info() {
        return #('srv.info('+para.infoGet+')');
    }

#if(table.tpl=='treeTable')###树表
    @Api("列表")
    #(para.query)
    public List<#(table.model)> list() {
        #(table.model) cond=getAllInModel(#(table.model).class);
        #if(para.list)
        cond.loadColumnArray(#(para.list));
        #end
        #if(para.isFake)
        cond.setDel(false);
        #end
        cond.condition(#(table.model).#(toUpperCase(toUnderScoreCase(table.tree_p_id))), Logic.EQUALS);
        return srv.list(cond);
    }

    @Api("下拉选项")
    public List<#(table.model)> treeSelect() {
        #(table.model) cond = new #(table.model)().loadColumnArray(#(table.model).#(toUpperCase(toUnderScoreCase(table.tree_id))), #(table.model).#(toUpperCase(toUnderScoreCase(table.tree_p_id))), #(table.model).#(toUpperCase(toUnderScoreCase(table.tree_label))) + " as label")
                .setDel(false);
        #if(para.sortColumnName)
        cond.orderByAsc(#(table.model).#(toUpperCase(toUnderScoreCase(sortColumnName))));
        #end
        List<#(table.model)> list = srv.list(cond);
        list = TreeKit.buildTree(list, #(table.model).#(toUpperCase(toUnderScoreCase(table.tree_p_id))), #(table.model).#(toUpperCase(toUnderScoreCase(table.tree_id))));
        return list;
    }
#end

}
