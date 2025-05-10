package com.anson.alicechat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
public class GeminiConfig {


    @Value("${ai.openai.deepseek.base-url}")
    private String geminiApiUrl;


    @Value("${ai.openai.deepseek.api-key}")
    private String geminiApiKey;


    @Value("${ai.openai.deepseek.default-deepseek-model}")
    private String geminiModel;

    // 这是爱丽丝和Kei的人格
    @Value("classpath:/alice.st")
    private Resource aliceAndKeiPersonality;

    @Bean
    public OpenAiChatModel geminiModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(geminiApiKey)
                        .baseUrl(geminiApiUrl)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(geminiModel)
                        .temperature(0.7)
                        .build())
                .build();
    }


    @Bean
    public ChatClient geminiClient(OpenAiChatModel geminiModel) {
        return ChatClient.builder(geminiModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.7)
                        .build()
                )
                .defaultSystem(aliceAndKeiPersonality)
                .build();
    }


}
