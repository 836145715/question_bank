package com.hu.wink.model.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 已登录用户视图（脱敏）
 *


 **/
@Data
@Schema(description = "已登录用户信息")
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String userName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg")
    private String userAvatar;

    /**
     * 用户简介
     */
    @Schema(description = "用户简介", example = "这是用户简介")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @Schema(description = "用户角色", example = "user", allowableValues = {"user", "admin", "ban"})
    private String userRole;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-01-01T12:00:00")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2023-01-01T12:00:00")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}