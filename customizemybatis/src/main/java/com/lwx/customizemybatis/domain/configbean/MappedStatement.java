package com.lwx.customizemybatis.domain.configbean;

import lombok.Data;
import org.apache.catalina.mapper.Mapper;

import java.util.List;

/**
 * @author John Lee
 * @Version 1.0
 * @Description
 * @date 2020/6/11 19:02
 */
@Data
public class MappedStatement {


    /**
     * 命名空间-即接口全路径
     */
    private String namespace;

    private String id;

    private String resultType;

    private String sql;

}
