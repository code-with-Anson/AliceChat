package com.anson.alicechat.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Configuration
public class ClaudeByIyunxiConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClaudeByIyunxiConfig.class);

    @Value("${ai.openai.iyunxi.base-url}")
    private String claudeApiUrl;

    @Value("${ai.openai.iyunxi.api-key}")
    private String claudeApiKey;

    @Value("${ai.openai.iyunxi.claude.default-model}")
    private String claudeModel;

    @Value("classpath:/alice.st")
    private Resource aliceAndKeiPersonality;

    @Bean
    public WebClient iyunxiWebClient() throws SSLException {
        // 配置SSL
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        // 配置HTTP客户端，添加多种超时选项
        HttpClient httpClient = HttpClient.create()
                .secure(spec -> spec.sslContext(sslContext))
                .responseTimeout(Duration.ofSeconds(120))
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .wiretap(true) // 启用Netty的日志调试
                .keepAlive(true);

        // 增加内存限制，处理更大的响应
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(claudeApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Connection", "keep-alive")
                .exchangeStrategies(exchangeStrategies)
                .filter(logRequest())  // 添加请求日志过滤器
                .filter(logResponse()) // 添加响应日志过滤器
                .build();
    }

    // 日志记录请求
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) ->
                    values.forEach(value -> LOGGER.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    // 日志记录响应
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOGGER.info("Response status: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders().forEach((name, values) ->
                    values.forEach(value -> LOGGER.info("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }

    @Bean
    public OpenAiChatModel claudeModel() {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(claudeApiKey)
                        .baseUrl(claudeApiUrl)
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(claudeModel)
                        .temperature(0.7)
                        .maxTokens(4000)
                        .build())
                .build();
    }

    @Bean
    public ChatClient claudeByIyunxi(OpenAiChatModel claudeModel) {
        return ChatClient.builder(claudeModel)
                .defaultSystem(aliceAndKeiPersonality)
                .build();
    }

    // 添加一个简单的方法来测试WebClient连接
    @Bean
    public boolean testIyunxiConnection(WebClient iyunxiWebClient) {
        try {
            // 尝试发送一个简单的GET请求，只是测试连接
            String response = iyunxiWebClient.get()
                    .uri("/")
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(e -> LOGGER.error("测试连接失败: " + e.getMessage(), e))
                    .onErrorReturn("连接失败")
                    .block(Duration.ofSeconds(10));

            LOGGER.info("测试连接响应: {}", response);
            return true;
        } catch (Exception e) {
            LOGGER.error("测试连接异常: " + e.getMessage(), e);
            return false;
        }
    }
}