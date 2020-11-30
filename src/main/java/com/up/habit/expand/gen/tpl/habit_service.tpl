###代码生成service
package #(path.servicePackage);

import #(path.modelPackage).#(table.model);
import com.up.habit.app.service.HabitService;
import com.jfinal.log.Log;


/**
 * TODO:#(table.menu) 业务类
 * @author #(table.author) on #(dateTime)
 */
public class #(table.model)Service extends HabitService<#(table.model)> {

    private final Log log=Log.getLog(#(table.model).class);

    public static #(table.model)Service me = new #(table.model)Service();

}

