package com.up.habit.expand.gen;

import com.jfinal.kit.JavaKeyword;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.up.habit.Habit;
import com.up.habit.app.config.HabitConfig;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.app.controller.validator.type.ArrayValidate;
import com.up.habit.app.controller.validator.type.StringValidate;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.expand.db.model.HabitModelConfig;
import com.up.habit.expand.gen.model.Table;
import com.up.habit.expand.gen.model.TableColumn;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.DateKit;
import com.up.habit.kit.FileKit;
import com.up.habit.kit.StrKit;
import org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/6/1 14:39
 */
public class HabitGenerator {

    public static HabitGenerator me = new HabitGenerator();

    protected HabitModelConfig modelConfig = Habit.config(HabitModelConfig.class);
    protected DataConfig dataConfig = null;

    protected Engine engine;

    protected TypeMapping typeMapping = new TypeMapping();

    /**
     * 默认包名
     */
    public final static String BASE_PACKAGE_NAME = "com.up.app";
    /**
     * 默认作者
     */
    public final static String DEF_AUTHOR = "王剑洪";

    public static final String TPL_TABLE = "table";
    public static final String TPL_TREE_TABLE = "treeTable";
    public static final String TPL_TREE_RIGHT_TABLE = "treeRightTable";

    /**
     * 文档生成路径
     */
    protected String dictionaryOutputPath = PathKit.getWebRootPath() + "/src/main/doc/";

    /**
     * 模版路径
     * modelTemplate model模版
     * dicHtmlTemplate 数据库字典网页模版
     * serviceTemplate service模版
     * sqlTemplate sql模版
     * controllerTemplate 控制器模版
     * vueTemplate vue页面模版模版
     * jsTemplate js页面模版模版
     */
    protected String modelTemplate = "/com/up/habit/expand/gen/tpl/habit_model.tpl";
    protected String dicHtmlTemplate = "/com/up/habit/expand/gen/tpl/habit_sql_dic_html.tpl";
    protected String serviceTemplate = "/com/up/habit/expand/gen/tpl/habit_service.tpl";
    protected String sqlTemplate = "/com/up/habit/expand/gen/tpl/habit_sql.tpl";
    protected String controllerTemplate = "/com/up/habit/expand/gen/tpl/habit_controller.tpl";
    protected String vueTemplate = "/com/up/habit/expand/gen/tpl/habit_vue_table.tpl";
    protected String jsTemplate = "/com/up/habit/expand/gen/tpl/habit_vue_js.tpl";

    private HabitGenerator() {
        engine = Engine.use().setToClassPathSourceFactory()
                .addSharedMethod(new StrKit())
                .addSharedMethod(new ArrayKit())
                .addSharedMethod(new GeneratorKit())
                .addSharedObject("javaKeyword", JavaKeyword.me)
                .addSharedObject("getterTypeMap", typeMapping.getGetterTypeMap());
    }

    public void setTypeMapping(TypeMapping typeMapping) {
        this.typeMapping = typeMapping;
        engine.removeSharedObject("getterTypeMap");
        engine.addSharedObject("getterTypeMap", typeMapping.getGetterTypeMap());
    }

    public TypeMapping getTypeMapping() {
        return this.typeMapping;
    }

    public DataSource getSource(DataConfig config) {
        String url = config.getUrl();
        if (!url.startsWith("jdbc:mysql://")) {
            url = "jdbc:mysql://" + url;
        }
        DruidPlugin druid = new DruidPlugin(url, config.getUser(), config.getPassword());
        druid.setConnectionProperties("useInformationSchema=true;remarks=true");
        druid.start();
        ActiveRecordPlugin activeRecord = new ActiveRecordPlugin(druid);
        activeRecord.start();
        return druid.getDataSource();
    }


