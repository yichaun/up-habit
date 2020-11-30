####列表数据模版
        <self-dialog :title="dialog.title" :visible.sync="dialog.visible" :width="500">
            <el-form ref="form" :model="info" label-width="80px">
                #for(cm:columns)
                #if(cm.pk&&!cm.increment)###非自增主键
                <el-form-item label="#(cm.business)" prop="#(cm.mame)">
                    <el-input v-model="info.#(cm.name)" placeholder="请输入#(cm.business)" :disabled="dialog.title!='新增'"></el-input>
                </el-form-item>
                #else if(!cm.pk)###非主键
                #########树表
                #if(table.tpl=='treeTable'&&cm.name==table.tree_p_id)
                <el-form-item label="父级#(table.tree_label)" prop="#(cm.mame)">
                    <treeselect v-model="info.#(cm.name)" :options="treeSelect" placeholder="选择父级#(table.tree_label)"/>
                </el-form-item>
                #########左侧树右侧列表
                #else if(table.tpl=='treeRightTable'&&cm.name==table.tree_id)
                 <el-form-item label="父级#(table.tree_label)" prop="#(cm.name)">
                     <treeselect v-model="info.#(cm.name)" :options="tree.data" placeholder="选择父级#(table.tree_label)"/>
                 </el-form-item>
                #else #if(cm.inserted||cm.edit)
                <el-form-item label="#(cm.business)" prop="#(cm.name)">
                    #if(cm.html_type=='Input')###普通输入框
                    <el-input v-model="info.#(cm.name)" placeholder="请输入#(cm.business)"></el-input>
                    #else if(cm.html_type=='InputNumber')###数字输入框
                    <el-input-number v-model="info.#(cm.name)" placeholder="请输入#(cm.business)" size="small"></el-input-number>
                    #else if(cm.html_type=='TextArea')###文本域
                    <el-input type="textarea" :rows="3" v-model="info.#(cm.name)" placeholder="请输入#(cm.business)"></el-input>
                    #else if(cm.html_type=='RichText')###富文本
                    <tinymce ref="content" v-model="info.content"/>
                    #else if(cm.html_type=='Select')###下拉框
                    <el-select v-model="info.#(cm.name)"  placeholder="请选择#(cm.business)" >
                        #if(cm.dict_type)
                        <el-option v-for="item in this.$store.state.dict.options['#(cm.dict_type)']" :key="item.id" :label="item.name" :value="item.value"/>
                        #else
                        <el-option label="请选择字典生成" value=""/>
                        #end
                    </el-select>
                    #else if(cm.html_type=='Radio')###单选框
                    <el-radio-group v-model="info.#(cm.name)">
                        #if(cm.dict_type)
                        <el-radio v-for="item in this.$store.state.dict.options['#(cm.dict_type)']" :key="item.id" :label="item.value">{{item.name}}</el-radio>
                        #else
                        <el-radio label="">请选择字典生成</el-radio>
                        #end
                    </el-radio-group>
                    #else if(cm.html_type=='RadioButton')###单选框
                    <el-radio-group v-model="info.#(cm.name)">
                        #if(cm.java_type=='java.lang.Boolean')
                        <el-radio-button v-for="item in this.yesOrNo" :key="item.value" :label="item.value">{{item.name}}</el-radio-button>
                        #else if(cm.dict_type)
                        <el-radio v-for="item in this.$store.state.dict.options['#(cm.dict_type)']" :key="item.id" :label="item.value">{{item.name}}</el-radio>
                        #else
                        <el-radio label="">请选择字典生成</el-radio>
                        #end
                    </el-radio-group>
                    #else if(cm.html_type=='Checkbox')###多选框
                    <el-checkbox-group v-model="info.#(cm.name)">
                        #if(cm.dict_type)
                        <el-checkbox v-for="item in cities" :label="item.value" :key="item.value">{{item.name}}</el-checkbox>
                        #else
                        <el-checkbox label="请选择字典生成"></el-checkbox>
                        #end
                    </el-checkbox-group>
                    <el-radio-button v-for="item in this.$store.state.dict.options['#(cm.dict_type)']" :key="item.id" :label="item.value">{{item.name}}</el-radio-button>
                    #else if(cm.html_type=='DatePicker')###日期
                    <el-date-picker size="small" v-model="info.#(cm.name)" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
                    #else if(cm.html_type=='DateTimePicker')###日期时间控件
                    <el-date-picker size="small" v-model="info.#(cm.name)" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择时间"></el-date-picker>
                    #else if(cm.html_type=='Image')###图片上传
                    <image-upload v-model="info.#(cm.name)" :is-single="true"/>
                    #else if(cm.html_type=='Upload')###文件上传
                    <image-upload v-model="info.#(cm.name)" :is-single="true"/>
                    #end
                </el-form-item>
                #end
                #end
                #end
                #end
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="handleSubmit">确 定</el-button>
                <el-button @click="dialog.visible=!dialog.visible">取 消</el-button>
            </div>
        </self-dialog>