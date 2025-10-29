package com.hu.wink.model.dto.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 用户登录请求
 *
 * 
 * 
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @NotBlank(message = "用户密码不能为空")
    private String userPassword;
}
