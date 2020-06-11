package com.lwx.customizemybatis.factory;


import com.lwx.customizemybatis.service.SqlSession;

/**
 * @author John Lee
 * @Version 1.0
 * @Description Session工厂类
 * @date 2020/6/11 10:15
 */
public interface SqlSessionFactory {
    SqlSession openSession();
}
