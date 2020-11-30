import {post} from '@/utils/request'

/**action权限*/
export const action={
    add:'#(path.controllerAction)add',
    delete:'#(path.controllerAction)delete',
    edit:'#(path.controllerAction)edit',
    page:'#(path.controllerAction)page',
    info:'#(path.controllerAction)info',
    #if(table.tpl=='treeTable')
    list:'#(path.controllerAction)list',
    treeSelect:'#(path.controllerAction)treeSelect',
    #end

}

/**新增*/
export function add(data, ok) {
    post(action.add, data, ok)
}

/**删除*/
export function remove(#(vue.functionPks), ok) {
    post(action.delete, #(vue.paraPks), ok)
}

/**编辑*/
export function edit(data, ok) {
    post(action.edit, data, ok)
}

/**列表*/
export function page(data, ok) {
    post(action.page, data, ok)
}

/**详情*/
export function info(#(vue.functionPks), ok) {
    post(action.info, #(vue.paraPks), ok)
}

#if(table.tpl=='treeTable')
/**列表*/
export function list(#(toCamelCase(table.tree_p_id)), ok) {
    post(action.list, {#(table.tree_p_id): #(toCamelCase(table.tree_p_id))}, ok)
}

/**列表*/
export function treeSelect(ok) {
    post(action.treeSelect, null, ok)
}
#end

