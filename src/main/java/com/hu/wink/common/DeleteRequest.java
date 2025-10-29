package com.hu.wink.common;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 删除请求
 *


 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "ID必须大于0")
    private Long id;

    private static final long serialVersionUID = 1L;
}