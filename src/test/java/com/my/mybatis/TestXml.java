package com.my.mybatis;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.my.mybatis.scripting.*;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestXml {

    @SneakyThrows
    @Test
    public void testXml() {
        // 解析xml文件
        SAXReader saxReader = new SAXReader();

        saxReader.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
            }
        });

        // 当前项目目录，而不是classpath目录
        BufferedInputStream inputStream = FileUtil.getInputStream("UserMapper.xml");
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//select");
        for (Element e : list) {
            String methodName = e.attributeValue("id");
            String resultType = e.attributeValue("resultType");
            System.out.println(methodName + " " + resultType);
            MixedSqlNode mixedSqlNode = parseTags(e);
            DynamicContext dynamicContext = new DynamicContext(
                    new HashMap<String, Object>() {{
                        this.put("id", 1);
                        this.put("xx", 2);
            }}
            );
            mixedSqlNode.apply(dynamicContext);
            dynamicContext.getSql();
            System.out.println(mixedSqlNode);
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
                contents.add(new StaticTextSqlNode(sql));
            }
        }


        return new MixedSqlNode(contents);
    }

}
