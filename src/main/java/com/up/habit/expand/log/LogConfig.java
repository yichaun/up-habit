package com.up.habit.expand.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.jfinal.kit.PropKit;
import com.up.habit.expand.config.Config;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.lang.reflect.Array;
import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 11:09
 */
@Config(prefix = "log")
public class LogConfig {
    /**
     * 日志存储路径
     */
    private String path;
    /**
     * 日志根等级
     */
    private Level root;
    /**
     * 需要写入到文件的日志
     */
    private String file;

    public String getPath() {
        if (StrKit.isBlank(path)) {
            path = "logback/";
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public void setRoot(String root) {
        if (StrKit.isBlank(root)) {
            root = Level.DEBUG.levelStr;
        }
        root = root.trim().toUpperCase();
        this.root = coverToLevel(root);

    }



    public Level getRoot() {
        if (root==null){
            root=Level.DEBUG;
        }
        return root;
    }

    public List<String> getFile() {
        if (StrKit.isBlank(file)) {
            return new ArrayList<>();
        } else {
            List<String> list = Arrays.asList(ArrayKit.toStrArray(file));
            return list;
        }
    }

    public void setFile(String file) {
        this.file = file;
    }

    /**
     * TODO:是否写入日志文件
     *
     * @param logName
     * @return boolean
     * @Author 王剑洪 on 2020/8/25 0:09
     **/
    public boolean hasFile(String logName) {
        return getFile().contains(logName);
    }

    /**
     * TODO:获取日志等级
     *
     * @param name
     * @return
     */
    public Level getLevel(String name) {
        String key = "log." + name + ".level";
        return getLogLevel(key);
    }


    /**
     * TODO:获取日志等级
     *
     * @param name
     * @return
     */
    public Level getFileLevel(String name) {
        String key = "log." + name + ".file";
        return getLogLevel(key);
    }

    public Level getLogLevel(String key) {
        String value = PropKit.get(key);
        if (StrKit.notBlank(value)) {
            value = value.trim();
            if (StrKit.notBlank(value)) {
                Level level = coverToLevel(value);
                return getRoot().levelInt >= level.levelInt ? getRoot() : level;
            }
        }
        return getRoot();
    }

    public Level coverToLevel(String level) {
        if (Level.OFF.levelStr.equals(level)) {
            return Level.OFF;
        } else if (Level.ERROR.levelStr.equals(level)) {
            return Level.ERROR;
        } else if (Level.WARN.levelStr.equals(level)) {
            return Level.WARN;
        } else if (Level.INFO.levelStr.equals(level)) {
            return Level.INFO;
        } else if (Level.DEBUG.levelStr.equals(level)) {
            return Level.DEBUG;
        } else if (Level.TRACE.levelStr.equals(level)) {
            return Level.TRACE;
        } else {
            return Level.ALL;
        }
    }
}
