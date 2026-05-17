# longchain4j-demo

基于 LangChain4j + Spring Boot 实现的 AI 应用示例项目，演示如何构建智能对话系统、知识库问答、工具调用等场景。

## 📚 项目简介

本项目展示了 Spring Boot 3.5 + LangChain4j 1.0 + 通义千问的完整集成方案，支持：

- ✅ 对话记忆管理（多轮上下文）
- ✅ AI 工具调用（Function Calling）
- ✅ RAG 检索增强生成
- ✅ 多模型协作 Agent
- ✅ MCP 协议集成
- ✅ 流式响应（SSE）

## 📦 技术栈

| 组件 | 版本/说明 |
|------|----------|
| Spring Boot | 3.5.13 |
| LangChain4j | 1.0.0-beta1 |
| Java | 17 |
| 通义千问 | qwen-plus / qwen-max |
| AI 提供商 | 阿里 DashScope / MiniMax |
| Web 框架 | Spring WebFlux (响应式) |
| 构建工具 | Maven |

## 🗂️ 项目结构

```
src/main/java/com/lss/longchain4jdemo/
├── Longchain4jDemoApplication.java      # 启动类
├── config/
│   ├── AiConfig.java                    # AI 配置（多个 Assistant）
│   └── PersistentChatMemoryStore.java   # 持久化记忆存储
├── controller/
│   ├── AiController.java                # 基础 AI 聊天
│   ├── OtherAIController.java           # 多种聊天模式
│   └── AirlineController.java           # 航空客服接口
├── service/
│   ├── ToolsService.java                # AI 工具服务（Function Calling）
│   └── AirlineService.java              # 航空客服业务逻辑
└── entity/
    └── Order.java                       # 订单实体

src/main/resources/
├── application.yaml                     # 应用配置
├── system-prompt.txt                    # 客服系统提示词
└── rag/
    └── terms-of-service.txt             # RAG 示例文档

src/test/java/
├── AirlineServiceTest.java              # 客服测试
├── _04TestAgent.java                    # Agent 编排测试
├── _05TestMCP.java                      # MCP 集成测试
└── RAGTest.java                         # RAG 测试
```

## 🚀 快速开始

### 1. 设置环境变量

```bash
# 通义千问 API（必填）
export AI_DASHSCOPE_API_KEY=your_dashscope_api_key

# MiniMax API（可选，用于 Agent 编排）
export AI_MINMAX_API_KEY=your_minimax_api_key
```

### 2. 启动项目

```bash
mvn spring-boot:run
```

### 3. 访问接口

| API | 说明 |
|-----|------|
| `GET /ai/chat?message=你好` | 基础聊天 |
| `GET /ai/memory_stream_chat` | 带记忆的流式聊天 |
| `GET /ai_other/memory_chat` | 多轮对话 |
| `GET /airline/chat` | 航空客服 |
| `GET /airline/query?orderNo=CN123456&passengerName=刘德华` | 查询订单 |
| `GET /airline/change` | 改签机票 |
| `GET /airline/cancel` | 取消订单 |

## 📖 使用场景

### 1️⃣ **智能客服系统** `/airline`

基于通义千问的航空客服系统，支持：

- 机票查询（订单号 + 姓名验证）
- 机票改签（自动计算改签费）
- 机票取消（自动计算退款金额）

**测试订单**：

| 订单号 | 乘客 | 航班 | 行程 | 舱位 |
|--------|------|------|------|------|
| CN123456 | 刘德华 | CA1501 | 上海→北京 | 经济舱 |
| CN789012 | 张学友 | 3U8888 | 广州→成都 | 商务舱 |
| CN345678 | 郭富城 | ZH9876 | 深圳→杭州 | 豪华经济舱 |

**费用规则**：

- 改签费：经济舱 50 元 / 豪华舱 30 元 / 商务舱 免费
- 取消费：经济舱 75 美元 / 豪华舱 50 美元 / 商务舱 25 美元

### 2️⃣ **知识库问答** (`RAGTest.java`)

基于 LangChain4j 的 RAG 实现：

1. 加载文档（`terms-of-service.txt`）
2. 分割文本（`DocumentByLineSplitter`）
3. 向量化（`QwenEmbeddingModel`）
4. 向量检索（`InMemoryEmbeddingStore`）
5. 构建 Prompt 返回答案

### 3️⃣ **AI Agent 编排** (`_04TestAgent.java`)

多模型协作场景：

- 使用 MiniMax 进行任务分类
- 使用通义千问进行对话生成
- 根据任务类型（查询/改签/取消）路由到不同处理逻辑

### 4️⃣ **MCP 协议集成** (`_05TestMCP.java`)

通过 Model Context Protocol 调用外部工具：

- 示例：百度地图 MCP 服务
- 路径规划：规划长沙→武汉骑行路线

## 🛠️ 配置说明

### application.yaml

```yaml
langchain4j:
  community:
    dashscope:
      chatModel:
        apiKey: ${AI_DASHSCOPE_API_KEY}
        modelName: qwen-plus
```

### 提示词配置

客服系统提示词保存在 `src/main/resources/system-prompt.txt`，可自定义：

```
您是"上海香港 航空"公司的客户聊天支持代理...
```

## 📝 AI Assistant 配置

### AiConfig.java 支持的接口

| Assistant | 说明 |
|-----------|------|
| `Assistant` | 基础助手（带工具调用） |
| `AssistantUnique` | 多用户隔离助手（通过 `@MemoryId`） |
| `Assistant2` | 模板变量助手（支持 `{{current_date}}`） |
| `AirlineAssistant` | 航空客服助手（带系统提示词） |

### 工具调用

```java
@Tool("查询上海的天气")
public String shanghaiWeather() {
    return "上海当前天气：晴，温度 25°C";
}
```

## 🧪 运行测试

```bash
# 运行客服测试
mvn test -Dtest=AirlineServiceTest

# 运行 RAG 测试
mvn test -Dtest=RAGTest

# 运行所有测试
mvn test
```

## 🔑 环境变量

| 变量名 | 必填 | 说明 |
|--------|------|------|
| `AI_DASHSCOPE_API_KEY` | ✅ | 通义千问 API 密钥（DashScope） |
| `AI_MINMAX_API_KEY` | ❌ | MiniMax API 密钥（Agent 编排） |

## 📖 参考文档

- [LangChain4j 官方文档](https://docs.langchain4j.dev/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [通义千问 API 文档](https://help.aliyun.com/document_detail/611760.html)

## 📄 项目文档

- [航空客服系统使用指南](AIRLINE_SERVICE.md)
- [RAG 检索增强生成示例](RAG.md) (待补充)

## 🔧 扩展建议

1. **持久化存储**：替换 `InMemoryEmbeddingStore` 为 Pinecone/_pgvector
2. **数据库集成**：替换 `Map` 为 MySQL/MongoDB
3. **鉴权系统**：集成 OAuth2/JWT
4. **监控日志**：接入 Prometheus + Grafana
5. **缓存优化**：Redis 缓存热门查询

## 📝 License

MIT License
