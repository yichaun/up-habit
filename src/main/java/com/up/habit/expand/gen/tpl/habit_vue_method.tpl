####方法模版模版
#set(isTreeTable=table.tpl=='treeTable')
#set(isTreeRightTable=table.tpl=='treeRightTable')
            #if(isTreeRightTable)
             //左侧树选择
             selectTree(node, treeData) {
                 this.tree.current = node.id;
                 this.tree.data = treeData;
                 this.query.#(table.tree_id) = node.id;
                 this.handleQuery();
             },
            #end
            //查询
            handleQuery() {
                page(this.query, ok => {
                    this.tableData = ok.data.list;
                    #if(isTreeTable)
                    this.tableData = this.tableData.map(item => {
                        this.$set(item, 'hasChildren', true);
                        return item;
                    })
                    #end
                    this.query.pageNo = ok.data.pageNumber;
                    this.query.pageSize = ok.data.pageSize;
                    this.total = ok.data.totalRow;
                })
                #if(table.tpl=='treeTable')
                this.getTreeSelect();
                #end
            },
            #if(isTreeTable)###树表
            //加载子列表
            handleQueryChild(row, treeNode, resolve){
                list(row.#(table.tree_id), ok => {
                    let list = ok.data;
                    list = list.map(item => {
                        this.$set(item, 'hasChildren', true);
                        return item;
                    })
                    resolve(list)
                })
            },
            //刷新子列表
            refreshTable(pId) {
                list(pId, ok => {
                    let list = ok.data;
                    list = list.map(item => {
                        this.$set(item, 'hasChildren', true);
                        return item;
                    })
                    this.$set(this.$refs.table.store.states.lazyTreeNodeMap, pId, list)
                })
            },
            //下拉树选择数据
            getTreeSelect() {
                treeSelect(ok => {
                    this.treeSelect = [];
                    const tree = {id: '#(table.tree_p_id_def)', label: '根类目', children: []};
                    tree.children = ok.data;
                    this.treeSelect.push(tree);
                })
            },
            #end
            //重置查询
            resetQuery() {
            #if(isTreeRightTable)
                this.query = {#(table.tree_id): this.tree.current};
            #else
                this.query = {};
            #end
                this.handleQuery();
            },
            //列表选择
            handleSelectionChange(selection) {
                #for(pk:toStrArray(table.pks))
                this.multiple.#(pk)s = selection.map(item => item.#(pk));
                #end
            },
            //新增
            handleAdd(row) {
                this.resetForm("form");
                this.info = {
                #for(cm:columns)
                #if(cm.inserted)###是否插入字段
                #if(cm.java_type=='java.lang.Boolean')
                    #(cm.name): this.yesOrNo[0].value,
                #else if(cm.dict_type)###是否有字典类型
                    #(cm.name):this.$store.state.dict.options['#(cm.dict_type)'][0].value,
                #end
                #end
                #end
                };
                #if(isTreeTable)###是否树表
                this.info.#(table.tree_p_id) = row.#(table.tree_id) || '';
                #end
                #if(isTreeRightTable)
                this.info.#(table.tree_id)=this.tree.current;
                #end
                this.dialog.title = '新增';
                this.dialog.visible = true;
            },
            //编辑
            handleEdit(row) {
                info(row.#(table.pks), ok => {
                    this.resetForm("form");
                    this.info = ok.data;
                    this.dialog.title = '修改';
                    this.dialog.visible = true;
                })

            },
            //弹框提交添加编辑
            handleSubmit() {
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        if (this.dialog.title === '新增') {
                            add(this.info, ok => {
                                this.$message.success(ok.msg);
                                this.dialog.visible = false;
                                #if(table.tpl=='treeTable')
                                if(this.info.#(table.tree_p_id)==this.treeSelect[0].id){
                                    this.resetQuery();
                                }else{
                                    this.refreshTable(this.info.#(table.tree_p_id))
                                }
                                #else
                                this.resetQuery();
                                #end
                            })
                        } else {
                            edit(this.info, ok => {
                                this.$message.success(ok.msg)
                                this.dialog.visible = false;
                                #if(table.tpl=='treeTable')
                                if(this.info.#(table.tree_p_id)==this.treeSelect[0].id){
                                    this.resetQuery();
                                }else{
                                    this.refreshTable(this.info.#(table.tree_p_id))
                                }
                                #else
                                this.resetQuery();
                                #end
                            })
                        }
                    } else {
                        return false;
                    }
                });
            },
            //删除记录
            handleDelete(row) {
                #set(pkCount=0)
                #for(pk:toStrArray(table.pks))
                const #(pk) = row.#(pk) || this.multiple.#(pk)s;
                #set(pkCount++)
                #end
                this.$msgbox.confirm(#if(pkCount>1)'确认删除数据?'#else'确认删除编号 [' + #(table.pks) + '] 的数据?'#end,
                    '提示', {
                        confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
                    }).then(() => {
                    remove(#(vue.functionPks), ok => {
                        this.$message.success("删除成功!");
                        #if(table.tpl=='treeTable')
                        if (row.p_id) {
                            this.refreshTable(row.p_id)
                        } else {
                            this.tableData=[]
                            this.resetQuery();
                        }
                        #else
                        this.resetQuery();
                        #end
                    })
                });
            },