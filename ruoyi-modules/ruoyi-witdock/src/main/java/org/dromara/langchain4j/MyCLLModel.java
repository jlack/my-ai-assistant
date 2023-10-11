package org.dromara.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

public class MyCLLModel {
    public static final String OPENAI_API_KEY = "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";

    public static OpenAiChatModel getOpenAI() {
        return OpenAiChatModel
            .builder()
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.30.164.133", 10809)))
            .timeout(Duration.ofSeconds(60))
            .apiKey(OPENAI_API_KEY)
            .build();
    }
}
