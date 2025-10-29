package com.hu.wink.model.dto.user;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户注册请求体
 *


 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 4, max = 20, message = "用户账号长度必须在4-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户账号只能包含字母、数字和下划线")
    private String userAccount;

    @NotBlank(message = "用户密码不能为空")
    @Size(min = 6, max = 20, message = "用户密码长度必须在6-20个字符之间")
    private String userPassword;

    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}
