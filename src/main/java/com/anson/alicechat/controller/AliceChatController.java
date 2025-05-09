package com.anson.alicechat.controller;

import com.anson.alicechat.domain.dto.ChatDTO;
import com.anson.alicechat.service.AliceChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")

@RequiredArgsConstructor
public class AliceChatController {
    private final AliceChatService aliceChatService;


    // 和alice聊天
    @PostMapping(value = "/chat", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> ChatWithAlice(@RequestBody ChatDTO chatDTO) {
        return aliceChatService.chatWithAlice(chatDTO);
    }
}
