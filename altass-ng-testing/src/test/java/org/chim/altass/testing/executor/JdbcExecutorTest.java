package org.chim.altass.testing.executor;

import org.apache.ibatis.mapping.SqlCommandType;
import org.chim.altass.core.domain.Job;
import org.chim.altass.core.domain.buildin.entry.Entry;
import org.chim.altass.core.exception.FlowDescException;
import org.chim.altass.core.executor.config.SimpleSequenceConfig;
import org.chim.altass.core.executor.debug.DebugConfig;
import org.chim.altass.core.executor.debug.DebugExecutor;
import org.chim.altass.core.executor.debug.DebugStreamExecutor;
import org.chim.altass.core.executor.toolkit.GenSequenceExecutor;
import org.chim.altass.executor.JdbcExecutor;
import org.chim.altass.executor.jdbc.DataSourceConfig;
import org.chim.altass.executor.jdbc.SqlConfig;
import org.chim.altass.testing.base.AbstractTesting;
import org.junit.Test;

/**
 * Class Name: JDBCExecutorTest
 * Create Date: 11/17/18 4:05 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class JdbcExecutorTest extends AbstractTesting {

    @Test
    public void baseTest() throws InterruptedException {
        execute("baseTest");
    }

    @Test
    public void testInsert() throws InterruptedException {
        execute("testInsert");
    }

    @Override
    public void executorDecorator(String selector, Job job, Entry startNode, Entry endNode) throws FlowDescException {
        if ("baseTest".equalsIgnoreCase(selector)) {
            this.generateBaseTest(job, startNode, endNode);
        } else if ("testInsert".equalsIgnoreCase(selector)) {
            this.generateTestInsert(job, startNode, endNode);
        }
    }

    private void generateTestInsert(Job job, Entry startNode, Entry endNode) {
        Entry debug = new Entry("DebugNode");
        debug.setExecutorClz(DebugExecutor.class);

        // ---------
        SimpleSequenceConfig simpleSequenceConfig = new SimpleSequenceConfig();
        // the start of sequence
        simpleSequenceConfig.setStart(0L);
        // the end of sequence
        simpleSequenceConfig.setEnd(10L);
        simpleSequenceConfig.setTransferExpress("{name = 'val=' + val; age = index}");

        Entry seq = new Entry("SeqNode");
        seq.setExecutorClz(GenSequenceExecutor.class);
        seq.inject("simpleSequenceConfig", simpleSequenceConfig);
        // ---------

        String ipPort = "10.9.96.182:43063";
        String database = "test";
        String username = "develop";
        String password = "mop_kwer_96_vw";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://" + ipPort + "/" + database +
                "?useUnicode=true&characterEncoding=GBK&autoReconnect=true&zeroDateTimeBehavior=convertToNull");
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);

        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.setSqlCommandType(SqlCommandType.INSERT);
        sqlConfig.setSql("insert into test_table (name, age) values (#{name, jdbcType=VARCHAR}, #{age, jdbcType=INTEGER})");
        Entry jdbc = new Entry("JDBCNode");
        jdbc.setExecutorClz(JdbcExecutor.class);
        jdbc.inject("sqlConfig", sqlConfig);
        jdbc.inject("dataSourceConfig", dataSourceConfig);

        Entry debugStream = new Entry("StreamDebug");
        debugStream.setExecutorClz(DebugStreamExecutor.class);
        debugStream.addJsonArg(DebugConfig.class, "{\"delay\":\"0\"}");

        job.link(startNode, debug, seq, jdbc, debugStream, endNode);
    }

    private void generateBaseTest(Job job, Entry startNode, Entry endNode) {
        Entry debug = new Entry("DebugNode");
        debug.setExecutorClz(DebugExecutor.class);

        // ---------
        SimpleSequenceConfig simpleSequenceConfig = new SimpleSequenceConfig();
        // the start of sequence
        simpleSequenceConfig.setStart(0L);
        // the end of sequence
        simpleSequenceConfig.setEnd(1L);

        Entry seq = new Entry("SeqNode");
        seq.setExecutorClz(GenSequenceExecutor.class);
        seq.inject("simpleSequenceConfig", simpleSequenceConfig);
        // ---------

        String ipPort = "10.9.96.182:43063";
        String database = "passport";
        String username = "develop";
        String password = "mop_kwer_96_vw";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://" + ipPort + "/" + database +
                "?useUnicode=true&characterEncoding=GBK&autoReconnect=true&zeroDateTimeBehavior=convertToNull");
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);

        SqlConfig sqlConfig = new SqlConfig();
        sqlConfig.setSql("select * from passport_user_name_9 limit 10");
        Entry jdbc = new Entry("JDBCNode");
        jdbc.setExecutorClz(JdbcExecutor.class);
        jdbc.inject("sqlConfig", sqlConfig);
        jdbc.inject("dataSourceConfig", dataSourceConfig);
        // ---------

        Entry debugStream = new Entry("StreamDebug");
        debugStream.setExecutorClz(DebugStreamExecutor.class);
        debugStream.addJsonArg(DebugConfig.class, "{\"delay\":\"0\"}");

        Entry fileOutPut = newBaseFileOutputEntry("FileOutPut", "/data/altass/tmp/common_output.dat");

        job.link(startNode, debug, seq, jdbc, debugStream, fileOutPut, endNode);
    }
}
