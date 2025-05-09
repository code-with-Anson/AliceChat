package com.anson.alicechat.exception;

/**
 * 自定义业务异常：对话不存在异常
 * 继承了我们自己定义的BaseException
 * 允许我们返回给前端异常信息
 */
public class ModelDontExistException extends BaseException {
    public ModelDontExistException(String msg) {
        super(msg, 50391);
    }
}