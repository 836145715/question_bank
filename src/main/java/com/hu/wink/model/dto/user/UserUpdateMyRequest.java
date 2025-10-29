package com.hu.wink.model.dto.user;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 用户更新个人信息请求
 *


 */
@Data
public class UserUpdateMyRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}