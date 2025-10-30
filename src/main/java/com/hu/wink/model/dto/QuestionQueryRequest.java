package com.hu.wink.model.dto;

import com.hu.wink.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 题目查询请求
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @Min(value = 1, message = "ID必须大于0")
    private Long id;

    /**
     * 标题
     */
    @Size(max = 200, message = "题目标题长度不能超过200个字符")
    private String title;

    /**
     * 内容
     */
    @Size(max = 5000, message = "题目内容长度不能超过5000个字符")
    private String content;

    /**
     * 标签列表（json 数组）
     */
    @Size(max = 500, message = "标签列表长度不能超过500个字符")
    private String tags;

    /**
     * 创建用户 id
     */
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核人 id
     */
    @Min(value = 1, message = "审核人ID必须大于0")
    private Long reviewerId;

    /**
     * 优先级
     */
    @Min(value = 0, message = "优先级不能小于0")
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

    /**
     * 最小浏览量
     */
    @Min(value = 0, message = "最小浏览量不能小于0")
    private Integer minViewNum;

    /**
     * 最大浏览量
     */
    @Min(value = 0, message = "最大浏览量不能小于0")
    private Integer maxViewNum;

    /**
     * 最小点赞数
     */
    @Min(value = 0, message = "最小点赞数不能小于0")
    private Integer minThumbNum;

    /**
     * 最大点赞数
     */
    @Min(value = 0, message = "最大点赞数不能小于0")
    private Integer maxThumbNum;

    /**
     * 最小收藏数
     */
    @Min(value = 0, message = "最小收藏数不能小于0")
    private Integer minFavourNum;

    /**
     * 最大收藏数
     */
    @Min(value = 0, message = "最大收藏数不能小于0")
    private Integer maxFavourNum;

    private static final long serialVersionUID = 1L;
}