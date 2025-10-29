package com.hu.wink.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 用户登录请求
 *
 *
 *
 */
@Schema(description = "用户登录请求")
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @Schema(description = "用户账号", example = "testuser", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @Schema(description = "用户密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户密码不能为空")
    private String userPassword;
}
