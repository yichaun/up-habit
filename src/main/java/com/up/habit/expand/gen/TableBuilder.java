package com.up.habit.expand.gen;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.up.habit.Habit;
import com.up.habit.expand.db.model.HabitModelConfig;
import com.up.habit.expand.gen.model.Table;
import com.up.habit.expand.gen.model.TableColumn;
import com.up.habit.kit.ArrayKit;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/6/1 16:09
 */
public class TableBuilder {

    protected DataSource dataSource;
    protected DataConfig dataConfig;
    protected HabitModelConfig modelConfig;
    protected Dialect dialect = new MysqlDialect();

    protected Connection conn = null;
    protected DatabaseMetaData dbMeta = null;

    protected TypeMapping typeMapping = new TypeMapping();


    public TableBuilder(DataSource dataSource, DataConfig dataConfig) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource can not be null.");
        }
        if (dataConfig == null) {
            throw new IllegalArgumentException("config can not be null.");
        }
        this.dataSource = dataSource;
        this.dataConfig = dataConfig;
        this.modelConfig = Habit.config(HabitModelConfig.class);

    }


    public void setDialect(Dialect dialect) {
        if (dialect != null) {
            this.dialect = dialect;
        }
    }

    public void setTypeMapping(TypeMapping typeMapping) {
        if (typeMapping != null) {
            this.typeMapping = typeMapping;
        }
    }

    public List<com.up.habit.expand.gen.model.Table> build() {
        System.out.println("Build Table ...");
        try {
            conn = dataSource.getConnection();
            dbMeta = conn.getMetaData();

            List<com.up.habit.expand.gen.model.Table> ret = new ArrayList<com.up.habit.expand.gen.model.Table>();
            buildTableNames(ret);
            for (com.up.habit.expand.gen.model.Table table : ret) {
                String primaryKey = buildPrimaryKey(table);
                buildColumnMetas(table, primaryKey);
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * 构造 modelName，mysql 的 tableName 建议使用小写字母，多单词表名使用下划线分隔，不建议使用驼峰命名
     * oracle 之下的 tableName 建议使用下划线分隔多单词名，无论 mysql还是 oralce，tableName 都不建议使用驼峰命名
     */
    protected String buildModelName(String tableName, Table table) {
        // 移除表名前缀仅用于生成 modelName、baseModelName，而 tableMeta.name 表名自身不能受影响
        if (dataConfig.getRemovedTableNamePrefixes() != null) {
            for (String prefix : dataConfig.getRemovedTableNamePrefixes()) {
                if (tableName.startsWith(prefix)) {
                    tableName = tableName.replaceFirst(prefix, "");
                    table.setModulePath(StrKit.firstCharToLowerCase(StrKit.toCamelCase(prefix)));
                    break;
                }
            }
        }

        // 将 oralce 大写的 tableName 转成小写，再生成 modelName
        if (dialect.isOracle()) {
            tableName = tableName.toLowerCase();
        }

        return StrKit.firstCharToUpperCase(StrKit.toCamelCase(tableName));
    }

    /**
     * 不同数据库 dbMeta.getTables(...) 的 schemaPattern 参数意义不同
     * 1：oracle 数据库这个参数代表 dbMeta.getUserName()
     * 2：postgresql 数据库中需要在 jdbcUrl中配置 schemaPatter，例如：
     * jdbc:postgresql://localhost:15432/djpt?currentSchema=public,sys,app
     * 最后的参数就是搜索schema的顺序，DruidPlugin 下测试成功
     * 3：开发者若在其它库中发现工作不正常，可通过继承 MetaBuilder并覆盖此方法来实现功能
     */
    protected ResultSet getTablesResultSet() throws SQLException {
        String schemaPattern = dialect.isOracle() ? dbMeta.getUserName() : null;
        return dbMeta.getTables(conn.getCatalog(), schemaPattern, null, new String[]{"TABLE"});
    }

    public static void setTableTpl(Table table) {
        String remarks = table.getComment();
        table.setTpl("table");
        if (StrKit.notBlank(remarks)) {
            table.setMenu(remarks.split(",")[0]);
            if (remarks.contains(",treeTable")) {
                String[] tmp = remarks.split(",treeTable");
                if (tmp.length > 1) {
                    try {
                        String treeTableInfo = tmp[1].substring(tmp[1].indexOf("[") + 1, tmp[1].indexOf("]"));
                        String[] colArray = treeTableInfo.split(",");
                        table.setTpl("treeTable").setTreeId(colArray[0]).setTreeLabel(colArray[1]).setTreePId(colArray[2]).setTreePIdDef("null".equals(colArray[3]) ? null : colArray[3]);
                    } catch (Exception e) {
                        table.setTpl("table").setTreeId(null).setTreeLabel(null).setTreePId(null).setTreePIdDef(null);
                    }
                }
            } else if (remarks.contains(",treeRightTable")) {
                String[] tmp = remarks.split(",treeRightTable");
                if (tmp.length > 1) {
                    try {
                        String treeRightTableInfo = tmp[1].substring(tmp[1].indexOf("[") + 1, tmp[1].indexOf("]"));
                        String[] colArray = treeRightTableInfo.split(",");
                        table.setTpl("treeRightTable").setTreeId(colArray[0]).setTreeAction(colArray[1]);
                    } catch (Exception e) {
                        table.setTpl("table").setTreeId(null).setTreeAction(null);
                    }
                }
            } else {
                table.setTpl("table");
            }
        }
    }

    protected void buildTableNames(List<com.up.habit.expand.gen.model.Table> ret) throws SQLException {
        ResultSet rs = getTablesResultSet();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            String remarks = rs.getString("REMARKS");
            com.up.habit.expand.gen.model.Table table = new com.up.habit.expand.gen.model.Table();
            String modelName = buildModelName(tableName, table);
            table.setComment(remarks);
            setTableTpl(table);
            ret.add(table
                    .setName(tableName)
                    /**.setPks("")*/
                    .setRemark(remarks)
                    .setAuthor(HabitGenerator.DEF_AUTHOR)
                    .setModule("默认模块")
                    /**.setModulePath("")*/
                    .setModuleIcon("iconfont ali-icon-appstore-fill")
                    .setMenuPath(StrKit.firstCharToLowerCase(modelName))
                    .setMenuIocn("iconfont ali-icon-appstore-fill")
                    .setModel(modelName)
                    .setPkg(dataConfig.getBasePackage()));
        }
        rs.close();
    }

    protected String buildPrimaryKey(com.up.habit.expand.gen.model.Table table) throws SQLException {
        ResultSet rs = dbMeta.getPrimaryKeys(conn.getCatalog(), null, table.getName());
        String primaryKey = "";
        int index = 0;
        while (rs.next()) {
            String cn = rs.getString("COLUMN_NAME");

            // 避免 oracle 驱动的 bug 生成重复主键，如：ID,ID
            if (primaryKey.equals(cn)) {
                continue;
            }

            if (index++ > 0) {
                primaryKey += ",";
            }
            primaryKey += cn;
        }
        table.setPks(primaryKey);
        rs.close();
        return primaryKey;
    }


    /**
     * TODO:
     * 文档参考：
     * http://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
     * <p>
     * JDBC 与时间有关类型转换规则，mysql 类型到 java 类型如下对应关系：
     * DATE				java.sql.Date
     * DATETIME			java.sql.Timestamp
     * TIMESTAMP[(M)]	java.sql.Timestamp
     * TIME				java.sql.Time
     * <p>
     * 对数据库的 DATE、DATETIME、TIMESTAMP、TIME 四种类型注入 new java.util.Date()对象保存到库以后可以达到“秒精度”
     * 为了便捷性，getter、setter 方法中对上述四种字段类型采用 java.util.Date，可通过定制 TypeMapping 改变此映射规则
     *
     * @param table
     * @param primaryKey
     * @throws SQLException
     */
    protected void buildColumnMetas(com.up.habit.expand.gen.model.Table table, String primaryKey) throws SQLException {
        String sql = dialect.forTableBuilderDoBuild(table.getName());
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] priKeys = ArrayKit.toStrArray(primaryKey);
        Set<String> keySet = new HashSet<>();
        for (String str : priKeys) {
            keySet.add(str);
        }
        Map<String, TableColumn> columnMetaMap = new HashMap<>();
        ResultSet colMetaRs = null;
        try {
            colMetaRs = dbMeta.getColumns(conn.getCatalog(), null, table.getName(), null);
            while (colMetaRs.next()) {
                TableColumn columnMeta = new TableColumn();
                columnMeta.setName(colMetaRs.getString("COLUMN_NAME"));
                String comment = colMetaRs.getString("REMARKS");
                columnMeta.setComment(comment);
                String type = colMetaRs.getString("TYPE_NAME");
                type = type == null ? "" : type;
                /**长度*/
                int columnSize = colMetaRs.getInt("COLUMN_SIZE");
                if (columnSize > 0) {
                    type = type + "(" + columnSize;
                    /**小数位数*/
                    int decimalDigits = colMetaRs.getInt("DECIMAL_DIGITS");
                    if (decimalDigits > 0) {
                        type = type + "," + decimalDigits;
                    }
                    type = type + ")";
                }
                /**默认值*/
                String defValue = colMetaRs.getString("COLUMN_DEF");
                /**是否允许 NULL 值*/
                String nullable = colMetaRs.getString("IS_NULLABLE");
                columnMeta.put("typeAndSize", type);
                columnMeta.put("nullable", "NO".equals(nullable));
                columnMeta.put("defValue", defValue == null ? "" : defValue);
                columnMetaMap.put(columnMeta.getName(), columnMeta);
            }
        } catch (Exception e) {
            System.out.println("无法生成 REMARKS");
        } finally {
            if (colMetaRs != null) {
                colMetaRs.close();
            }
        }

        List<TableColumn> columns = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            TableColumn column = new TableColumn();
            String columnName = rsmd.getColumnName(i);
            String columnType = rsmd.getColumnTypeName(i).toLowerCase();
            column.setName(columnName);
            column.setType(columnType);
            if (columnMetaMap.containsKey(column.getName())) {
                column.put(columnMetaMap.get(column.getName()));
            }
            column.setJavaType(handleJavaType(rsmd, i));
            column.setIncrement(rsmd.isAutoIncrement(i));
            column.setPk(keySet.contains(columnName));
            columns.add(buildColumn(table, column));
        }
        table.put("columns", columns);
        rs.close();
        stm.close();
    }


    protected TableColumn buildColumn(com.up.habit.expand.gen.model.Table table, TableColumn cm) {
        String columnName = cm.getName();
        cm.setRequired(true);
        cm.setInserted(true);
        cm.setEdit(true);
        cm.setList(true);
        cm.setQuery(true);
        if (columnName.equals(modelConfig.getCreated()) || columnName.equals(modelConfig.getCreatedId()) || columnName.equals(modelConfig.getModifyId()) || columnName.equals(modelConfig.getModified()) || columnName.equals(modelConfig.getModified())) {
            cm.setRequired(false).setInserted(false).setEdit(false);
            cm.setList(columnName.equals(modelConfig.getCreated())).setQuery(columnName.equals(modelConfig.getCreated()));
        }
        if (columnName.equals(modelConfig.getDel())) {
            cm.setRequired(false);
            cm.setInserted(false);
            cm.setEdit(false);
            cm.setList(false);
            cm.setQuery(false);
        }
        if (columnName.equals(modelConfig.getSort())) {
            cm.setRequired(true);
            cm.setInserted(true);
            cm.setEdit(true);
            cm.setList(true);
            cm.setQuery(false);
        }
        if ("treeTabel".equals(table.getTpl())) {
            if (columnName.equals(table.getTreePId())) {
                cm.setRequired(true);
                cm.setInserted(true);
                cm.setEdit(true);
                cm.setList(false);
                cm.setQuery(false);
            }
            if (columnName.equals(table.getTreeId())) {
                cm.setRequired(true);
                cm.setInserted(true);
                cm.setEdit(true);
                cm.setList(false);
                cm.setQuery(false);
            }
        }

        /**自增主键*/
        if (cm.getPk() && cm.getIncrement()) {
            cm.setQuery(false);
        }
        cm.setHtmlType(typeMapping.getHtmlType(cm.getJavaType()));
        String comment = cm.getComment();
        if (StrKit.notBlank(comment)) {
            String[] tempArray = ArrayKit.toStrArray(comment);
            if (tempArray.length > 0) {
                cm.setBusiness(tempArray[0]);
            }
            if (tempArray.length > 1) {
                String typeStr = tempArray[1];
                if (typeStr.startsWith("dict:")) {
                    cm.setHtmlType("Select");
                    cm.setDictType(typeStr.substring(5));
                } else if (typeStr.startsWith("img")) {
                    cm.setHtmlType("Image");
                } else if (typeStr.startsWith("richText")) {
                    cm.setHtmlType("RichText");
                }
            }
        }
        return cm;
    }


    protected String handleJavaType(ResultSetMetaData rsmd, int i) throws SQLException {
        String typeStr = null;
        if (dialect.isKeepByteAndShort()) {
            int type = rsmd.getColumnType(i);
            if (type == Types.TINYINT) {
                typeStr = "java.lang.Byte";
            } else if (type == Types.SMALLINT) {
                typeStr = "java.lang.Short";
            }
        }

        if (typeStr == null) {
            String colClassName = rsmd.getColumnClassName(i);
            typeStr = typeMapping.getType(colClassName);
        }

        if (typeStr == null) {
            int type = rsmd.getColumnType(i);
            if (type == Types.BINARY || type == Types.VARBINARY || type == Types.LONGVARBINARY || type == Types.BLOB) {
                typeStr = "byte[]";
            } else if (type == Types.CLOB || type == Types.NCLOB) {
                typeStr = "java.lang.String";
            }
            // 支持 oracle 的 TIMESTAMP、DATE 字段类型，其中 Types.DATE 值并不会出现
            // 保留对 Types.DATE 的判断，一是为了逻辑上的正确性、完备性，二是其它类型的数据库可能用得着
            else if (type == Types.TIMESTAMP || type == Types.DATE) {
                typeStr = "java.util.Date";
            }
            // 支持 PostgreSql 的 jsonb json
            else if (type == Types.OTHER) {
                typeStr = "java.lang.Object";
            } else {
                typeStr = "java.lang.String";
            }
        }

        if (dialect.isOracle()) {
            // 默认实现只处理 BigDecimal 类型
            if ("java.math.BigDecimal".equals(typeStr)) {
                /**小数点右边的位数，值为 0 表示整数*/
                int scale = rsmd.getScale(i);
                /**最大精度*/
                int precision = rsmd.getPrecision(i);
                if (scale == 0) {
                    if (precision <= 9) {
                        typeStr = "java.lang.Integer";
                    } else if (precision <= 18) {
                        typeStr = "java.lang.Long";
                    } else {
                        typeStr = "java.math.BigDecimal";
                    }
                } else {
                    // 非整数都采用 BigDecimal 类型，需要转成 double 的可以覆盖并改写下面的代码
                    typeStr = "java.math.BigDecimal";
                }
            }

        }
        return typeStr;
    }

}
