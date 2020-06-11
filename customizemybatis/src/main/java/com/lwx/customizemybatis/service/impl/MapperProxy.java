package com.lwx.customizemybatis.service.impl;

import com.lwx.customizemybatis.config.Configuration;
import com.lwx.customizemybatis.service.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Lee
 * @Version 1.0
 * @Description 将请求转发给 sqlSession
 * @date 2020/6/11 10:36
 */
public class MapperProxy<T> implements InvocationHandler {

    private SqlSession sqlSession;
    private Class<T> mapperInterface;
    private Configuration dataSourceConfiguration;
    private Map<Method, MapperMethod> methodCache = new HashMap<>();

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Configuration dataSourceConfiguration) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.dataSourceConfiguration = dataSourceConfiguration;
    }

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  {
        //最终还是将执行方法转给 sqlSession，因为 sqlSession 里面封装了 Executor
        //根据调用方法的类名和方法名以及参数，传给 sqlSession 对应的方法
        if(Collection.class.isAssignableFrom(method.getReturnType())){
            return sqlSession.selectList(method.getDeclaringClass().getName()+"."+method.getName(),args==null?null:args[0]);
        }else{
            return sqlSession.selectOne(method.getDeclaringClass().getName()+"."+method.getName(),args==null?null:args[0]);
        }
    }
}