    /**
     * TODO:代码生成
     *
     * @param config
     */
    public void build(DataConfig config) {
        this.dataConfig = config;
        if (StrKit.isBlank(config.getBasePackage())) {
            config.setBasePackage(BASE_PACKAGE_NAME);
        }
        String rootPath = PathKit.getWebRootPath();
        DataSource source = getSource(config);
        TableBuilder metaBuilder = new TableBuilder(source, config);
        metaBuilder.setTypeMapping(typeMapping);
        metaBuilder.setDialect(new MysqlDialect());
        List<Table> tableMetas = metaBuilder.build();
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE DATABASE `" + config.getName() + "`;\n");
        sql.append("USE  `" + config.getName() + "`;\n");
        List<String> buildTables = config.getTable();
        for (Table meta : tableMetas) {
            sql.append(String.format("\n\n\n/**\n * table name :<%s><%s>\n *\n */\nDROP TABLE IF EXISTS `%s`;\n\n%s;\n",
                    meta.getName(), meta.getComment(), meta.getName(),
                    Db.findFirst(String.format("SHOW CREATE TABLE %s", meta.getName())).getStr("Create Table")));
            System.out.println("table >>>>> " + meta.getName());
            if (StrKit.notBlank(meta.getPks())) {
                if (config.getExcludedTables().contains(meta.getName())) {
                    System.out.println("table >>>>> " + meta.getName() + " skip...");
                    continue;
                }
                if (ArrayKit.isNotEmpty(buildTables)) {
                    boolean has=false;
                    for (String table : buildTables) {
                        if (meta.getName().startsWith(table)) {
                            has=true;
                            System.out.println("table >>>>> " + meta.getName() + " building in config...");
                            buildFile(meta, rootPath);
                            System.out.println("table >>>>> " + meta.getName() + " complete...");
                        }
                    }
                    if (!has){
                        System.out.println("table >>>>> " + meta.getName() + " skip...");
                    }
                } else {
                    System.out.println("table >>>>> " + meta.getName() + " building...");
                    buildFile(meta, rootPath);
                    System.out.println("table >>>>> " + meta.getName() + " complete...");
                }
            }
        }
        System.out.println("Build Table Finish...");
        //创建数据库
        if (config.isSqlDict()) {
            FileKit.renderByString(sql.toString(), dictionaryOutputPath + config.getName() + "_" + DateKit.toStr(new Date(), "yyyyMMddHHmmss") + ".sql", true);
        }
        if (config.isHtmlDict()) {
            //html文档
            String html = engine.getTemplate(dicHtmlTemplate).renderToString(Kv.create().set("tables", tableMetas).set("dbName", config.getName() + "_" + DateKit.toStr(new Date(), "yyyyMMddHHmmss") + ".txt"));
            FileKit.renderByString(html, dictionaryOutputPath + config.getName() + "_" + DateKit.toStr(new Date(), "yyyyMMddHHmmss") + ".html", true);
        }
    }

    /**
     * TODO:文件生成
     *
     * @param meta
     * @param rootPath
     * @return void
     * @Author 王剑洪 on 2020/7/25 21:42
     **/
    protected void buildFile(Table meta, String rootPath) {
        Ret content = build(meta, meta.get("columns"), rootPath);
        Kv path = (Kv) content.get("path");
        if (dataConfig == null) {
            FileKit.renderByString(content.getStr("model"), path.getStr("modelPath"), true);
            FileKit.renderByString(content.getStr("service"), path.getStr("servicePath"), true);
            FileKit.renderByString(content.getStr("sql"), path.getStr("sqlPath"), true);
            FileKit.renderByString(content.getStr("controller"), path.getStr("controllerPath"), true);
            FileKit.renderByString(content.getStr("js"), path.getStr("jsPath"), true);
            FileKit.renderByString(content.getStr("vue"), path.getStr("vuePath"), true);
        } else {
            if (dataConfig.isModel()) {
                FileKit.renderByString(content.getStr("model"), path.getStr("modelPath"), true);
            }
            if (dataConfig.isService()) {
                FileKit.renderByString(content.getStr("service"), path.getStr("servicePath"), true);
            }
            if (dataConfig.isSql()) {
                FileKit.renderByString(content.getStr("sql"), path.getStr("sqlPath"), true);
            }
            if (dataConfig.isController()) {
                FileKit.renderByString(content.getStr("controller"), path.getStr("controllerPath"), true);
            }
            if (dataConfig.isJs()) {
                FileKit.renderByString(content.getStr("js"), path.getStr("jsPath"), true);
            }
            if (dataConfig.isVue()) {
                FileKit.renderByString(content.getStr("vue"), path.getStr("vuePath"), true);
            }
        }


    }

