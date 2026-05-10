package com.lss.longchain4jdemo.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class ToolsService {
//    ToolsService配置为了一个bean
//    ● @Tool  用于告诉AI什么对话调用这个方法
//    ● @P("姓名")  用于告诉AI ，调用方法的时候需要提取对话中的什么信息， 这里提取的是姓名

    @Tool
    private Integer shanghaiNameCount(@P("姓名") String name) {
        System.out.println("调用了shanghaiNameCount工具");
        System.out.println(name);
        return 100;
    }


    @Tool("上海的天气")
    public String shanghaiWeather() {
        System.out.println("调用了shanghaiWeather");
        return "下雪";
    }

}
