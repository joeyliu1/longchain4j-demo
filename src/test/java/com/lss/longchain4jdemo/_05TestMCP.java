package com.lss.longchain4jdemo;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class _05TestMCP {


    // 测试npx 方式百度地图
    @Test
    public void test() throws Exception {
        // 1.构建模型
        ChatLanguageModel model = QwenChatModel
                .builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .modelName("qwen-max")
                .build();

        // 2.构建MCP服务传输方式  有sse和stdio两种， 这里演示的是stdio

        StdioMcpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("cmd",
                        "/c",
                        "npx",
                        "-y",
                        "@baidumap/mcp-server-baidu-map",
                        "mcp/github"))
                .environment(Map.of("BAIDU_MAP_API_KEY",
                        "LEyBQxG9UzR9C1GZ6zDHsFDVKvBem2do"))
                .logEvents(true)
                .build();

        // 3.构建MCP客户端， 指定传输方式
        DefaultMcpClient mcpClient = new DefaultMcpClient.Builder().transport(transport).build();

        // 4.构建MCP工具提供者， 指定MCP客户端

        McpToolProvider mcpToolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

        // 5.构建服务代理， 指定模型和工具提供者
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(mcpToolProvider)
                .build();

        try {
            // 对话请求
            String response = bot.chat("规划长沙到武汉骑行路线");
            System.out.println("RESPONSE: " + response);
        } finally {
            mcpClient.close();
        }

    }


    interface Bot {

        String chat(String userMessage);
    }

}
