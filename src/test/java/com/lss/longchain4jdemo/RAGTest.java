package com.lss.longchain4jdemo;

import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
class RAGTest {


    @Test
    public void testRAG() throws URISyntaxException {

        Path documentPath = Paths.get(RAGTest.class.getClassLoader().getResource("rag/terms-of-service.txt").toURI());
//        TextDocumentParser textDocumentParser = new TextDocumentParser();
        Document document = FileSystemDocumentLoader.loadDocument(documentPath);

        DocumentByLineSplitter splitter = new DocumentByLineSplitter(
                20, // 每段最长字数
                10                      // 自然语言最大重叠字数
                );

        List<TextSegment> segments = splitter.split(document);
        QwenEmbeddingModel embeddingModel = QwenEmbeddingModel.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        // 存入
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);


        Embedding content = embeddingModel.embed("退款政策").content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder().queryEmbedding(content).build();

        EmbeddingSearchResult<TextSegment> results = embeddingStore.search(searchRequest);
        for (EmbeddingMatch<TextSegment> match : results.matches()) {
            System.out.println(match.embedded().text() + ",分数为：" + match.score());
        }

    }
}
