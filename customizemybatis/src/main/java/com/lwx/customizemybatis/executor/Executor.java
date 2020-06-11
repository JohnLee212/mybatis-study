package com.lwx.customizemybatis.executor;

import com.lwx.customizemybatis.domain.configbean.MappedStatement;

import java.util.List;

/**
 * @author John Lee
 * @Version 1.0
 * @Description mybatis 核心接口之一，定义了数据库操作的最基本的方法，JDBC，sqlSession的所有功能都是基于它来实现的
 * @date 2020/6/11 10:29
 */
public interface Executor {

    /**
     *
     * 查询接口
     * @param ms 封装sql 语句的 mappedStatemnet 对象，里面包含了 sql 语句，resultType 等。
     * @param parameter 传入sql 参数
     * @param <E> 将数据对象转换成指定对象结果集返回
     * @return
     */
    <E> List<E> query(MappedStatement ms, Object parameter);
}
