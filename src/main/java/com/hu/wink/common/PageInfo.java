package com.hu.wink.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "分页信息")
public class PageInfo<T> implements Serializable {

    public PageInfo(IPage<T> page) {
        //这里如果long转int失败了会抛出ArithmeticException异常
        this.pageNum = Math.toIntExact(page.getCurrent());
        this.pageSize = Math.toIntExact(page.getSize());
        this.total = Math.toIntExact(page.getTotal());
        this.pages = Math.toIntExact(page.getPages());
        this.list = page.getRecords();
    }

    @Schema(description = "当前页码", example = "1")
    private Integer pageNum;
    @Schema(description = "每页显示条数", example = "10")
    private Integer pageSize;
    @Schema(description = "总条数")
    private Integer total;
    @Schema(description = "总页数")
    private Integer pages;
    @Schema(description = "列表数据")
    private List<T> list;

}
