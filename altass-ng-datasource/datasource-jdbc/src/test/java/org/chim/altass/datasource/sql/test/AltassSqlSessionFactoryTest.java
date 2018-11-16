package org.chim.altass.datasource.sql.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.chim.altass.datasource.sql.AltassSqlSessionFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Class Name: AltassSqlSessionFactory
 * Create Date: 11/16/18 6:51 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class AltassSqlSessionFactoryTest {

    private DruidDataSource dataSource = null;

    @Before
    public void setUp() throws Exception {
        String ipPort = "10.9.96.182:43063";
        String database = "passport";
        String username = "develop";
        String password = "mop_kwer_96_vw";

        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://" + ipPort + "/" + database +
                "?useUnicode=true&characterEncoding=GBK&autoReconnect=true&zeroDateTimeBehavior=convertToNull");

        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    @Test
    public void baseTest() throws SQLException {
        AltassSqlSessionFactory altassSqlSessionFactory = new AltassSqlSessionFactory();

        altassSqlSessionFactory.setDataSource(dataSource);
        SqlSessionFactory sqlSessionFactory = altassSqlSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession(dataSource.getConnection());

        MapperBuilderAssistant assistant = new MapperBuilderAssistant(altassSqlSessionFactory
                .getConfiguration(), "test");
        assistant.addMappedStatement("atest", new StaticSqlSource(altassSqlSessionFactory.getConfiguration(),
                        "select * from passport_user_name_22 limit 100000"),
                StatementType.STATEMENT, SqlCommandType.SELECT, Integer.MIN_VALUE, 200, null, null,
                null, HashMap.class, ResultSetType.FORWARD_ONLY, true, true, true,
                new NoKeyGenerator(), "id", "id", "dd",
                new RawLanguageDriver());

        sqlSession.select("atest", null, new ResultHandler<String>() {
            @Override
            public void handleResult(ResultContext resultContext) {
                Object resultObject = resultContext.getResultObject();
                System.err.println(resultObject + "\t" + resultContext.getResultCount());
            }
        });
    }
}
