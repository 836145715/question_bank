package com.hu.wink.exception;

import com.hu.wink.common.BaseResponse;
import com.hu.wink.common.ErrorCode;
import com.hu.wink.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 全局异常处理器
 *


 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    /**
     * 处理 @Valid 注解校验失败异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            String message = fieldError.getDefaultMessage();
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, message);
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数校验失败");
    }

    /**
     * 处理 @Validated 注解校验失败异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResponse<?> bindExceptionHandler(BindException e) {
        log.error("BindException", e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            String message = fieldError.getDefaultMessage();
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, message);
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数校验失败");
    }

    /**
     * 处理 @Validated 注解校验失败异常（URL参数校验）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("ConstraintViolationException", e);
        ConstraintViolation<?> constraintViolation = e.getConstraintViolations().iterator().next();
        if (constraintViolation != null) {
            String message = constraintViolation.getMessage();
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, message);
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数校验失败");
    }

    /**
     * 处理验证异常（兜底处理）
     */
    @ExceptionHandler(ValidationException.class)
    public BaseResponse<?> validationExceptionHandler(ValidationException e) {
        log.error("ValidationException", e);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, e.getMessage());
    }

    /**
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("Exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
