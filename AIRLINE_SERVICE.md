# 航空客服系统使用指南

## 功能概述

基于 LangChain4j + Spring Boot 实现的智能航空客服系统，支持机票查询、改签、取消等功能。

## API 接口

### 1. 普通聊天
```
GET /airline/chat?message=你好，我要查询机票
```

### 2. 流式聊天（支持上下文）
```
GET /airline/stream?message=你好
```

### 3. 查询订单
```
GET /airline/query?orderNo=CN123456&passengerName=刘德华
```

### 4. 改签订单
```
GET /airline/change?orderNo=CN123456&passengerName=刘德华&newFlightNo=CA1503&newDepartureTime=2026-05-21 14:30
```

### 5. 取消订单
```
GET /airline/cancel?orderNo=CN123456&passengerName=刘德华
```

## 测试数据

系统预置了 3 个测试订单：

| 订单号 | 乘客姓名 | 航班 | 行程 | 舱位 | 状态 |
|--------|----------|------|------|------|------|
| CN123456 | 刘德华 | CA1501 | 上海→北京 | 经济舱 | 已出票 |
| CN789012 | 张学友 | 3U8888 | 广州→成都 | 商务舱 | 已出票 |
| CN345678 | 郭富城 | ZH9876 | 深圳→杭州 | 豪华经济舱 | 已出票 |

## 业务规则

### 改签费用
- 经济舱：50 元
- 豪华经济舱：30 元
- 商务舱：免费

### 取消费用
- 经济舱：75 美元
- 豪华经济舱：50 美元
- 商务舱：25 美元

### 注意事项
- 改签需在航班起飞前 24 小时办理
- 取消需在航班起飞前 48 小时办理
- 退款将在 7 个工作日内处理

## 运行测试

```bash
# 设置环境变量
export AI_DASHSCOPE_API_KEY=your_api_key

# 运行测试
mvn test -Dtest=AirlineServiceTest
```

## 启动应用

```bash
mvn spring-boot:run
```

访问：http://localhost:8080/airline/chat?message=你好
