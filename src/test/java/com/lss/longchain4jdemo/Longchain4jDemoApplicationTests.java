package com.lss.longchain4jdemo;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Longchain4jDemoApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void test01() {
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        String answer = model.chat("你好，你是谁？");

        System.out.println(answer);
    }

    @Test
    void test03_bad() {
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        System.out.println(model.chat("你好，我是刘德华"));
        System.out.println("----");
        System.out.println(model.chat("我叫什么"));
    }

    @Test
    void test03_good() {
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();


        UserMessage userMessage = UserMessage.userMessage("你好，我是刘德华");
        ChatResponse chat1 = model.chat(userMessage);
        AiMessage aiMessage = chat1.aiMessage();
        System.out.println(aiMessage.text());
        System.out.println("----");

        ChatResponse chat2 = model.chat(userMessage, aiMessage, UserMessage.userMessage("我叫什么"));
        System.out.println(chat2.aiMessage().text());
    }



}
