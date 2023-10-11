package org.dromara.langchain4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.Data;
import org.aspectj.apache.bcel.classfile.InnerClass;
import org.w3c.dom.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;

public class MyAppTest {
    public static void main(String[] args) throws Exception {
        String str = "http://122.9.157.44:9000/wit-dock/2023/10/11/a2dcff8e97174d9e8ad6bb1330e39ff7.txt";
        System.out.println(str.substring(7));
        if (false) {
            //分段的实现，embeddingStore 执行完以后遍历embeddingStore对象，保存到dataset_doc_paragraphs
            EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

            EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

            String url = "E:\\Projects\\wit-dock\\ruoyi-modules\\ruoyi-witdock\\src\\main\\resources\\example-files\\鲲鹏演讲稿.txt";
            Document document = FileSystemDocumentLoader.loadDocument(url);
            ingestor.ingest(document);


//        输出 entries中分段结束后每一段的文本内容。
// 使用反射获取 entries 字段
//        Field entriesField = embeddingStore.getClass().getDeclaredField("entries");
//        entriesField.setAccessible(true);
//        List<?> entries = (List<?>) entriesField.get(embeddingStore);
//
//        // 输出 entries 中分段结束后每一段的文本内容
//        for (Object entry : entries) {
//            // 使用反射获取 embedded 字段
//            Field embeddedField = entry.getClass().getDeclaredField("embedded");
//            embeddedField.setAccessible(true);
//            TextSegment segment = (TextSegment) embeddedField.get(entry);
//
//            System.out.println(segment.text());
//        }

            // 使用反射获取 entries 字段
            parasToTXT(embeddingStore);
        }
        System.out.println();
    }

    public static void parasToTXT(EmbeddingStore<TextSegment> embeddingStore) throws NoSuchFieldException, IllegalAccessException, IOException {
        Field entriesField = embeddingStore.getClass().getDeclaredField("entries");
        entriesField.setAccessible(true);
        List<?> entries = (List<?>) entriesField.get(embeddingStore);

        // 创建一个 FileWriter 对象来写入 txt 文件
        FileWriter writer = new FileWriter("C:\\Users\\A70822\\Desktop\\分段情况.txt");

        // 遍历每个分段
        for (int i = 0; i < entries.size(); i++) {
            Field embeddedField = entries.get(i).getClass().getDeclaredField("embedded");
            embeddedField.setAccessible(true);
            TextSegment segment = (TextSegment) embeddedField.get(entries.get(i));

            // 将段落标识和段落内容写入到 txt 文件中
            writer.write("第" + (i + 1) + "段:\n");
            writer.write(segment.text() + "\n\n");
        }

        // 关闭 FileWriter 对象
        writer.close();
    }

    public static void specificMethod() {
//        // Split document into segments 100 tokens each
//        DocumentSplitter splitter = DocumentSplitters.recursive(
//            100,
//            0,
//            new OpenAiTokenizer(GPT_3_5_TURBO)
//        );
//        List<TextSegment> segments = splitter.split(document);
//
//// Embed segments (convert them into vectors that represent the meaning) using embedding model
//        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
//        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
//
//// Store embeddings into embedding store for further search / retrieval
//        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//        embeddingStore.addAll(embeddings, segments);
    }
}
