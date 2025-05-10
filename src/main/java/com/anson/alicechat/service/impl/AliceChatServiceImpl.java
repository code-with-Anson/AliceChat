package com.anson.alicechat.service.impl;

import com.anson.alicechat.constant.ModelNameConstant;
import com.anson.alicechat.domain.dto.ChatDTO;
import com.anson.alicechat.exception.ModelDontExistException;
import com.anson.alicechat.service.AliceChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
public class AliceChatServiceImpl implements AliceChatService {
    private final ChatClient claudeByIyunxiClient;
    private final ChatClient deepseekClient;
    private final ChatClient geminiClient;

    @Override
    public Flux<String> chatWithAlice(ChatDTO chatDTO) {
        if (chatDTO.getModel() == null) {
            throw new RuntimeException("模型不可以为空哦");
        }

        String message = chatDTO.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "你好";
        }

        return switch (chatDTO.getModel()) {
            case ModelNameConstant.CLAUDE_BY_IYUNXI -> this.chatWithClaudeByIyunxi(message);
            case ModelNameConstant.DEEPSEEK -> this.chatWithDeepseek(message);
            case ModelNameConstant.GEMINI -> this.chatWithGemini(message);
            default -> throw new ModelDontExistException("模型不存在哦");
        };
    }

    private Flux<String> chatWithClaudeByIyunxi(String message) {

        return claudeByIyunxiClient.prompt(message).stream().content();
    }

    private Flux<String> chatWithDeepseek(String message) {
        return deepseekClient.prompt(message).stream().content();
    }

    private Flux<String> chatWithGemini(String message) {
        return geminiClient.prompt(message).stream().content();
    }
}
