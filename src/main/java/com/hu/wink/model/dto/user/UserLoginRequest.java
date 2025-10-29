package com.hu.wink.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 用户登录请求
 *
 * 
 * 
 */
@ApiModel(value = "UserLoginRequest", description = "用户登录请求")
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @ApiModelProperty(value = "用户账号", example = "testuser", required = true)
    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @ApiModelProperty(value = "用户密码", example = "123456", required = true)
    @NotBlank(message = "用户密码不能为空")
    private String userPassword;
}
