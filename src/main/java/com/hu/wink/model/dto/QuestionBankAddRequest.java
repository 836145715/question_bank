package com.hu.wink.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题库添加请求
 *
 *
 */
@Schema(description = "题库添加请求")
@Data
public class QuestionBankAddRequest implements Serializable {

    /**
     * 标题
     */
    @Schema(description = "题库标题", example = "Java基础题库", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "题库标题不能为空")
    @Size(max = 100, message = "题库标题长度不能超过100个字符")
    private String title;

    /**
     * 描述
     */
    @Schema(description = "题库描述", example = "包含Java基础知识的题目", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 500, message = "题库描述长度不能超过500个字符")
    private String description;

    /**
     * 图片
     */
    @Schema(description = "题库图片URL", example = "https://example.com/image.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 255, message = "图片URL长度不能超过255个字符")
    private String picture;

    /**
     * 优先级
     */
    @Schema(description = "优先级", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer priority;

    private static final long serialVersionUID = 1L;
}