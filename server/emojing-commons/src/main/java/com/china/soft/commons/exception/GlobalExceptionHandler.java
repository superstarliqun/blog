package com.china.soft.commons.exception;


import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.commons.utils.response.GlobalErrorCodeConstants;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // --- 1. 捕获自定义业务异常 (如果项目中有) ---

    // 假设你有一个自定义的业务异常 BusinessException
    @ExceptionHandler(BusinessException.class)
    public CommonResponse<?> handleBusinessException(BusinessException e) {
        // 记录日志，但不把敏感信息返回给用户
        log.warn("业务异常: {}", e.getMsg());
        return CommonResponse.error(GlobalErrorCodeConstants.UNKNOWN.getCode(), e.getMsg());
    }

    @ExceptionHandler(BaseException.class)
    public CommonResponse<?> handleBaseException(BaseException e) {
        // 记录日志，但不把敏感信息返回给用户
        log.warn("系统框架异常: {}", e.getMessage());
        return CommonResponse.error(GlobalErrorCodeConstants.UNKNOWN);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public CommonResponse<?> handleInvalidJwtException(InvalidJwtException e) {
        // 记录日志，但不把敏感信息返回给用户
        log.warn("JWT组件业务异常: {}", e.getMessage());
        return CommonResponse.error(GlobalErrorCodeConstants.ERROR_CONFIGURATION);
    }

    // --- 2. 捕获所有运行时异常（兜底方案） ---
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 可以设置 HTTP 状态码为 500
    public CommonResponse<?> handleRuntimeException(RuntimeException e) {
        // 打印详细堆栈，方便排查问题
        log.error("运行时异常捕获:", e);
        // 返回统一的错误信息
        return CommonResponse.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }

    // 在 GlobalExceptionHandler 中添加这个方法：
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleGeneralException(Exception e) {
        log.error("系统全局异常捕获(Exception):", e);
        return CommonResponse.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UncheckedExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<?> handleUncheckedExecutionException(UncheckedExecutionException e) {
        Throwable cause = e.getCause();
        // 1. 如果内部是 BusinessException，按业务异常处理
        if (cause instanceof BusinessException) {
            BusinessException be = (BusinessException) cause;
            log.warn("捕获到 BusinessException (来自 UncheckedExecutionException): {}", be.getMessage());
            return CommonResponse.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), be.getMsg());
        }

        // 2. 如果内部是其他异常，按系统错误处理
        log.error("Guava 异步任务执行异常捕获:", e);
        return CommonResponse.error(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }
}
