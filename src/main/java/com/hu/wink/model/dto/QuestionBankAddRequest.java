package com.hu.wink.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题库添加请求
 *
 *
 */
@ApiModel(value = "QuestionBankAddRequest", description = "题库添加请求")
@Data
public class QuestionBankAddRequest implements Serializable {

    /**
     * 标题
     */
    @ApiModelProperty(value = "题库标题", example = "Java基础题库", required = true)
    @NotBlank(message = "题库标题不能为空")
    @Size(max = 100, message = "题库标题长度不能超过100个字符")
    private String title;

    /**
     * 描述
     */
    @ApiModelProperty(value = "题库描述", example = "包含Java基础知识的题目", required = false)
    @Size(max = 500, message = "题库描述长度不能超过500个字符")
    private String description;

    /**
     * 图片
     */
    @ApiModelProperty(value = "题库图片URL", example = "https://example.com/image.jpg", required = false)
    @Size(max = 255, message = "图片URL长度不能超过255个字符")
    private String picture;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级", example = "1", required = false)
    private Integer priority;

    private static final long serialVersionUID = 1L;
}