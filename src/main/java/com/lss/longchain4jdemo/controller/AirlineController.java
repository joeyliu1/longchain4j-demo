package com.lss.longchain4jdemo.controller;

import com.lss.longchain4jdemo.config.AiConfig;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * 航空客服控制器
 * 提供机票查询、改签、取消等 API 接口
 */
@RestController
@RequestMapping("/airline")
public class AirlineController {

    @Autowired
    AiConfig.AirlineAssistant airlineAssistant;

    /**
     * 普通聊天接口
     * @param message 用户消息
     * @return AI 回复
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(defaultValue = "你好，我要查询机票") String message) {
        return airlineAssistant.chat(message);
    }

    /**
     * 流式聊天接口（支持上下文记忆）
     * @param message 用户消息
     * @return 流式响应
     */
    @GetMapping(value = "/stream", produces = "text/stream;charset=UTF-8")
    public Flux<String> stream(@RequestParam(defaultValue = "你好") String message) {
        TokenStream stream = airlineAssistant.stream(message, LocalDate.now().toString());
        return Flux.create(sink -> {
            stream.onPartialResponse(sink::next)
                    .onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }

    /**
     * 查询订单接口
     * @param orderNo 订单号
     * @param passengerName 乘客姓名
     * @return 订单信息
     */
    @GetMapping("/query")
    public String queryOrder(
            @RequestParam String orderNo,
            @RequestParam String passengerName) {
        String message = String.format("我要查询机票，订单号是%s，乘客姓名是%s", orderNo, passengerName);
        return airlineAssistant.chat(message);
    }

    /**
     * 改签订单接口
     * @param orderNo 订单号
     * @param passengerName 乘客姓名
     * @param newFlightNo 新航班号
     * @param newDepartureTime 新起飞时间
     * @return 改签结果
     */
    @GetMapping("/change")
    public String changeOrder(
            @RequestParam String orderNo,
            @RequestParam String passengerName,
            @RequestParam String newFlightNo,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") java.time.LocalDateTime newDepartureTime) {
        String message = String.format(
                "我要改签机票，订单号是%s，乘客姓名是%s，新航班号是%s，新起飞时间是%s",
                orderNo, passengerName, newFlightNo, newDepartureTime);
        return airlineAssistant.chat(message);
    }

    /**
     * 取消订单接口
     * @param orderNo 订单号
     * @param passengerName 乘客姓名
     * @return 取消结果
     */
    @GetMapping("/cancel")
    public String cancelOrder(
            @RequestParam String orderNo,
            @RequestParam String passengerName) {
        String message = String.format("我要取消机票，订单号是%s，乘客姓名是%s", orderNo, passengerName);
        return airlineAssistant.chat(message);
    }
}
