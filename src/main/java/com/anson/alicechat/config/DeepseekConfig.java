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
public class DeepseekConfig {

    // 这是iyunxi平台的API地址
    @Value("${ai.openai.deepseek.base-url}")
    private String deepseekApiUrl;

    // 这是iyunxi平台的API秘钥
    @Value("${ai.openai.deepseek.api-key}")
    private String deepseekApiKey;

    // 这是使用deepseek时的默认模型
    @Value("${ai.openai.deepseek.default-deepseek-model}")
    private String deepseekModel;

    // 这是爱丽丝和Kei的人格
    @Value("classpath:/alice.st")
    private Resource aliceAndKeiPersonality;

    @Bean
    public OpenAiChatModel deepseekModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(deepseekApiKey)
                        .baseUrl(deepseekApiUrl)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(deepseekModel)
                        .temperature(0.7)
                        .build())
                .build();
    }


    @Bean
    public ChatClient deepseekClient(OpenAiChatModel deepseekModel) {
        return ChatClient.builder(deepseekModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .temperature(0.7)
                        .build()
                )
                .defaultSystem(aliceAndKeiPersonality)
                .build();
    }


}
