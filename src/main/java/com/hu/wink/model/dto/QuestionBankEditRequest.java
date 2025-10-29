package com.hu.wink.model.dto;

import lombok.Data;

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
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}