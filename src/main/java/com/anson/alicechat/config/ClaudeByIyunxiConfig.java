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
public class ClaudeByIyunxiConfig {

    @Value("${ai.openai.iyunxi.base-url}")
    private String claudeApiUrl;

    @Value("${ai.openai.iyunxi.api-key}")
    private String claudeApiKey;

    @Value("${ai.openai.iyunxi.claude.default-model}")
    private String claudeModel;

    @Value("classpath:/alice.st")
    private Resource aliceAndKeiPersonality;

    @Bean
    public OpenAiChatModel claudeByIyunxiModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(claudeApiKey)
                        .baseUrl(claudeApiUrl)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(claudeModel)
                        .temperature(0.7)
                        .build())
                .build();
    }


    @Bean
    public ChatClient claudeByIyunxiClient(OpenAiChatModel claudeByIyunxiModel) {
        return ChatClient.builder(claudeByIyunxiModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.7)
                        .build()
                )
                .defaultSystem(aliceAndKeiPersonality)
                .build();
    }


}
