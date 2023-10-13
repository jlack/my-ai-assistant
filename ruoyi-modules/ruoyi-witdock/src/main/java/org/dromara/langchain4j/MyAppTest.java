//package org.dromara.langchain4j;
//
//import dev.langchain4j.data.document.Document;
//import dev.langchain4j.data.document.DocumentSplitter;
//import dev.langchain4j.data.document.FileSystemDocumentLoader;
//import dev.langchain4j.data.document.splitter.DocumentSplitters;
//import dev.langchain4j.data.embedding.Embedding;
//import dev.langchain4j.data.message.AiMessage;
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.data.segment.TextSegment;
//import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
//import dev.langchain4j.model.embedding.BGE_SMALL_ZH_EmbeddingModel;
//import dev.langchain4j.model.embedding.EmbeddingModel;
//import dev.langchain4j.model.input.Prompt;
//import dev.langchain4j.model.input.PromptTemplate;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//import dev.langchain4j.model.openai.OpenAiTokenizer;
//import dev.langchain4j.store.embedding.EmbeddingMatch;
//import dev.langchain4j.store.embedding.EmbeddingStore;
//import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
//import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
//import lombok.Data;
//import okio.Path;
//import org.aspectj.apache.bcel.classfile.InnerClass;
//import org.w3c.dom.Text;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//import static dev.langchain4j.data.document.FileSystemDocumentLoader.loadDocument;
//import static dev.langchain4j.model.openai.OpenAiModelName.GPT_3_5_TURBO;
//import static java.net.Proxy.Type.HTTP;
//import static java.util.stream.Collectors.joining;
//
//public class MyAppTest {
//    public static void main(String[] args) throws Exception {
//        //分段的实现，embeddingStore 执行完以后遍历embeddingStore对象，保存到dataset_doc_paragraphs
//        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
//
//        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//
//        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
//            .documentSplitter(DocumentSplitters.recursive(500, 0))
//            .embeddingModel(embeddingModel)
//            .embeddingStore(embeddingStore)
//            .build();
//
//        String url = "E:\\Projects\\wit-dock\\ruoyi-modules\\ruoyi-witdock\\src\\main\\resources\\example-files\\鲲鹏演讲稿.txt";
//        Document document = FileSystemDocumentLoader.loadDocument(url);
//        ingestor.ingest(document);
//
//
////        输出 entries中分段结束后每一段的文本内容。
//// 使用反射获取 entries 字段
////        Field entriesField = embeddingStore.getClass().getDeclaredField("entries");
////        entriesField.setAccessible(true);
////        List<?> entries = (List<?>) entriesField.get(embeddingStore);
////
////        // 输出 entries 中分段结束后每一段的文本内容
////        for (Object entry : entries) {
////            // 使用反射获取 embedded 字段
////            Field embeddedField = entry.getClass().getDeclaredField("embedded");
////            embeddedField.setAccessible(true);
////            TextSegment segment = (TextSegment) embeddedField.get(entry);
////
////            System.out.println(segment.text());
////        }
//
//        // 使用反射获取 entries 字段
//        parasToTXT(embeddingStore);
//    }
//
//
//    public static void parasToTXT(EmbeddingStore<TextSegment> embeddingStore) throws NoSuchFieldException, IllegalAccessException, IOException {
//        Field entriesField = embeddingStore.getClass().getDeclaredField("entries");
//        entriesField.setAccessible(true);
//        List<?> entries = (List<?>) entriesField.get(embeddingStore);
//
//        // 创建一个 FileWriter 对象来写入 txt 文件
//        FileWriter writer = new FileWriter("C:\\Users\\A70822\\Desktop\\分段情况.txt");
//
//        // 遍历每个分段
//        for (int i = 0; i < entries.size(); i++) {
//            Field embeddedField = entries.get(i).getClass().getDeclaredField("embedded");
//            embeddedField.setAccessible(true);
//            TextSegment segment = (TextSegment) embeddedField.get(entries.get(i));
//
//            // 将段落标识和段落内容写入到 txt 文件中
//            writer.write("第" + (i + 1) + "段:\n");
//            writer.write(segment.text() + "\n\n");
//        }
//
//        // 关闭 FileWriter 对象
//        writer.close();
//    }
//
//    public static void specificMethod() {
////        // Split document into segments 100 tokens each
////        DocumentSplitter splitter = DocumentSplitters.recursive(
////            100,
////            0,
////            new OpenAiTokenizer(GPT_3_5_TURBO)
////        );
////        List<TextSegment> segments = splitter.split(document);
////
////// Embed segments (convert them into vectors that represent the meaning) using embedding model
////        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
////        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
////
////// Store embeddings into embedding store for further search / retrieval
////        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
////        embeddingStore.addAll(embeddings, segments);
//    }
//
//    static class CN_Model {
//
//        public static void main(String[] args) {
//
//            // Load the document that includes the information you'd like to "chat" about with the model.
//            Document document = loadDocument("E:\\Projects\\wit-dock\\ruoyi-modules\\ruoyi-witdock\\src\\main\\resources\\example-files\\鲲鹏演讲稿.txt");
//
//            // Split document into segments 100 tokens each
//            DocumentSplitter splitter = DocumentSplitters.recursive(
//                1000,
//                0,
//                new OpenAiTokenizer(GPT_3_5_TURBO)
//            );
//            List<TextSegment> segments = splitter.split(document);
//
//            // Embed segments (convert them into vectors that represent the meaning) using embedding model
////            EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
////            换成中文向量解析模型
//            EmbeddingModel embeddingModel = new BGE_SMALL_ZH_EmbeddingModel();
//
//            List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
//
//            // Store embeddings into embedding store for further search / retrieval
//            EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
//            embeddingStore.addAll(embeddings, segments);
//
////            String question = "Who the hell is Charlie?";
//
//            String question = "谁参加了鲲鹏应用创新大赛";
//
//            // Embed the question
//            Embedding questionEmbedding = embeddingModel.embed(question).content();
//
//            // Find relevant embeddings in embedding store by semantic similarity
//            // You can play with parameters below to find a sweet spot for your specific use case
//            int maxResults = 3;
//            double minScore = 0.7;
//            List<EmbeddingMatch<TextSegment>> relevantEmbeddings
//                = embeddingStore.findRelevant(questionEmbedding, maxResults, minScore);
//
//            // Create a prompt for the model that includes question and relevant embeddings
//            PromptTemplate promptTemplate = PromptTemplate.from(
//                "Answer the following question to the best of your ability:\n"
//                    + "\n"
//                    + "Question:\n"
//                    + "{{question}}\n"
//                    + "\n"
//                    + "Base your answer on the following information:\n"
//                    + "{{information}}");
//
//            String information = relevantEmbeddings.stream()
//                .map(match -> match.embedded().text())
//                .collect(joining("\n\n"));
//
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("question", question);
//            variables.put("information", information);
//
//            Prompt prompt = promptTemplate.apply(variables);
//
//            // Send the prompt to the OpenAI chat model
////            ChatLanguageModel chatModel = OpenAiChatModel.withApiKey(ApiKeys.OPENAI_API_KEY);
//
//            OpenAiChatModel chatModel = OpenAiChatModel.builder()
//                .apiKey("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")
//                .timeout(Duration.ofSeconds(360))
//                .proxy(new Proxy(HTTP, new InetSocketAddress("172.30.164.83", 10809)))
//                .build();
//            AiMessage aiMessage = chatModel.generate(prompt.toUserMessage()).content();
//
//            // See an answer from the model
//            String answer = aiMessage.text();
//            System.out.println(answer); // Charlie is a cheerful carrot living in VeggieVille...
//        }
//    }
//}
