package com.my.mybatis.builder;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import com.my.mybatis.annotation.*;
import com.my.mybatis.cache.PerpetualCache;
import com.my.mybatis.mapping.MappedStatement;
import com.my.mybatis.mapping.SqlCommandType;
import com.my.mybatis.scripting.*;
import com.my.mybatis.session.Configuration;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * XML配置构建器
 */
public class XMLConfigBuilder {

    private Set<Class> sqlAnnotationSet = new HashSet<>();

    {
        sqlAnnotationSet.add(Select.class);
        sqlAnnotationSet.add(Update.class);
        sqlAnnotationSet.add(Delete.class);
        sqlAnnotationSet.add(Insert.class);
    }

    public Configuration parse() {
        // 解析mapper
        Configuration configuration = new Configuration();
        parseMapper(configuration);
        parseMapperXml(configuration);
        return configuration;
    }

    @SneakyThrows
    private void parseMapper(Configuration configuration) {

        Set<Class<?>> classes = ClassUtil.scanPackage("com.my.demo.mapper");
        for (Class<?> aClass : classes) {
            CacheNamespace cacheNamespace = aClass.getAnnotation(CacheNamespace.class);
            boolean isCache = cacheNamespace != null;
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                boolean isExistAnnotation = false;
                String originalSql = null;
                SqlCommandType sqlCommandType = null;
                for (Class<? extends Annotation> annotationClass : sqlAnnotationSet) {
                    if (method.isAnnotationPresent(annotationClass)) {
                        if (annotationClass == Select.class) {
                            originalSql = method.getAnnotation(Select.class).value();
                            sqlCommandType = SqlCommandType.SELECT;
                        } else if (annotationClass == Update.class) {
                            originalSql = method.getAnnotation(Update.class).value();
                            sqlCommandType = SqlCommandType.UPDATE;
                        } else if (annotationClass == Delete.class) {
                            originalSql = method.getAnnotation(Delete.class).value();
                            sqlCommandType = SqlCommandType.DELETE;
                        } else if (annotationClass == Insert.class) {
                            originalSql = method.getAnnotation(Insert.class).value();
                            sqlCommandType = SqlCommandType.INSERT;
                        }
                        isExistAnnotation = true;
                        break;
                    }
                }

                if (! isExistAnnotation) {
                    continue;
                }

                Class returnType = null;
                boolean isSelectMany = false;
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    returnType =  (Class) ((ParameterizedType) genericReturnType).getActualTypeArguments()[0];
                    isSelectMany = true;
                } else if (genericReturnType instanceof Class) {
                    returnType = (Class) genericReturnType;
                }

                MappedStatement mappedStatement = MappedStatement.builder()
                        .id(aClass.getName() + "." + method.getName())
                        .sql(originalSql)
                        .returnType(returnType)
                        .sqlCommandType(sqlCommandType)
                        .isSelectMany(isSelectMany)
                        .cache(isCache ? new PerpetualCache(aClass.getName()) : null)
                        .build();
                configuration.addMappedStatement(mappedStatement);
            }
        }
    }

    @SneakyThrows
    public void parseMapperXml(Configuration configuration) {
        // 解析xml文件
        SAXReader saxReader = new SAXReader();

        saxReader.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
            }
        });

        // 当前项目目录，而不是classpath目录
        BufferedInputStream inputStream = FileUtil.getInputStream(System.getProperty("user.dir") + "/src/main/resources/mapper/UserMapper.xml");
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> list = rootElement.selectNodes("//select");
        for (Element e : list) {
            String methodName = e.attributeValue("id");
            String resultType = e.attributeValue("resultType");
            System.out.println(methodName + " " + resultType);
            MixedSqlNode mixedSqlNode = parseTags(e);

            Class<?> resultTypeClass = Class.forName(resultType);
            // 封装
            MappedStatement mappedStatement = MappedStatement.builder()
                    .id(namespace + "." + methodName)
                    .sql("")
                    .sqlSource(mixedSqlNode)
                    .returnType(resultTypeClass)
                    .sqlCommandType(SqlCommandType.SELECT)
                    .isSelectMany(false)
                    .cache(new PerpetualCache(resultTypeClass.getName()))
                    .build();
            configuration.addMappedStatement(mappedStatement);
        }

        System.out.println(rootElement.getName());
    }

    private MixedSqlNode parseTags(Element e) {
        List<SqlNode> contents = new ArrayList<>();

        List<Node> contentList = e.content();
        for (Node node : contentList) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childNodeElement = (Element) node;
                String sqlNodeType = childNodeElement.getName();
                String test = childNodeElement.attributeValue("test");
                System.out.println("类型" + sqlNodeType);
                System.out.println("表达式" + test);

                if (sqlNodeType.equals("if")) {
                    contents.add(new IfSqlNode(test, parseTags(childNodeElement)));
                } else if (sqlNodeType.equals("choose")) {
//                contents.add(new ChooseSqlNode(test, parseTags(childNodeElement)));
                }
            } else {
                String sql = node.getText();
                if (sql.contains("${")) {
                    contents.add(new TextSqlNode(sql));
                } else {
                    contents.add(new StaticTextSqlNode(sql));
                }
            }
        }


        return new MixedSqlNode(contents);
    }
}
