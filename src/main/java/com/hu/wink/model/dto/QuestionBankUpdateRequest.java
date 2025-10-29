package com.hu.wink.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "题库ID不能为空")
    @Min(value = 1, message = "题库ID必须大于0")
    private Long id;

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
     * 编辑时间
     */
    private Date editTime;

    /**
     * 优先级
     */
    private Integer priority;

    private static final long serialVersionUID = 1L;
}