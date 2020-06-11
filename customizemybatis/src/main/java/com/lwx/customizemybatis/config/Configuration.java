package com.lwx.customizemybatis.config;

import com.lwx.customizemybatis.domain.configbean.MappedStatement;
import com.lwx.customizemybatis.service.impl.MapperRegistry;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author John Lee
 * @Version 1.0
 * @Description 配置类
 * @date 2020/6/11 10:14
 */
@Data
public class Configuration {

    private String jdbcDriver;

    private String jdbcUrl;

    private String jdbcPassword;

    private String jdbcUsername;

    protected final Map<String, MappedStatement> mappedStatements = new ConcurrentHashMap<>();

    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);





}
