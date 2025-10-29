package com.hu.wink.model.dto.user;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户创建请求
 *


 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 50, message = "用户昵称长度不能超过50个字符")
    private String userName;

    /**
     * 账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 4, max = 20, message = "用户账号长度必须在4-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户账号只能包含字母、数字和下划线")
    private String userAccount;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "用户头像URL长度不能超过255个字符")
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    @NotBlank(message = "用户角色不能为空")
    @Pattern(regexp = "^(user|admin)$", message = "用户角色只能是user或admin")
    private String userRole;

    private static final long serialVersionUID = 1L;
}