####查询模版
        <el-form :model="query" ref="queryForm" :inline="true">
            #for(cm:columns)
            #if(cm.query)
            #if(table.tpl=='treeTable'&&table.tree_p_id==cm.name)
            #else
                #if(cm.htmlType=='Input'||cm.htmlType=='TextArea'||cm.htmlType=='RichText')
            <el-form-item label="#(cm.business)" prop="#(cm.name)">
                <el-input v-model="query.#(cm.name)" placeholder="请输入#(cm.business)" clearable size="small" @keyup.enter.native="handleQuery"/>
            </el-form-item>
                #else if(cm.htmlType=='Select'||cm.htmlType=='Radio'||cm.htmlType=='RadioButton'||cm.htmlType=='Checkbox')
            #if(cm.htmlType=='RadioButton'&&cm.java_type=='java.lang.Boolean')
            <el-form-item label="#(cm.business)" prop="#(cm.name)">
                <el-select v-model="query.#(cm.name)" placeholder="请选择#(cm.business)" clearable size="small">
                    <el-option v-for="item in this.yesOrNo" :key="item.value" :label="item.name" :value="item.value"/>
                </el-select>
            </el-form-item>
            #else
            <el-form-item label="#(cm.business)" prop="#(cm.name)">
                <el-select v-model="query.#(cm.name)" placeholder="请选择#(cm.business)" clearable size="small">
                    #if(cm.dict_type)
                    <el-option v-for="item in this.$store.state.dict.options['#(cm.dict_type)']" :key="item.id" :label="item.name" :value="item.value"/>
                    #else
                    <el-option label="请选择字典生成" value=""/>
                    #end
                </el-select>
            </el-form-item>
            #end
                #else if(cm.htmlType=='InputNumber')
            <el-form-item label="#(cm.business)" prop="#(cm.name)">
                <el-input-number v-model="query.#(cm.name)" placeholder="请输入#(cm.business)" clearable size="small" @keyup.enter.native="handleQuery"/>
            </el-form-item>
                #else if(cm.htmlType=='DatePicker')
            <el-form-item label="#(cm.business)" prop="#(cm.name)">
                <el-date-picker
                        v-model="query.#(cm.name)"
                        size="small" :picker-options="this.$pickerOptions"
                        type="daterange" value-format="yyyy-MM-dd"
                        start-placeholder="开始日期" end-placeholder="结束日期"/>
            </el-form-item>
                #else if(cm.htmlType=='DateTimePicker')
            <el-form-item label="#(cm.business)" prop="#(cm.name)">
                <el-date-picker
                        v-model="query.#(cm.name)"
                        size="small" :picker-options="this.$pickerOptions"
                        type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
                        start-placeholder="开始时间" end-placeholder="结束时间"/>
            </el-form-item>
                #end
            #end
            #end
            #end
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>