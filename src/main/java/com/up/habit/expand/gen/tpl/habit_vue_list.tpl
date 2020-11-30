####列表数据模版
#if(table.tpl=='treeTable')###树表
        <el-table ref="table" :data="tableData" row-key="#(table.tree_id)" @selection-change="handleSelectionChange"
                  lazy :load="handleQueryChild" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
#else
        <el-table ref="table" :data="tableData" row-key="#(table.pks)" @selection-change="handleSelectionChange">
#end
            <el-table-column type="selection" width="50" align="center"/>
#for(cm:columns)#if(cm.list)
#if(cm.java_type=='java.lang.Boolean')
            <el-table-column label="#(cm.business)" align="center" prop="#(cm.name)">
                <template slot-scope="scope">
                    <el-tag> {{dictName(yesOrNo,scope.row.#(cm.name))}}</el-tag>
                </template>
            </el-table-column>
#else if(cm.dict_type)
            <el-table-column label="#(cm.business)" align="center" prop="#(cm.name)">
                <template slot-scope="scope">
                    <el-tag> {{dictName($store.state.dict.options['#(cm.dict_type)'],scope.row.#(cm.name))}}</el-tag>
                </template>
            </el-table-column>
#else if(cm.html_type=='Image')
            <el-table-column label="#(cm.business)" align="center" prop="#(cm.name)">
                <template slot-scope="scope">
                    <el-image style="width: 100px; height: 100px" :src="scope.row.#(cm.name)" :fit="fit"></el-image>
                </template>
            </el-table-column>
#else if(cm.html_type=='RichText')
#else
            <el-table-column label="#(cm.business)" prop="#(cm.name)" align="center" show-overflow-tooltip/>
#end
#end#end
            <el-table-column label="操作" align="center" prop="create_time" sortable>
                <template slot-scope="scope">
#if(table.tpl=='treeTable')###树表,添加子节点
                    <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)" v-perm="[action.add]">
                        新增
                    </el-button>
#end
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)" v-perm="[action.edit]">
                        编辑
                    </el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-perm="[action.delete]" class="delete">
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="query.pageNo" :limit.sync="query.pageSize" @pagination="handleQuery"/>
