package com.anson.alicechat.domain.dto;

import lombok.Data;

@Data
public class ChatDTO {
    // 用户输入的消息
    private String message;

    // 使用的模型
    private String model;
}
