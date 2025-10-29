package com.hu.wink.common;

import com.hu.wink.constant.CommonConstant;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 分页请求
 *


 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    @Min(value = 1, message = "当前页号必须大于0")
    private int current = 1;

    /**
     * 页面大小
     */
    @Min(value = 1, message = "页面大小必须大于0")
    @Max(value = 20, message = "页面大小不能超过20")
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
