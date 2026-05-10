package com.lss.longchain4jdemo.controller;

import com.lss.longchain4jdemo.config.AiConfig;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai_other")
public class OtherAIController {

    @Autowired
    AiConfig.Assistant assistant;


    @GetMapping("/memory_chat")
    public String testChatMemory(@RequestParam(defaultValue = "我是刘德华") String message) {
        return assistant.chat(message);
    }

    @GetMapping(value = "/memory_stream_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryStreamChat(@RequestParam(defaultValue = "我是谁") String message) {
        TokenStream stream = assistant.stream(message);
        return Flux.create(sink -> {
            stream.onPartialResponse(s -> sink.next(s))
                    .onCompleteResponse(c -> sink.complete())
                    .onError(sink::error)
                    .start();

        });
    }

    @RequestMapping(value = "/stream_chat", produces = "text/stream;charset=UTF-8")
    public Flux<String> test(@RequestParam(defaultValue = "你是谁") String message) {


        QwenStreamingChatModel chatModel = QwenStreamingChatModel
                .builder()
                .apiKey("demo")
                .modelName("qwen2.5-3b-streaming")
                .build();

        return Flux.create(sink -> {
            chatModel.chat(message, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    sink.next(partialResponse);  // 逐次返回部分响应
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    sink.complete();  // 完成整个响应流
                }

                @Override
                public void onError(Throwable error) {
                    sink.error(error);  // 异常处理
                }
            });
        });
    }


    @Autowired
    AiConfig.AssistantUnique assistantUnique;

    @RequestMapping(value = "/memoryId_chat")
    public String memoryChat(@RequestParam(defaultValue = "我是谁") String message, Integer userId) {
        return assistantUnique.chat(userId, message);
    }
}