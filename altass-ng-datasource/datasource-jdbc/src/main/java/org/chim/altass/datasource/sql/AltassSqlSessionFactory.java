package org.chim.altass.datasource.sql;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * Class Name: AltassSqlSessionFactory
 * Create Date: 11/15/18 12:13 AM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class AltassSqlSessionFactory {

    private Interceptor[] plugins = null;

    private DataSource dataSource = null;

    private Cache cache = null;

    private TransactionFactory transactionFactory = null;

    private String environment = AltassSqlSessionFactory.class.getSimpleName();

    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    private SqlSessionFactory sqlSessionFactory = null;


    public AltassSqlSessionFactory() {

    }

    public AltassSqlSessionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Sets the {@code SqlSessionFactoryBuilder} to use when creating the {@code SqlSessionFactory}.
     * <p>
     * This is mainly meant for testing so that mock SqlSessionFactory classes can be injected. By
     * default, {@code SqlSessionFactoryBuilder} creates {@code DefaultSqlSessionFactory} instances.
     */
    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    /**
     * Mybatis plugin list.
     *
     * @param plugins list of plugins
     * @since 1.0.1
     */
    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void build() throws SQLException {
        this.sqlSessionFactory = init();
    }

    public SqlSessionFactory getSqlSessionFactory() throws SQLException {
        if (this.sqlSessionFactory == null) {
            build();
        }
        return sqlSessionFactory;
    }

    private Configuration configuration = null;

    private SqlSessionFactory init() throws SQLException {
        configuration = new Configuration();

        if (this.plugins != null) {
            for (Interceptor plugin : this.plugins) {
                configuration.addInterceptor(plugin);
            }
        }

        if (this.cache != null) {
            configuration.addCache(this.cache);
        }

        if (transactionFactory != null) {
            configuration.setEnvironment(new Environment(this.environment, transactionFactory, this.dataSource));
        }


        return this.sqlSessionFactoryBuilder.build(configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addStatement(String id, String sql, SqlCommandType cmdType, String resource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(this.configuration, id,
                new StaticSqlSource(this.configuration, sql), cmdType);
        MappedStatement mappedStatement = builder.build();
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(this.configuration, resource);
        assistant.addMappedStatement(
                mappedStatement.getId(),
                mappedStatement.getSqlSource(),
                mappedStatement.getStatementType(),
                mappedStatement.getSqlCommandType(),
                mappedStatement.getFetchSize(),
                mappedStatement.getTimeout(),
                null,
                HashMap.class,
                null,
                HashMap.class,
                mappedStatement.getResultSetType(),
                false,
                true,
                true,
                null,
                null,
                null,
                null,
                new RawLanguageDriver());
    }

}
