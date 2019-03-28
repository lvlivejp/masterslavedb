package com.lvlivejp.masterslavedb.config;

import com.lvlivejp.masterslavedb.datasource.DynamicDataSource;
import com.lvlivejp.masterslavedb.plugin.CheckReadOnlyDBPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan("com.lvlivejp.masterslavedb.dao")
public class MybatisConfig {

    @Autowired
    @Qualifier(DataSources.MASTER_DB)
    private DataSource dataSource;

    @Autowired
    @Qualifier(DataSources.SLAVE_DB)
    private DataSource dataSourceSlave;

    @Bean
    public DataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(dataSource);

        Map<Object,Object> dsMap =new HashMap();

        dsMap.put(DataSources.MASTER_DB,dataSource);
        dsMap.put(DataSources.SLAVE_DB,dataSourceSlave);
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

    @Bean
    @ConfigurationProperties("mybatis")
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{interceptor()});
        return sqlSessionFactoryBean;
    }

    /**
     * 必须手动配置事务管理器，并将DataSource配置为动态数据源，不能指定主数据源，否则事务不生效
     * 系统自动生成的事务管理器，它的数据源是主数据源，事务无法生效。
     * 因为执行SQL语句的是动态数据源，所以事务管理器的数据源必须和执行的数据源一致，都是动态数据源，否则事务不生效。
     * @return
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager= new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dynamicDataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    public Interceptor interceptor() {
        return new CheckReadOnlyDBPlugin();
    }
}
