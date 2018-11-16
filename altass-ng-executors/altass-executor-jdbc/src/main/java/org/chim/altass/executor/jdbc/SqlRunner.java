package org.chim.altass.executor.jdbc;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.StringReader;
import java.sql.Connection;

/**
 * Class Name: SqlRunner
 * Create Date: 11/14/18 1:11 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public abstract class SqlRunner {

    public void onSqlRunner(Connection connection) {

        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.runScript(new StringReader(this.getSql()));
    }

    protected abstract String getSql();

    protected abstract Object onDataReturn();
}
