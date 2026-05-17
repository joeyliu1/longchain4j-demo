package com.lss.longchain4jdemo;

import com.lss.longchain4jdemo.config.AiConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 航空客服系统测试
 * 测试机票查询、改签、取消等功能
 */
@SpringBootTest
public class AirlineServiceTest {

    @Autowired
    AiConfig.AirlineAssistant airlineAssistant;

    @Test
    public void testGreeting() {
        // 测试问候
        String response = airlineAssistant.chat("你好，我要查询机票");
        System.out.println("=== 问候测试 ===");
        System.out.println(response);
        System.out.println();
    }

    @Test
    public void testQueryOrder() {
        // 测试查询订单（信息完整）
        String response = airlineAssistant.chat("我要查询机票，订单号是 CN123456，乘客姓名是刘德华");
        System.out.println("=== 查询订单测试（完整信息）===");
        System.out.println(response);
        System.out.println();
    }

    @Test
    public void testQueryOrderMissingInfo() {
        // 测试查询订单（缺少信息，AI 应该追问）
        String response = airlineAssistant.chat("我要查询机票");
        System.out.println("=== 查询订单测试（缺少信息）===");
        System.out.println(response);
        System.out.println();
    }

    @Test
    public void testChangeOrder() {
        // 测试改签订单
        String response = airlineAssistant.chat(
                "我要改签机票，订单号是 CN123456，乘客姓名是刘德华，" +
                "改签到航班 CA1503，起飞时间是 2026-05-21 14:30");
        System.out.println("=== 改签订单测试 ===");
        System.out.println(response);
        System.out.println();
    }

    @Test
    public void testCancelOrder() {
        // 测试取消订单
        String response = airlineAssistant.chat(
                "我要取消机票，订单号是 CN789012，乘客姓名是张学友");
        System.out.println("=== 取消订单测试 ===");
        System.out.println(response);
        System.out.println();
    }

    @Test
    public void testMultiTurnConversation() {
        // 测试多轮对话
        System.out.println("=== 多轮对话测试 ===");

        // 第一轮：问候
        String response1 = airlineAssistant.chat("你好");
        System.out.println("用户：你好");
        System.out.println("AI: " + response1);
        System.out.println();

        // 第二轮：查询订单
        String response2 = airlineAssistant.chat("我要查询机票，订单号 CN123456，姓名刘德华");
        System.out.println("用户：我要查询机票，订单号 CN123456，姓名刘德华");
        System.out.println("AI: " + response2);
        System.out.println();

        // 第三轮：改签
        String response3 = airlineAssistant.chat("帮我改签到 CA1503 航班");
        System.out.println("用户：帮我改签到 CA1503 航班");
        System.out.println("AI: " + response3);
        System.out.println();
    }

    @Test
    public void testWrongPassengerName() {
        // 测试姓名不匹配
        String response = airlineAssistant.chat("我要查询机票，订单号是 CN123456，乘客姓名是张三");
        System.out.println("=== 姓名不匹配测试 ===");
        System.out.println(response);
        System.out.println();
    }

    @Test
    public void testInvalidOrderNo() {
        // 测试无效订单号
        String response = airlineAssistant.chat("我要查询机票，订单号是 CN999999，乘客姓名是刘德华");
        System.out.println("=== 无效订单号测试 ===");
        System.out.println(response);
        System.out.println();
    }
}
