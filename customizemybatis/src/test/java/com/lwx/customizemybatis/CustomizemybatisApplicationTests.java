package com.lwx.customizemybatis;

import com.lwx.customizemybatis.domain.User;
import com.lwx.customizemybatis.service.SqlSession;
import com.lwx.customizemybatis.factory.SqlSessionFactory;
import com.lwx.customizemybatis.service.UserMapper;
import com.lwx.customizemybatis.factory.impl.DefaultSqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CustomizemybatisApplicationTests {

    @Test
    void contextLoads() {
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectByPrimaryKey(1);
        List<User> users = mapper.selectAll();
        System.out.println(user.toString());
        System.out.println(users.size());
    }

}
