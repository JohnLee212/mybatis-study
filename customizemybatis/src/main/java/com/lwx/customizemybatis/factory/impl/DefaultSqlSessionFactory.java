package com.lwx.customizemybatis.factory.impl;

import com.lwx.customizemybatis.config.Configuration;
import com.lwx.customizemybatis.domain.configbean.MappedStatement;
import com.lwx.customizemybatis.factory.SqlSessionFactory;
import com.lwx.customizemybatis.service.SqlSession;
import com.lwx.customizemybatis.service.impl.DefaultSqlSession;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author John Lee
 * @Version 1.0
 * @Description 默认Session工厂实现
 * @date 2020/6/11 10:16
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration = new Configuration();

    // xml 文件存放的位置
    private static final String MAPPER_CONFIG_LOCATION = "mapper";

    /**
     * 数据库信息存放的位置
     */
    private static final String DB_CONFIG_FILE = "db.properties";

    public DefaultSqlSessionFactory() {
        loadDBInfo();
        loadMapperInfo();
    }

    private void loadDBInfo() {
        InputStream db = this.getClass().getClassLoader().getResourceAsStream(DB_CONFIG_FILE);
        Properties p = new Properties();
        try {
            p.load(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将配置信息写入Configuration 对象
        configuration.setJdbcDriver(p.get("jdbc.driver").toString());
        configuration.setJdbcUrl(p.get("jdbc.url").toString());
        configuration.setJdbcUsername(p.get("jdbc.username").toString());
        configuration.setJdbcPassword(p.get("jdbc.password").toString());
    }

    private void loadMapperInfo() {
        URL resources = null;
        resources = this.getClass().getClassLoader().getResource(MAPPER_CONFIG_LOCATION);

        File mappers = new File(resources.getFile());
        //读取文件夹下面的文件信息
        if (mappers.isDirectory()) {
            File[] files = mappers.listFiles();
            for (File file : files) {
                loadMapperInfo(file);
            }
        }
    }

    private void loadMapperInfo(File file) {
        SAXReader reader = new SAXReader();
        //通过read方法读取一个文件转换成Document 对象
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获取根结点元素对象<mapper>
        Element e = document.getRootElement();
        //获取命名空间namespace
        String namespace = e.attribute("namespace").getData().toString();
        //获取select,insert,update,delete子节点列表
        List<Element> selects = e.elements("select");
        List<Element> inserts = e.elements("insert");
        List<Element> updates = e.elements("update");
        List<Element> deletes = e.elements("delete");

        List<Element> all = new ArrayList<>();
        all.addAll(selects);
        all.addAll(inserts);
        all.addAll(updates);
        all.addAll(deletes);

        //遍历节点，组装成 MappedStatement 然后放入到configuration 对象中
        for (Element ele : all) {
            MappedStatement mappedStatement = new MappedStatement();
            String id = ele.attribute("id").getData().toString();
            String resultType = ele.attribute("resultType").getData().toString();
            String sql = ele.getData().toString();

            mappedStatement.setId(namespace + "." + id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setNamespace(namespace);
            mappedStatement.setSql(sql);
            // xml 文件中的每个 sql 方法都组装成 mappedStatement 对象，以 namespace+"."+id 为 key， 放入
            // configuration 配置类中。
            configuration.getMappedStatements().put(namespace + "." + id, mappedStatement);
        }
    }

    @Override
    public SqlSession openSession() {
        // openSession 方法创建一个 DefaultSqlSession，configuration 配置类作为 构造函数参数传入
        return new DefaultSqlSession(configuration);
    }
}
