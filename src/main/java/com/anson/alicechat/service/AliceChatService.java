package com.anson.alicechat.service;

import com.anson.alicechat.domain.dto.ChatDTO;
import reactor.core.publisher.Flux;

public interface AliceChatService {
    Flux<String> chatWithAlice(ChatDTO chatDTO);
}
