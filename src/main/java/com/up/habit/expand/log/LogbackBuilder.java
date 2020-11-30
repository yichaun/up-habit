package com.up.habit.expand.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import com.jfinal.core.ActionReporter;
import com.jfinal.ext.kit.DateKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.up.habit.Habit;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 11:09
 */
public class LogbackBuilder {
    private static final Map<Object, Logger> container = new HashMap<>();
    private Logger logger;
    static LogConfig config;
    static Logger rootLog;
    static String LOG_ACTION = "action";
    static String LOG_SQL = "sql";

    static {
        config = Habit.config(LogConfig.class);
        rootLog = (Logger) LoggerFactory.getILoggerFactory().getLogger(Logger.ROOT_LOGGER_NAME);
        rootLog.setLevel(config.getRoot());
    }


    public Logger getLogger(Object object) {
        synchronized (LogbackBuilder.class) {
            logger = container.get(object);
            if (logger == null) {
                logger = build(object);
            }
            container.put(object, logger);
        }
        return logger;
    }

    private Logger build(Object object) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = null;
        String name = "";
        if (object instanceof String) {
            name = object + "";
            logger = context.getLogger(name);
        } else {
            Class clz = (Class) object;
            name = clz.getName();
            logger = context.getLogger(clz);

        }
        logger.addAppender(consoleAppender(name, context, config.getLevel(name)));
        if (config.hasFile(name)) {
            logger.addAppender(fileAppender(name, context, config.getFileLevel(name)));
        }
        logger.setAdditive(false);
        return logger;
    }

    /**
     * TODO:控制台输出设置
     *
     * @param name
     * @param context
     * @param level
     * @return
     */
    public ConsoleAppender consoleAppender(String name, LoggerContext context, Level level) {
        ConsoleAppender console = new ConsoleAppender();
        console.setContext(context);
        console.setName("CONSOLE");
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        if (LOG_ACTION.equals(name)) {
            encoder.setPattern("%msg");
        } else {
            if (name.contains(LOG_SQL)){
                /**sql日志格式处理*/
                encoder.setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} %boldGreen(%msg%n)");
            } else {
                encoder.setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} %boldYellow([%thread]) %boldGreen(%logger{40}) %highlight(%-5level) (%file:%line\\) %n%msg%n");
            }
        }

        encoder.start();
        console.setEncoder(encoder);
        ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(level.levelStr);
        console.addFilter(filter);
        console.start();
        return console;
    }

    /**
     * TODO:文件输出设置
     *
     * @param name
     * @param context
     * @param level
     * @return
     */
    public RollingFileAppender fileAppender(String name, LoggerContext context, Level level) {
        String date = DateKit.toStr(new Date());
        RollingFileAppender appender = new RollingFileAppender();
        appender.setName(name);
        appender.setContext(context);
        //设置级别过滤器
        ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(level.levelStr);
        appender.addFilter(filter);
        //设置文件名
        String fileName = config.getPath() + date + "/" + name + "." + level.levelStr.toLowerCase() + ".log";
        appender.setFile(OptionHelper.substVars(fileName, context));
        appender.setAppend(true);
        appender.setPrudent(false);
        //设置文件创建时间及大小的类
        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
        policy.setContext(context);
        //文件名格式
        String fp = OptionHelper.substVars(config.getPath() + "%d{yyyy-MM-dd}/" + name + "." + level.levelStr.toLowerCase() + ".%i.log", context);
        //最大日志文件大小
        policy.setMaxFileSize(FileSize.valueOf("1024KB"));
        //设置文件名模式
        policy.setFileNamePattern(fp);
        //设置最大历史记录为15条
        policy.setMaxHistory(15);
        //总大小限制
        policy.setTotalSizeCap(FileSize.valueOf("32GB"));
        //设置父节点是appender
        policy.setParent(appender);
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        policy.start();
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        //%d %p (%file:%line\\)- %m%n
        if (LOG_ACTION.equals(name)) {
            encoder.setPattern("%msg%n");
        } else {
            encoder.setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} %logger{40} (%file:%line\\) %n%msg%n");
        }
        encoder.start();
        //加入下面两个节点
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        return appender;
    }

    /**
     * TODO:接口访问日志记录
     */
    public static void actionReporter() {
        Log log = Log.getLog(LOG_ACTION);
        ActionReporter.setWriter(new Writer() {
            @Override
            public void write(String str) throws IOException {
                log.info(str);
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {

            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        });
    }
}
