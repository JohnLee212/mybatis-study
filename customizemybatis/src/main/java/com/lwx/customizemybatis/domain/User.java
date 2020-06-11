package com.lwx.customizemybatis.domain;

import lombok.Data;

/**
 * @author John Lee
 * @Version 1.0
 * @Description 用户实体类
 * @date 2020/6/11 10:10
 */
@Data
public class User {

    private long userId;
    private String userName;
    private int sex;
    private String role;
}
