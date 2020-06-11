package com.lwx.customizemybatis.service;

import com.lwx.customizemybatis.domain.User;

import java.util.List;

/**
 * @author John Lee
 * @Version 1.0
 * @Description 接口
 * @date 2020/6/11 10:06
 */
public interface UserMapper {

    User selectByPrimaryKey(long userId);
    List<User> selectAll();
}
