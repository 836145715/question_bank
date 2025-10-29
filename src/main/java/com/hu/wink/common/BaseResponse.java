package com.hu.wink.common;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用返回类
 *
 * @param <T>


 */
@Data
@Schema(description = "通用返回结果")
public class BaseResponse<T> implements Serializable {

    @Schema(description = "状态码", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "返回消息", example = "操作成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
