package com.hu.wink.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 用户更新个人信息请求
 *


 */
@Schema(description = "用户更新个人信息请求")
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "用户昵称长度不能超过50个字符")
    private String userName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "用户头像URL长度不能超过255个字符")
    private String userAvatar;

    /**
     * 简介
     */
    @Schema(description = "用户简介", example = "这是用户简介", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 500, message = "用户简介长度不能超过500个字符")
    private String userProfile;

    private static final long serialVersionUID = 1L;
}