<template>
    #if(table.tpl=='treeRightTable')
    <left-tree @select="selectTree" :action="tree.action">
    #else
    <div class="app-container">
    #end
        <!--查询条件-->
#include("habit_vue_query.tpl")
        <!--操作-->
#include("habit_vue_oper.tpl")
        <!--列表数据-->
#include("habit_vue_list.tpl")
        <!--弹出框-->
#include("habit_vue_dialog.tpl")
     #if(table.tpl=='treeRightTable')
    </left-tree>
    #else
    </div>
    #end
</template>
<script>

    import {add, remove, edit, page, info, action#if(table.tpl=='treeTable'), list, treeSelect#end} from '@#(path.jsImportPath)';
#(vue.componentImport)

    export default {
        name: "#(table.menu_path)",
        components: {#(vue.components)},
        data() {
            return {
                #if(table.tpl=='treeRightTable')
                tree:{
                    action:'#(table.tree_action)',//左侧树的请求连接
                    current:'',//当前选中
                    data:[],//左侧树数据
                },
                #end
                action: action,//权限action
                query: {},//查询条件
                tableData: [],//列表数据
                total: 0,//列表数量
                multiple: {//列表选择
                    ids: []
                },
                dialog: {//弹出框
                    title: '',
                    visible: false
                },
                info: {},//详情
                #if(table.tpl=='treeTable')
                treeSelect:[],
                #end
            }
        },
        created() {
        #if(table.tpl!='treeRightTable')
            this.handleQuery();
        #end
        },
        methods: {
#include("habit_vue_method.tpl")
        }
    }
</script>
