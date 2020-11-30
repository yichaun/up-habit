package com.up.habit.expand.db.plugin;

import com.alibaba.druid.filter.logging.Log4jFilterMBean;
import com.alibaba.druid.filter.logging.LogFilter;
import com.jfinal.log.Log;

/**
 * TODO:sql语句打印
 * <p>
 *
 * @author 王剑洪 on 2019/11/18 17:43
 */
public class SqlFilter extends LogFilter implements Log4jFilterMBean {
    private Log dataSourceLogger;
    private Log connectionLogger;
    private Log statementLogger;
    private Log resultSetLogger;

    public SqlFilter() {
        this.dataSourceLogger = Log.getLog(this.dataSourceLoggerName);
        this.connectionLogger = Log.getLog(this.connectionLoggerName);
        this.statementLogger = Log.getLog(this.statementLoggerName);
        this.resultSetLogger = Log.getLog(this.resultSetLoggerName);
    }

    @Override
    public String getDataSourceLoggerName() {
        return this.dataSourceLoggerName;
    }

    @Override
    public void setDataSourceLoggerName(String dataSourceLoggerName) {
        this.dataSourceLoggerName = dataSourceLoggerName;
        this.dataSourceLogger = Log.getLog(dataSourceLoggerName);
    }

    public void setDataSourceLogger(Log dataSourceLogger) {
        this.dataSourceLogger = dataSourceLogger;
    }

    @Override
    public String getConnectionLoggerName() {
        return this.connectionLoggerName;
    }

    @Override
    public void setConnectionLoggerName(String connectionLoggerName) {
        this.connectionLoggerName = connectionLoggerName;
        this.connectionLogger = Log.getLog(connectionLoggerName);
    }

    public void setConnectionLogger(Log connectionLogger) {
        this.connectionLogger = connectionLogger;
    }

    @Override
    public String getStatementLoggerName() {
        return this.statementLoggerName;
    }

    @Override
    public void setStatementLoggerName(String statementLoggerName) {
        this.statementLoggerName = statementLoggerName;
        this.statementLogger = Log.getLog(statementLoggerName);
    }

    public void setStatementLogger(Log statementLogger) {
        this.statementLogger = statementLogger;
    }

    @Override
    public String getResultSetLoggerName() {
        return this.resultSetLoggerName;
    }

    @Override
    public void setResultSetLoggerName(String resultSetLoggerName) {
        this.resultSetLoggerName = resultSetLoggerName;
        this.resultSetLogger = Log.getLog(resultSetLoggerName);
    }

    public void setResultSetLogger(Log resultSetLogger) {
        this.resultSetLogger = resultSetLogger;
    }

    @Override
    public boolean isConnectionLogErrorEnabled() {
        return this.connectionLogger.isErrorEnabled() && super.isConnectionLogErrorEnabled();
    }

    @Override
    public boolean isDataSourceLogEnabled() {
        return this.dataSourceLogger.isDebugEnabled() && super.isDataSourceLogEnabled();
    }

    @Override
    public boolean isConnectionLogEnabled() {
        return this.connectionLogger.isDebugEnabled() && super.isConnectionLogEnabled();
    }

    @Override
    public boolean isStatementLogEnabled() {
        return this.statementLogger.isDebugEnabled() && super.isStatementLogEnabled();
    }

    @Override
    public boolean isResultSetLogEnabled() {
        return this.resultSetLogger.isDebugEnabled() && super.isResultSetLogEnabled();
    }

    @Override
    public boolean isResultSetLogErrorEnabled() {
        return this.resultSetLogger.isErrorEnabled() && super.isResultSetLogErrorEnabled();
    }

    @Override
    public boolean isStatementLogErrorEnabled() {
        return this.statementLogger.isErrorEnabled() && super.isStatementLogErrorEnabled();
    }

    @Override
    protected void connectionLog(String message) {
        this.connectionLogger.debug(message);
    }

    @Override
    protected void statementLog(String message) {
        this.statementLogger.debug(format(message));
    }

    @Override
    protected void resultSetLog(String message) {
        this.resultSetLogger.debug(message);
    }

    @Override
    protected void resultSetLogError(String message, Throwable error) {
        this.resultSetLogger.error(message, error);
    }

    @Override
    protected void statementLogError(String message, Throwable error) {
        this.statementLogger.error(format(message), error);
    }


    /**
     * TODO:日志格式化
     *
     * @param message
     * @return java.lang.String
     * @Author 王剑洪 on 2020/8/25 0:10
     **/
    public String format(String message) {
        return message.replaceAll("\n", "")
                .replaceAll("\t", " ")
                .replaceAll(" +", " ");
    }
}
