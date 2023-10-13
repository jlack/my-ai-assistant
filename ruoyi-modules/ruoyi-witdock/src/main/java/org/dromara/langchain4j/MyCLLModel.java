package org.dromara.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

public class MyCLLModel {
    public static final String OPENAI_API_KEY = "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";
    public static final String PROXY_URL = "172.30.164.83";
    public static final int PROXY_PORT = 10809;
    public static final int TIME_OUT = 60;

    public static OpenAiChatModel getOpenAiChatModel() {
        return OpenAiChatModel
            .builder()
            .apiKey(OPENAI_API_KEY)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URL, PROXY_PORT)))
            .timeout(Duration.ofSeconds(TIME_OUT))
            .build();
    }

    public static OpenAiEmbeddingModel getOpenAiEmbeddingModel() {
        return OpenAiEmbeddingModel.builder()
            .apiKey(OPENAI_API_KEY)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URL, PROXY_PORT)))
            .timeout(Duration.ofSeconds(TIME_OUT))
            .build();
    }

    public static OpenAiStreamingChatModel getOpenAiStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URL, PROXY_PORT)))
            .timeout(Duration.ofSeconds(TIME_OUT))
            .build();
    }
}
