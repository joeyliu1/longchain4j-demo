package com.lss.longchain4jdemo.controller;

import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.output.Response;

public class LangChainVector {

    public static void main(String[] args) {

        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        Response<Embedding> embed = embeddingModel.embed("你好，我叫刘德华");
        System.out.println(embed.content().toString());
        System.out.println(embed.content().vector().length);

    }
}
