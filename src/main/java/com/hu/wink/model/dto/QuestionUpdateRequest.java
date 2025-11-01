package com.hu.wink.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 题目更新请求
 *
 *
 */
@Schema(description = "题目更新请求")
@Data
public class QuestionUpdateRequest implements Serializable {


    /**
     * 题库ID数组
     */
    private List<Long> questionBankIds;

    /**
     * id
     */
    @Schema(description = "题目ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "题目ID不能为空")
    @Min(value = 1, message = "题目ID必须大于0")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "题目标题", example = "Java基础题目", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 200, message = "题目标题长度不能超过200个字符")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "题目内容", example = "以下哪个是Java的基本数据类型？", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 5000, message = "题目内容长度不能超过5000个字符")
    private String content;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表", example = "[\"Java\", \"基础\", \"选择题\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 10, message = "标签数量不能超过10个")
    private List<String> tags;

    /**
     * 推荐答案
     */
    @Schema(description = "推荐答案", example = "int, double, char, boolean", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 2000, message = "答案长度不能超过2000个字符")
    private String answer;

    /**
     * 优先级
     */
    @Schema(description = "优先级", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer priority;

    /**
     * 题目来源
     */
    @Schema(description = "题目来源", example = "网络搜集", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 100, message = "题目来源长度不能超过100个字符")
    private String source;

    /**
     * 仅会员可见（1 表示仅会员可见）
     */
    @Schema(description = "仅会员可见", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer needVip;

    private static final long serialVersionUID = 1L;
}