    public Ret build(Table table, List<TableColumn> columns, String rootPath) {
        Kv path = expandPathTemp(table, rootPath);
        Kv para = expandJavaTemp(table, columns);
        Kv vue = expandVueTemp(table, columns);
        Kv kv = Kv.by("table", table).set("columns", columns).set("path", path).set("para", para).set("vue", vue).set("dateTime", DateKit.toStr(new Date(), DateKit.timeStampPattern));
        String modelContent = this.engine.getTemplate(modelTemplate).renderToString(kv);
        String serviceContent = this.engine.getTemplate(serviceTemplate).renderToString(kv);
        String sqlContent = this.engine.getTemplate(sqlTemplate).renderToString(kv);
        String controllerContent = this.engine.getTemplate(controllerTemplate).renderToString(kv);
        String jsContent = this.engine.getTemplate(jsTemplate).renderToString(kv);
        String vueContent = this.engine.getTemplate(vueTemplate).renderToString(kv);
        return Ret.create().set("path", path)
                .set("model", modelContent).set("sql", sqlContent).set("service", serviceContent).set("controller", controllerContent)
                .set("js", jsContent).set("vue", vueContent);
    }

    /**
     * 生成ZIP byte数组
     *
     * @param tables
     * @return
     */
    public byte[] genZip(List<Table> tables) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            List<TableColumn> columns = table.get("columns");
            Ret content = build(table, columns, "");
            Kv path = (Kv) content.get("path");
            gen(zip, content.getStr("model"), path.getStr("modelPath"));
            gen(zip, content.getStr("service"), path.getStr("servicePath"));
            gen(zip, content.getStr("sql"), path.getStr("sqlPath"));
            gen(zip, content.getStr("controller"), path.getStr("controllerPath"));
            gen(zip, content.getStr("js"), path.getStr("jsPath"));
            gen(zip, content.getStr("vue"), path.getStr("vuePath"));
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * TODO:代码生成zip流
     *
     * @param zip
     * @param content
     * @param fileName
     * @return void
     * @Author 王剑洪 on 2020/7/25 21:44
     **/
    public void gen(ZipOutputStream zip, String content, String fileName) {
        try {
            zip.putNextEntry(new ZipEntry(fileName));
            IOUtils.write(content, zip, Habit.CHART_UTF8);
            zip.flush();
            zip.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO:包名转路径
     *
     * @param packageName
     * @return java.lang.String
     * @Author 王剑洪 on 2020/7/25 21:43
     **/
    protected String package2Path(String packageName) {
        String packagePath = packageName.replace("..", ".").replace(".", "/");
        if (!packagePath.endsWith("/")) {
            packagePath += "/";
        }
        return packagePath;
    }

    /**
     * TODO:路径拓展字段
     *
     * @param meta
     * @param rootPath
     * @return com.jfinal.kit.Kv
     * @Author 王剑洪 on 2020/7/25 21:08
     **/
    private Kv expandPathTemp(Table meta, String rootPath) {
        String baseDir = rootPath + "/src/main/java/";
        String viewDir = rootPath + "/src/main/vue";
        if (StrKit.notBlank(rootPath)) {
            int index = baseDir.lastIndexOf(File.separator);
            String proPath = baseDir.substring(0, index);
            if (StrKit.notBlank(dataConfig.getVuePath())) {
                viewDir = proPath + "/" + dataConfig.getVuePath() + "/src/";
            } else {
                viewDir = proPath + "/up-vue-admin/src/";
            }

        }
        String modulePkg = StrKit.isBlank(meta.getModulePath()) ? "" : ("." + meta.getModulePath());
        String modulePath = StrKit.isBlank(meta.getModulePath()) ? "" : (meta.getModulePath() + "/");
        Kv kv = Kv.create();
        /**实体类包名,实体类文件路径*/
        String modelPkg = meta.getPkg() + ".model" + modulePkg;
        String modelPath = baseDir + package2Path(modelPkg) + meta.getModel() + ".java";
        kv.set("modelPackage", modelPkg).set("modelPath", modelPath);
        /**service类包名,service类文件路径,sql文件路径*/
        String srvPkg = meta.getPkg() + ".service" + modulePkg;
        String srvPath = baseDir + package2Path(srvPkg) + meta.getModel() + "Service.java";
        String sqlPath = baseDir + package2Path(srvPkg) + "sql/" + meta.getName() + ".sql";
        kv.set("servicePackage", srvPkg).set("servicePath", srvPath).set("sqlPath", sqlPath);
        /**controller类包名,controller类文件路径*/
        String ctrPkg = meta.getPkg() + ".controller.admin" + modulePkg;
        String ctrPath = baseDir + package2Path(ctrPkg) + meta.getModel() + "Controller.java";
        kv.set("controllerPackage", ctrPkg).set("controllerPath", ctrPath);
        /**js文件导入路径,js文件路径*/
        String jsCodePath = "/api/" + modulePath + meta.getMenuPath();
        kv.set("jsImportPath", jsCodePath).set("jsPath", viewDir + jsCodePath + ".js");
        /**vue页面文件路径*/
        String vuePath = "/views/" + modulePath + meta.getMenuPath() + "/index.vue";
        kv.set("vuePath", viewDir + vuePath);
        /**控制器接口访问路径*/;
        kv.set("controllerAction", modulePath + meta.getMenuPath() + "/");
        return kv;
    }

    /**
     * TODO:代码生成拓展
     *
     * @param table
     * @param columnList
     */
    protected Kv expandJavaTemp(Table table, List<TableColumn> columnList) {
        Kv para = Kv.create();
        /**注解参数新增*/
        String tableInfo = "";
        String add = "", addRemove = "";
        String delete = "", deleteGet = "", info = "", infoGet = "";
        String edit = "", editRemove = "";
        String query = "", list = "";
        /**是否有删除标识字段*/
        boolean isFake = false;
        String sortColumnName = "";
        for (int i = 0; i < columnList.size(); i++) {
            tableInfo += columnList.get(i).getName() + ":-" + columnList.get(i).getBusiness() + ((i < columnList.size() - 1) ? ":|" : "");
            TableColumn column = columnList.get(i);
            boolean isTreeTablePId = TPL_TREE_TABLE.equals(table.getTpl()) && table.getTreePId().equals(column.getName());
            String name = column.getName();
            String des = column.getBusiness();
            Class<?> validateClazz = getTypeMapping().getValidateType(column.getJavaType());
            isFake = isFake ? true : column.getName().equals(modelConfig.getDel());
            if (column.getName().equals(modelConfig.getSort())) {
                sortColumnName = name;
            }
            boolean isIncrementPk = column.getPk() && column.getIncrement();
            /*插入,非删除,非自增主键*/
            if (column.getInserted() && !column.getName().equals(modelConfig.getDel()) && !isIncrementPk) {
                boolean requested = column.getRequired();
                requested = isTreeTablePId ? StrKit.notBlank(table.getTreePIdDef()) : requested;
                add = param(add, name, des, requested, validateClazz, column.getDefValue(), column.getDictType(), table, null);
            } else {
                addRemove += (StrKit.isBlank(addRemove) ? "" : ", ") + table.getModel() + "." + name.toUpperCase();
            }
            /*编辑字段且非删除,或主键*/
            boolean notDelEdit = column.getEdit() && !column.getName().equals(modelConfig.getDel());
            if (notDelEdit || column.getPk()) {
                boolean requested = column.getRequired();
                requested = isTreeTablePId ? StrKit.notBlank(table.getTreePIdDef()) : requested;
                edit = param(edit, name, des, requested, validateClazz, column.getDefValue(), column.getDictType(), table, null);
            } else {
                editRemove += (StrKit.isBlank(editRemove) ? "" : ", ") + table.getModel() + "." + name.toUpperCase();
            }
            if (column.getPk()) {
                delete = param(delete, name, des + "列表,可数组格式或逗号隔开", true, ArrayValidate.class, "", "", table, null);
                info = param(info, name, des, true, ArrayValidate.class, "", "", table, null);
            }
            if (column.getQuery()) {
                if (column.getJavaType().equals(Date.class.getSimpleName())) {
                    query = param(query, name, des, false, validateClazz, "", column.getDictType(), table, "[0]");
                    query = param(query, name, des, false, validateClazz, "", column.getDictType(), table, "[1]");
                } else {
                    query = param(query, name, des, false, validateClazz, "", column.getDictType(), table, null);
                }
            }
            if (column.getList() || column.getPk()) {
                list += (StrKit.isBlank(list) ? "" : ", ") + table.getModel() + "." + name.toUpperCase();
            }
        }
        String[] pks = ArrayKit.toStrArray(table.getPks());
        for (int i = 0; i < pks.length; i++) {
            String columnName = table.getModel() + "." + pks[i].toUpperCase();
            deleteGet += (StrKit.isBlank(deleteGet) ? "" : ", ") + "getArray(" + columnName + ")";
            infoGet += (StrKit.isBlank(infoGet) ? "" : ", ") + "get(" + columnName + ")";
        }
        String paramTemp = "@Params({%s\n\t})";
        para.set("tableInfo", tableInfo).set("isFake", isFake).set("sort", sortColumnName)
                .set("add", String.format(paramTemp, add)).set("delete", String.format(paramTemp, delete)).set("edit", String.format(paramTemp, edit))
                .set("query", String.format(paramTemp, query)).set("info", String.format(paramTemp, info))
                .set("addRemove", addRemove).set("deleteGet", deleteGet).set("editRemove", editRemove).set("infoGet", infoGet).set("list", list);
        return para;
    }

    private String param(String paramsStr,
                         String name, String des, boolean requested, Class<?> clazz, String defValue, String dict,
                         Table table, String nameExp) {
        StringBuffer sb = new StringBuffer();
        if (StrKit.notBlank(paramsStr)) {
            sb.append(paramsStr).append(",");
        }
        sb.append("\n\t\t\t").append("@Param(");
        sb.append("name = ").append(table.getModel()).append(".").append(name.toUpperCase());
        if (StrKit.notBlank(nameExp)) {
            sb.append(" + \"").append(nameExp).append("\"");
        }
        sb.append(", des = \"").append(des).append("\"");
        if (!requested) {
            sb.append(", required = false");
        }
        if (!clazz.getSimpleName().equals(StringValidate.class.getSimpleName())) {
            sb.append(", dataType = " + clazz.getSimpleName() + ".class");
        } else if (StrKit.notBlank(dict)) {
            sb.append(", dataType = DictValueValidate.class");
        }
        if (StrKit.notBlank(defValue)) {
            sb.append(", defaultValue = \"" + defValue + "\"");
        }
        if (StrKit.notBlank(dict)) {
            sb.append(", dict = \"" + dict + "\"");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * TODO:vue 页面拓展
     *
     * @param table
     * @param columnList
     * @return com.jfinal.kit.Kv
     * @Author 王剑洪 on 2020/7/25 15:31
     **/
    protected Kv expandVueTemp(Table table, List<TableColumn> columnList) {
        Kv vueExpand = Kv.create();
        String[] pks = ArrayKit.toStrArray(table.getPks());
        /**js主键参数,提交参数*/
        String functionPks = "", paraPks = "";
        for (int i = 0; i < pks.length; i++) {
            functionPks += StrKit.isBlank(functionPks) ? "" : (functionPks + ", ");
            functionPks += pks[i];
            paraPks += StrKit.isBlank(paraPks) ? "" : (paraPks + ", ");
            paraPks += pks[i] + ": " + pks[i];
        }
        vueExpand.set("functionPks", functionPks).set("paraPks", "{" + paraPks + "}");
        /**import组件,组件载入到页面*/
        String componentImport = "", components = "";
        for (TableColumn column : columnList) {
            if (column.getInserted() || column.getEdit()) {
                if ("Image".equals(column.getHtmlType())) {
                    componentImport += StrKit.isBlank(componentImport) ? "" : "\n";
                    componentImport += "\timport ImageUpload from '@/components/ImageUpload'";
                    components += StrKit.isBlank(components) ? "" : ", ";
                    components += "ImageUpload";
                } else if ("RichText".equals(column.getHtmlType())) {
                    componentImport += StrKit.isBlank(componentImport) ? "" : "\n";
                    componentImport += "\timport Tinymce from '@/components/Tinymce'";
                    components += StrKit.isBlank(components) ? "" : ", ";
                    components += "Tinymce";
                }
            }
        }
        if ("treeTable".equals(table.getTpl()) || "treeRightTable".equals(table.getTpl())) {
            componentImport += StrKit.isBlank(componentImport) ? "" : "\n";
            componentImport += "\timport Treeselect from '@riophae/vue-treeselect';";
            componentImport += "\n\timport '@riophae/vue-treeselect/dist/vue-treeselect.css';";
            components += StrKit.isBlank(components) ? "" : ", ";
            components += "Treeselect";
            if ("treeRightTable".equals(table.getTpl())) {
                componentImport += "\n\timport LeftTree from \"@/layout/LeftTree\";";
                components += ", LeftTree";
            }
        }

        vueExpand.set("componentImport", componentImport).set("components", components);
        return vueExpand;
    }


}
