package com.lwx.customizemybatis.service.impl;


import com.lwx.customizemybatis.config.Configuration;
import com.lwx.customizemybatis.domain.configbean.MappedStatement;
import com.lwx.customizemybatis.executor.Executor;
import com.lwx.customizemybatis.executor.impl.SimpleExecutor;
import com.lwx.customizemybatis.service.SqlSession;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author John Lee
 * @Version 1.0
 * @Description
 * @date 2020/6/11 10:28
 */
public class DefaultSqlSession implements SqlSession {


    private Configuration configuration;

    private boolean autoCommit;

    private Executor executor;

    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        executor = new SimpleExecutor(configuration);
    }

    public DefaultSqlSession(Configuration configuration, boolean autoCommit) {
        super();
        this.configuration = configuration;
        this.autoCommit = autoCommit;
        executor = new SimpleExecutor(configuration);
    }


    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = this.selectList(statement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatements().get(statement);
        // 我们的查询方法最终还是交给了 Executor 去执行，Executor 里面封装了 JDBC 操作。传入参数包含了 sql 语句和 sql 语句需要的参数。
        return executor.query(ms, parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        //通过动态代理生成了一个实现类，我们重点关注，动态代理的实现，它是一个 InvocationHandler，传入参数是 this，就是 sqlSession 的一个实例。
        MapperProxy mapperProxy = new MapperProxy(this, type, configuration);
        //给我一个接口，还你一个实现类
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type},
                mapperProxy);

    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return configuration.getMapperRegistry().getMapper(type, sqlSession);
    }
}
