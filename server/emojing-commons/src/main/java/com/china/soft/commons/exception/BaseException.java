package com.china.soft.commons.exception;

import lombok.Getter;

/**
 * @author 衡钊清
 * @Classname BaseException
 * @Description 基础异常处理类
 * @Date 2020/2/12 15:43
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常提示信息
     */
    private String msg;

    public BaseException() {
        super();
    }

    public BaseException(String msg) {
        super();
        this.msg = msg;
    }

    public BaseException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

}
