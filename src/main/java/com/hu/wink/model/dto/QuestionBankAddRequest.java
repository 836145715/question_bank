package com.hu.wink.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题库添加请求
 *
 *
 */
@Data
public class QuestionBankAddRequest implements Serializable {

    /**
     * 标题
     */
    @NotBlank(message = "题库标题不能为空")
    @Size(max = 100, message = "题库标题长度不能超过100个字符")
    private String title;

    /**
     * 描述
     */
    @Size(max = 500, message = "题库描述长度不能超过500个字符")
    private String description;

    /**
     * 图片
     */
    @Size(max = 255, message = "图片URL长度不能超过255个字符")
    private String picture;

    /**
     * 优先级
     */
    private Integer priority;

    private static final long serialVersionUID = 1L;
}