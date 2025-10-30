package com.hu.wink.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题目添加请求
 *
 *
 */
@Schema(description = "题目添加请求")
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 标题
     */
    @Schema(description = "题目标题", example = "什么是Java？", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "题目标题不能为空")
    @Size(max = 100, message = "题目标题长度不能超过100个字符")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "题目内容", example = "请简述Java的特点", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "题目内容不能为空")
    @Size(max = 5000, message = "题目内容长度不能超过5000个字符")
    private String content;

    /**
     * 标签列表（json 数组）
     */
    @Schema(description = "标签列表", example = "[\"Java\", \"基础\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 1000, message = "标签列表长度不能超过1000个字符")
    private String tags;

    /**
     * 推荐答案
     */
    @Schema(description = "推荐答案", example = "Java是一种面向对象的编程语言...", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 5000, message = "推荐答案长度不能超过5000个字符")
    private String answer;

    /**
     * 优先级
     */
    @Schema(description = "优先级", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer priority;

    /**
     * 题目来源
     */
    @Schema(description = "题目来源", example = "原创", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 100, message = "题目来源长度不能超过100个字符")
    private String source;

    /**
     * 仅会员可见（1 表示仅会员可见）
     */
    @Schema(description = "是否仅会员可见", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer needVip;

    private static final long serialVersionUID = 1L;
}
