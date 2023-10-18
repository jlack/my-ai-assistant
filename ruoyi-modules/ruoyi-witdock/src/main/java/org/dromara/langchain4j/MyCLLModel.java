package org.dromara.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.dromara.common.core.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

public class MyCLLModel {
    public static final String OPENAI_API_KEY = "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";
    public static final String PROXY_URL = "172.30.164.83";
    public static final int PROXY_PORT = 10809;
    public static final int TIME_OUT = 600;

    public static OpenAiChatModel getOpenAiChatModel() {
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .timeout(Duration.ofSeconds(TIME_OUT));

        if (!isProdEnvironment()) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URL, PROXY_PORT)));
        }

        return builder.build();
    }

    public static OpenAiEmbeddingModel getOpenAiEmbeddingModel() {
        OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder builder = OpenAiEmbeddingModel.builder()
            .apiKey(OPENAI_API_KEY)
            .timeout(Duration.ofSeconds(TIME_OUT));

        if (!isProdEnvironment()) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URL, PROXY_PORT)));
        }

        return builder.build();
    }

    public static OpenAiStreamingChatModel getOpenAiStreamingChatModel() {
        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder()
            .apiKey(OPENAI_API_KEY)
            .timeout(Duration.ofSeconds(TIME_OUT));

        if (!isProdEnvironment()) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URL, PROXY_PORT)));
        }

        return builder.build();
    }

    private static boolean isProdEnvironment() {
        String environment = SpringUtils.context().getEnvironment().getActiveProfiles()[0];
        return "prod".equalsIgnoreCase(environment);
    }
}
