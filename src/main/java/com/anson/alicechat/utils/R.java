package com.anson.alicechat.utils;

import com.anson.alicechat.enums.ResponseCode;
import com.anson.alicechat.exception.BaseException;
import lombok.Data;

@Data
public class R<T> {
    private int code;
    private String msg;
    private T data;

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应
    public static <T> R<T> success(T data) {
        return new R<>(ResponseCode.SUCCESS.getValue(), ResponseCode.SUCCESS.getDescription(), data);
    }

    // 静态方法：失败响应
    public static <T> R<T> failure(String message) {
        return new R<>(ResponseCode.FAILURE.getValue(), message, null);
    }

    // 自定义异常响应
    public static <T> R<T> failure(BaseException e) {
        return new R<>(e.getCode(), e.getMessage(), null);
    }
}
