package com.lss.longchain4jdemo.config;

import com.lss.longchain4jdemo.service.ToolsService;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AiConfig {


    public interface Assistant {
//        @SystemMessage(fromResource = "system_message.txt")
        String chat(String message);

        // 流式响应
        TokenStream stream(String message);
    }

    @Bean
    public QwenStreamingChatModel getQwenStreamingChatModel() {
        return QwenStreamingChatModel
                .builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .modelName("qwen-plus")
                .build();
    }


    @Bean
    public Assistant assistant(ChatLanguageModel qwenChatModel,
                               StreamingChatLanguageModel qwenStreamingChatModel,
                               ToolsService toolsService) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(qwenChatModel)
                .streamingChatLanguageModel(qwenStreamingChatModel)
                .tools(toolsService)
                .chatMemory(chatMemory)
                .build();
    }


    public interface AssistantUnique {
        String chat(@MemoryId int memoryId, @UserMessage String userMessage);
        // 流式响应
        TokenStream stream(@MemoryId int memoryId, @UserMessage String userMessage);
    }

    @Bean
    public AssistantUnique assistantUnique(ChatLanguageModel qwenChatModel) {
        return AiServices.builder(AssistantUnique.class)
                .chatLanguageModel(qwenChatModel)
                .streamingChatLanguageModel(getQwenStreamingChatModel())
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder().maxMessages(10)
                                .id(memoryId).build()
                )
                .build();
    }

    @Bean
    public AssistantUnique assistantUniqueStore(ChatLanguageModel qwenChatModel) {

        PersistentChatMemoryStore store = new PersistentChatMemoryStore();

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(store)
                .build();

        return AiServices.builder(AssistantUnique.class)
                .chatLanguageModel(qwenChatModel)
                .streamingChatLanguageModel(getQwenStreamingChatModel())
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }


    public interface Assistant2 {
        String chat(String message);
        // 流式响应

        TokenStream stream(String message);
        @SystemMessage("""
                您是“Tuling”航空公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                        您正在通过在线聊天系统与客户互动。 
                        在提供有关预订或取消预订的信息之前，您必须始终从用户处获取以下信息：预订号、客户姓名。
                        请讲中文。
					   今天的日期是 {{current_date}}.
                """)
        TokenStream stream(@UserMessage String message,@V("current_date") String currentDate);
    }

    @Bean
    public Assistant2 assistant2(ChatLanguageModel qwenChatModel,
                               StreamingChatLanguageModel qwenStreamingChatModel,
                               ToolsService toolsService) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(Assistant2.class)
                .chatLanguageModel(qwenChatModel)
                .streamingChatLanguageModel(qwenStreamingChatModel)
                .tools(toolsService)
                .chatMemory(chatMemory)
                .build();
    }

    /**
     * 航空客服 Assistant 接口
     * 专门处理机票查询、改签、取消等业务
     */
    public interface AirlineAssistant {
        @SystemMessage(fromResource = "system-prompt.txt")
        String chat(String message);

        @SystemMessage(fromResource = "system-prompt.txt")
        TokenStream stream(@UserMessage String message, @V("current_date") String currentDate);
    }

    @Bean
    public AirlineAssistant airlineAssistant(ChatLanguageModel qwenChatModel,
                                            StreamingChatLanguageModel qwenStreamingChatModel,
                                            ToolsService toolsService) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(AirlineAssistant.class)
                .chatLanguageModel(qwenChatModel)
                .streamingChatLanguageModel(qwenStreamingChatModel)
                .tools(toolsService)
                .chatMemory(chatMemory)
                .build();
    }


}
