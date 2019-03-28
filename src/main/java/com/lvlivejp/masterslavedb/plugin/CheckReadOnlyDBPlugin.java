package com.lvlivejp.masterslavedb.plugin;

import com.lvlivejp.masterslavedb.config.DataSources;
import com.lvlivejp.masterslavedb.datasource.DataSourceContextHolder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
        , @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class CheckReadOnlyDBPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("*********************intercept:" + invocation);
        if(DataSources.SLAVE_DB.equals(DataSourceContextHolder.getDB())){
            throw new RuntimeException("slavedb is readonly");
        }
        invocation.getTarget();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        System.out.println("*********************plugin:" + target);
        Object wrap = Plugin.wrap(target, this);
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("properties:" + properties);
    }
}
