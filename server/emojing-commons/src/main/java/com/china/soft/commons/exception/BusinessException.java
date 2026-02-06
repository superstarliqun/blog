package com.china.soft.commons.exception;

import com.china.soft.commons.utils.response.GlobalErrorCodeConstants;
import lombok.Getter;


@Getter
public class BusinessException extends RuntimeException {

    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常提示信息
     */
    private String msg;

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super();
        this.msg = msg;
    }

    public BusinessException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

}
