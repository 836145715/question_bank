package com.hu.wink.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题库编辑请求（审核相关）
 *
 *
 */
@Data
public class QuestionBankEditRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "题库ID不能为空")
    @Min(value = 1, message = "题库ID必须大于0")
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    @NotNull(message = "审核状态不能为空")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @Size(max = 255, message = "审核信息长度不能超过255个字符")
    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}