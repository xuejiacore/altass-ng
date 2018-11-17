package org.chim.altass.executor.jdbc;

import org.apache.ibatis.mapping.SqlCommandType;

import java.util.HashMap;

/**
 * Class Name: SqlConfig
 * Create Date: 11/16/18 7:17 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 执行sql所需的各种参数，包含执行的类型，是存储过程还是普通sql、是否需要返回insert主键信息等
 */
public class SqlConfig {
    private String sql = null;

    private Class parameterType = HashMap.class;

    private Class resultType = HashMap.class;

    private SqlCommandType sqlCommandType = SqlCommandType.SELECT;

    private Integer timeout = null;

    private String keyProperty = null;
    private String keyColumn = null;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class parameterType) {
        this.parameterType = parameterType;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Class getResultType() {
        return resultType;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }

    public String getKeyProperty() {
        return keyProperty;
    }

    public void setKeyProperty(String keyProperty) {
        this.keyProperty = keyProperty;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }
}
