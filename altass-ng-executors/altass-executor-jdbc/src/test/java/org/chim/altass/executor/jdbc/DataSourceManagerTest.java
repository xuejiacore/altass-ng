package org.chim.altass.executor.jdbc;

import org.junit.Test;

/**
 * Class Name: DataSourceManagerTest
 * Create Date: 11/14/18 8:26 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class DataSourceManagerTest {

    @Test
    public void testSqlRunner() {
        DataSourceManager sourceManager = new DataSourceManager(new DataSourceConfig(), this.sqlConfig);
        sourceManager.run(new SqlRunner() {

            @Override
            protected String getSql() {
                return "select * from test_table;";
            }

            @Override
            protected Object onDataReturn() {
                return null;
            }
        });
    }

}
