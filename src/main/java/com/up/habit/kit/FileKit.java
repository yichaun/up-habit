package com.up.habit.kit;

import com.jfinal.kit.LogKit;
import com.up.habit.Habit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/11/14 11:29
 */
public class FileKit extends com.jfinal.kit.FileKit {
    /**
     * 查找对应文件夹下所有指定后缀的文件
     *
     * @param baseDirName 查找的文件夹路径
     */
    public static List<String> find(String baseDirName, String... suffixs) {
        List<String> configFiles = new ArrayList<String>();
        // 判断目录是否存在
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new RuntimeException("directory does not exist：" + baseDirName);
        } else {
            String[] fileList = baseDir.list();
            boolean isFilter = ArrayKit.isNotEmpty(suffixs);
            for (String fileName : fileList) {
                if (isFilter) {
                    for (String suffix : suffixs) {
                        if (fileName.endsWith(suffix)) {
                            configFiles.add(fileName);
                            break;
                        }
                    }
                } else {
                    configFiles.add(fileName);
                }
            }
        }
        return configFiles;
    }

    /**
     * 判断文件是否是图片
     *
     * @param file
     * @return
     */
    public static boolean isImage(File file) {
        try {
            Image image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 字符串转文件存储
     *
     * @param content
     * @param path
     * @param fileName
     */
    public static void str2File(String content, String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String target = path + File.separator + fileName;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(target), Habit.CHART_UTF8);
            osw.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    LogKit.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * TODO:根据自定义内容生成文件
     *
     * @param data           自定义内容
     * @param outputFilePath 输出目录
     * @param override       重新生成是否覆盖
     */
    public static void renderByString(String data, String outputFilePath, boolean override) {
        BufferedWriter output = null;
        try {
            File file = new File(outputFilePath);
            if (file.exists() && !override) {
                return;    // 若存在，不覆盖
            }
            File path = new File(file.getParent());
            if (!path.exists()) {
                path.mkdirs();
            }
            output = new BufferedWriter(new FileWriter(file));
            output.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("renderByString error!\toutputFilePath:" + outputFilePath);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
