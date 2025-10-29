package com.hu.wink.model.dto;

import com.hu.wink.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题库查询请求
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionBankQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @Min(value = 1, message = "ID必须大于0")
    private Long id;

    /**
     * 标题
     */
    @Size(max = 100, message = "题库标题长度不能超过100个字符")
    private String title;

    /**
     * 描述
     */
    @Size(max = 500, message = "题库描述长度不能超过500个字符")
    private String description;

    /**
     * 创建用户 id
     */
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    @Pattern(regexp = "^[012]?$", message = "审核状态只能是0、1、2或空")
    private Integer reviewStatus;

    /**
     * 审核人 id
     */
    @Min(value = 1, message = "审核人ID必须大于0")
    private Long reviewerId;

    /**
     * 优先级
     */
    @Min(value = 0, message = "优先级不能小于0")
    private Integer priority;

    /**
     * 最小浏览量
     */
    @Min(value = 0, message = "最小浏览量不能小于0")
    private Integer minViewNum;

    /**
     * 最大浏览量
     */
    @Min(value = 0, message = "最大浏览量不能小于0")
    private Integer maxViewNum;

    private static final long serialVersionUID = 1L;
}