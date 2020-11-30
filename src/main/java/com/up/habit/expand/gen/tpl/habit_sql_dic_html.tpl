###数据库说明文档,html格式模版
<!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="utf-8">
                    <meta name="author" content="yichuan95@126.com">
                    <title>数据库表结构</title>
                    <style type="text/css">
                        table {
                            color: #666;
                            border: 1px solid #cad9ea;
                            empty-cells: show;
                            border-collapse: collapse;
                            margin: 20px auto 50px;
                            cursor: default;
                            width: 80%
                        }

                        table th {
                            font-weight: bold;
                            color: #4D647C;
                            background: #E5EDF9;
                            height: 30px;
                            word-break: keep-all;
                            white-space: nowrap;
                            border: 1px solid #cad9ea;
                            padding: 0 1em 0;
                        }

                        table td {
                            height: 25px;
                            border: 1px solid #cad9ea;
                            padding: 0 1em 0;
                        }

                        table tr:hover {
                            background: #FFFF99;

                        }
                    </style>
                </head>

                <body>
                <table id="all">
                    #for(tb : tables)

                    <tr style="height: 70px">
                        <th colspan="6" class="tableRow">
                            #if(tb.comment)
                            #(tb.comment)：
                            #end
                            #(tb.name)
                            #if(tb.pks)
                            （主键：#(tb.pks)）
                            #end
                        </th>
                    </tr>
                    <tr class="tableTitle">
                        <th>序号
                        </td>
                        <th>字段名
                        </td>
                        <th>类型
                        </td>
                        <th>是否允许为空
                        </td>
                        <th>默认值
                        </td>
                        <th>说明
                        </td>
                    </tr>
                    #for(col : tb.columns)
                    <tr>
                        <td>#(for.count)</td>
                        <td #if(col.pk) style="color: red;" #end>#(col.name)</td>
                        <td>#(col.typeAndSize)</td>
                        <td>#(col.nullable)</td>
                        <td>#(col.defValue)</td>
                        <td>#(col.comment)</td>
                    </tr>
                    #end
                    #end
                </table>
                </body>
                </html>";