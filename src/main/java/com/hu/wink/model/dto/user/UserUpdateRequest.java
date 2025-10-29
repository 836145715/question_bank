package com.hu.wink.model.dto.user;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户更新请求
 *


 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Long id;

    /**
     * 用户昵称
     */
    @Size(max = 50, message = "用户昵称长度不能超过50个字符")
    private String userName;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "用户头像URL长度不能超过255个字符")
    private String userAvatar;

    /**
     * 简介
     */
    @Size(max = 500, message = "用户简介长度不能超过500个字符")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @Pattern(regexp = "^(user|admin|ban)$", message = "用户角色只能是user、admin或ban")
    private String userRole;

    private static final long serialVersionUID = 1L;
}