package com.hu.wink.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题库更新请求
 *
 *
 */
@Data
public class QuestionBankUpdateRequest implements Serializable {

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
     * 图片
     */
    private String picture;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 优先级
     */
    private Integer priority;

    private static final long serialVersionUID = 1L;
}