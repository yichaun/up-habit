package com.up.habit.expand.log;

import ch.qos.logback.classic.Logger;
import com.jfinal.log.Log;

import static org.slf4j.spi.LocationAwareLogger.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 11:08
 */
public class Logback extends Log {

    private Logger log;
    private static final String FQCN = Logback.class.getName();

    public Logback(Object object) {
        log = new LogbackBuilder().getLogger(object);
    }

    @Override
    public void debug(String message) {
        log.log(null, FQCN, DEBUG_INT, message, null, null);
    }

    @Override
    public void debug(String message, Throwable t) {
        log.log(null, FQCN, DEBUG_INT, message, null, t);
    }

    @Override
    public void info(String message) {
        log.log(null, FQCN, INFO_INT, message, null, null);
    }

    @Override
    public void info(String message, Throwable t) {
        log.log(null, FQCN, INFO_INT, message, null, t);
    }

    @Override
    public void warn(String message) {
        log.log(null, FQCN, WARN_INT, message, null, null);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.log(null, FQCN, WARN_INT, message, null, t);
    }

    @Override
    public void error(String message) {
        log.log(null, FQCN, ERROR_INT, message, null, null);
    }

    @Override
    public void error(String message, Throwable t) {
        log.log(null, FQCN, ERROR_INT, message, null, t);
    }

    @Override
    public void fatal(String message) {
        log.log(null, FQCN, ERROR_INT, message, null, null);
    }

    @Override
    public void fatal(String message, Throwable t) {
        log.log(null, FQCN, ERROR_INT, message, null, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }
}
