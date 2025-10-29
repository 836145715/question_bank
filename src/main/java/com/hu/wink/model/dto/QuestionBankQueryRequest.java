package com.hu.wink.model.dto;

import com.hu.wink.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 最小浏览量
     */
    private Integer minViewNum;

    /**
     * 最大浏览量
     */
    private Integer maxViewNum;

    private static final long serialVersionUID = 1L;
}