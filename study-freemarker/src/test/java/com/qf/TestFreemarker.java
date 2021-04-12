package com.qf;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>title: com.qf</p>
 * author zhuximing
 * description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFreemarker {

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;


    @Test
    public void test1 () throws Exception{
        Configuration configuration = freeMarkerConfig.getConfiguration();


        //4.获取模板对象
        Template template = configuration.getTemplate("demo.ftl");
        //5.创建模型数据

        Student student = new Student("jack",12);
        Student student1 = new Student("rose",18);
        Student student2 = new Student("james",19);
        List stus = new ArrayList(){{
            add(student);
            add(student1);
            add(student2);
        }};

        Map data = new HashMap(){{
            put("username","张三丰");
            put("age",19);
            put("stus",stus);
            put("num",1000000);
        }};
        //6.创建输出流(Writer)对象
        Writer out = new FileWriter("e:\\demo.html");
        //7.输出
        template.process(data,out);
        //8.关闭
        out.close();
    }


}