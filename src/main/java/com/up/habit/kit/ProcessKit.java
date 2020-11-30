package com.up.habit.kit;

import java.io.File;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/11/21 17:38
 */
public class ProcessKit {
    public static void backup(String host, String dbName, String user, String password, String path) throws Exception {
        File file = new File(path);
        //创建备份sql文件
        if (!file.exists()) {
            file.createNewFile();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("mysqldump");
        sb.append(" -h").append(host);
        sb.append(" -u").append(user);
        sb.append(" -p").append(password);
        sb.append(" " + dbName + " >").append(dbName);
        sb.append(" >").append(path);
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec( sb.toString());
    }

    public static void main(String[] args)  {
        try {
            backup("192.168.8.11", "smlitev2", "root", "nihao123456!", "C:/Users/46973/Documents/xyt/test.sql");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
