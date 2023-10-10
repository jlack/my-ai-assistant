package org.dromara.langchain4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class MyAppTest {
    public static void main(String[] args) {
        //分段的实现，embeddingStore 执行完以后遍历embeddingStore对象，保存到dataset_doc_paragraphs
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .documentSplitter(DocumentSplitters.recursive(500, 0))
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .build();

        String url = "D:\\02-ideaWorkSpace\\WitDock\\ruoyi-modules\\ruoyi-witdock\\src\\main\\resources\\example-files\\鲲鹏演讲稿.txt";
        Document document = FileSystemDocumentLoader.loadDocument(url);
        ingestor.ingest(document);
        System.out.println();
    }
}
