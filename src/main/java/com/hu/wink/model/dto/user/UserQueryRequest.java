package com.hu.wink.model.dto.user;

import com.hu.wink.common.PageRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户查询请求
 *


 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    @Size(max = 100, message = "开放平台ID长度不能超过100个字符")
    private String unionId;

    /**
     * 公众号openId
     */
    @Size(max = 100, message = "公众号OpenID长度不能超过100个字符")
    private String mpOpenId;

    /**
     * 用户昵称
     */
    @Size(max = 50, message = "用户昵称长度不能超过50个字符")
    private String userName;

    /**
     * 简介
     */
    @Size(max = 500, message = "用户简介长度不能超过500个字符")
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    @Pattern(regexp = "^(user|admin|ban)?$", message = "用户角色只能是user、admin、ban或空")
    private String userRole;

    private static final long serialVersionUID = 1L;
}