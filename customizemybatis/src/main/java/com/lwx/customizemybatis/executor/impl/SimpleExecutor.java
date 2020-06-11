package com.lwx.customizemybatis.executor.impl;


import com.lwx.customizemybatis.config.Configuration;
import com.lwx.customizemybatis.domain.configbean.MappedStatement;
import com.lwx.customizemybatis.executor.Executor;
import com.lwx.customizemybatis.util.ReflectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John Lee
 * @Version 1.0
 * @Description
 * @date 2020/6/11 10:31
 */
public class SimpleExecutor implements Executor {


    private final Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) {
        System.out.println(ms.getSql().toString());
        //返回结果集
        List<E> ret = new ArrayList<>();
        try {
            Class.forName(configuration.getJdbcDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(configuration.getJdbcUrl(), configuration.getJdbcUsername(), configuration.getJdbcPassword());
            String regex = "#\\{([^}])*\\}";
            // 将 sql 语句中的 #{userId} 替换为 ？
            String  sql = ms.getSql().replaceAll(regex,"?");
            preparedStatement = connection.prepareStatement(sql);
            //处理占位符，把占位符用传入的参数替换
            parameterSize(preparedStatement, parameter);
            resultSet = preparedStatement.executeQuery();
            handlerResultSet(resultSet, ret,ms.getResultType());
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                resultSet.close();
                preparedStatement.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return ret;
    }

    private <E> void handlerResultSet(ResultSet resultSet, List<E> ret, String className) {

        Class<E> clazz = null;
        //通过反射获取类对象
        try {
            clazz = (Class<E>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while (resultSet.next()) {
                Object entity = clazz.newInstance();
                //通过反射工具 将 resultset 中的数据填充到 entity 中
                ReflectionUtil.setPropToBeanFromResultSet(entity, resultSet);
                ret.add((E) entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parameterSize(PreparedStatement preparedStatement, Object parameter) throws SQLException {
        if(parameter instanceof Integer){
            preparedStatement.setInt(1,(int)parameter);
        }else if(parameter instanceof  Long){
            preparedStatement.setLong(1,(Long)parameter);
        }else if(parameter instanceof  String){
            preparedStatement.setString(1,(String)parameter);
        }
    }
}
