package com.hu.wink.model.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目更新请求
 *
 *
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "题目ID不能为空")
    @Min(value = 1, message = "题目ID必须大于0")
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "题目标题不能为空")
    @Size(max = 100, message = "题目标题长度不能超过100个字符")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "题目内容不能为空")
    @Size(max = 5000, message = "题目内容长度不能超过5000个字符")
    private String content;

    /**
     * 标签列表（json 数组）
     */
    @Size(max = 1000, message = "标签列表长度不能超过1000个字符")
    private List<String> tags;

    /**
     * 推荐答案
     */
    @Size(max = 5000, message = "推荐答案长度不能超过5000个字符")
    private String answer;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 题目来源
     */
    @Size(max = 100, message = "题目来源长度不能超过100个字符")
    private String source;

    /**
     * 仅会员可见（1 表示仅会员可见）
     */
    private Integer needVip;

    private static final long serialVersionUID = 1L;
}
