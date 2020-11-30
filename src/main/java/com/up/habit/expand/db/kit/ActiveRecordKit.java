package com.up.habit.expand.db.kit;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.sql.SqlKit;
import com.jfinal.template.Template;
import com.up.habit.expand.db.dialect.HabitDialect;
import com.up.habit.expand.db.dialect.MysqlHabitDialect;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.StrKit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * TODO:数据库
 *
 * @author 王剑洪 on 2020/1/13 23:03
 */
public class ActiveRecordKit {


    public static final String SQL_SUFFIX = ".sql";
    public static final String CLASS_SUFFIX = ".class";
    public static final String FILE_SEPARATOR = "/";

    private static final Map<String, HabitDialect> habitDialect = new HashMap<>();

    public static void addDialect(String name, String habitDialectName) {
        if (StrKit.isBlank(habitDialectName)) {
            habitDialect.put(name, new MysqlHabitDialect());
        } else {
            try {
                Class<HabitDialect> clazz = ClassKit.loadClass(habitDialectName);
                HabitDialect dialect = ClassKit.newInstance(clazz);
                habitDialect.put(name, dialect);
            } catch (Exception e) {
                habitDialect.put(name, new MysqlHabitDialect());
            }
        }
    }

    public static HabitDialect getDialect(String name) {
        HabitDialect dialect = habitDialect.get(name);
        if (dialect == null) {
            habitDialect.put(name, new MysqlHabitDialect());
            return getDialect(name);
        }
        return dialect;
    }


    /**
     * TODO:添加sql模版
     *
     * @param arp
     * @param sqlPackageNames
     * @return void
     * @Author 王剑洪 on 2020/1/14 1:32
     **/
    public static void addSqlTemplate(ActiveRecordPlugin arp, String sqlPackageNames, String without) {
        List<String> withoutList = new ArrayList<>();
        if (StrKit.notBlank(without)) {
            String[] withoutArray = ArrayKit.toStrArray(without);
            withoutList = Arrays.asList(withoutArray);
        }
        if (StrKit.notBlank(sqlPackageNames)) {
            String[] packageArray = sqlPackageNames.split(",");
            for (String packageName : packageArray) {
                if (StrKit.isBlank(packageName)) {
                    continue;
                }
                try {
                    scan(arp, packageName, withoutList, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * TODO:添加数据映射
     *
     * @param arp
     * @param modelPackageName
     * @return void
     * @Author 王剑洪 on 2020/1/14 1:29
     **/
    public static void addMapping(ActiveRecordPlugin arp, String modelPackageName, String without) {

        List<String> withoutList = new ArrayList<>();
        if (StrKit.notBlank(without)) {
            String[] withoutArray = ArrayKit.toStrArray(without);
            withoutList = Arrays.asList(withoutArray);
        }
        if (StrKit.notBlank(modelPackageName)) {
            String[] packageArray = modelPackageName.split(",");
            for (String packageName : packageArray) {
                if (StrKit.isBlank(packageName) || withoutList.contains(packageName)) {
                    continue;
                }
                try {
                    scan(arp, packageName, withoutList, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * TODO:扫描包名路径下的文件
     *
     * @param arp
     * @param packageName
     * @param isAddMapping
     * @return void
     * @Author 王剑洪 on 2020/1/14 1:31
     **/
    public static void scan(ActiveRecordPlugin arp, String packageName, List<String> without, boolean isAddMapping) throws IOException {
        String path = packageName.replace(".", FILE_SEPARATOR);
        Enumeration<URL> urls = ClassKit.getContextClassLoader().getResources(path);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url != null) {
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String pathTemp = path;
                    if (!pathTemp.startsWith(FILE_SEPARATOR)) {
                        pathTemp = FILE_SEPARATOR + pathTemp;
                    }
                    if (!pathTemp.endsWith(FILE_SEPARATOR)) {
                        pathTemp = pathTemp + FILE_SEPARATOR;
                    }
                    recursionScan(packageName, pathTemp, new File(url.getFile()), arp, without, isAddMapping);
                    break;
                } else {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();

                                if (isAddMapping) {
                                    if (jarEntryName.endsWith(CLASS_SUFFIX)) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        if (className.startsWith(packageName)){
                                            addReflectMapping(arp, className, without);
                                        }
                                    }
                                } else {
                                    if (jarEntryName.endsWith(SQL_SUFFIX)) {
                                        if (!jarEntryName.startsWith(FILE_SEPARATOR)) {
                                            jarEntryName = FILE_SEPARATOR + jarEntryName;
                                        }
                                        if (!without.contains(jarEntryName)){
                                            arp.addSqlTemplate(jarEntryName);
                                        }

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * TODO:递归扫描文件
     *
     * @param file
     * @return void
     * @Author 王剑洪 on 2020/1/13 23:31
     **/
    public static void recursionScan(String packageName, String path, File file, ActiveRecordPlugin arp, List<String> without, boolean isAddMapping) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tmp : files) {
                recursionScan(packageName, path, tmp, arp, without, isAddMapping);
            }
        } else {
            String name = file.getName();
            if (name.endsWith(SQL_SUFFIX) && !isAddMapping && !without.contains(name)) {
                String temp = file.getPath().replace("\\", ".").replace(FILE_SEPARATOR, ".").split(path.replace(FILE_SEPARATOR, "."))[1].replace(name, "").replace(".", FILE_SEPARATOR);
                String template = path + temp + name;
                arp.addSqlTemplate(template);
            } else if (name.endsWith(CLASS_SUFFIX) && isAddMapping) {
                String temp = file.getPath().replace("\\", ".").replace(FILE_SEPARATOR, ".").split(path.replace(FILE_SEPARATOR, "."))[1].replace(CLASS_SUFFIX, "");
                addReflectMapping(arp, packageName + "." + temp, without);
            }
        }
    }

    /**
     * TODO:MODEL映射
     *
     * @param arp
     * @param clazzName
     */
    private static void addReflectMapping(ActiveRecordPlugin arp, String clazzName, List<String> without) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            if (Model.class.isAssignableFrom(clazz) &&!clazz.getName().equals(HabitModel.class.getName())&& !without.contains(clazz.getName())) {
                Field tableNameFiled = clazz.getDeclaredField("TABLE_NAME");
                Field tablePksFiled = clazz.getDeclaredField("TABLE_PKS");
                String tableName = (String) tableNameFiled.get(clazz);
                String tablePks = (String) tablePksFiled.get(clazz);
                if (StrKit.notBlank(tablePks)) {
                    arp.addMapping(tableName, tablePks, (Class<? extends Model<?>>) clazz);
                }
            }
        } catch (Exception e) {
            System.err.println(clazzName);
            e.printStackTrace();
        }
    }


}
