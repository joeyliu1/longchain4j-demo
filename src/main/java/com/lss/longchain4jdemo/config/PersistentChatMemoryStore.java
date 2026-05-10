package com.lss.longchain4jdemo.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final Map<Integer, List<ChatMessage>> map = new HashMap<>();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // todo 根据memoryId从数据库获取
        return List.of();
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // todo 根据memoryId修改、新增记录
    }

    @Override
    public void deleteMessages(Object memoryId) {
        // todo 根据memoryId删除
    }
}
