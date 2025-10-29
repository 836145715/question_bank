package com.hu.wink.common;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用返回类
 *
 * @param <T>


 */
@Data
@ApiModel(value = "BaseResponse", description = "通用返回结果")
public class BaseResponse<T> implements Serializable {

    @ApiModelProperty(value = "状态码", example = "0", required = true)
    private int code;

    @ApiModelProperty(value = "返回数据", notes = "泛型数据对象")
    private T data;

    @ApiModelProperty(value = "返回消息", example = "操作成功", required = true)
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
