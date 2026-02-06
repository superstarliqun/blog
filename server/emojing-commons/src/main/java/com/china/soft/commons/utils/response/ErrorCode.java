package com.china.soft.commons.utils.response;

import lombok.Data;

/**
 * description 响应体参数枚举
 * @author  MingYue
 * @version 1.0
 * @since   2025/3/18
 */
@Data
public class ErrorCode {

    /** 响应吗 */
    private final Integer code;
    /** 提示信息 */
    private final String msg;

    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
