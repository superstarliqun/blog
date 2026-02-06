package com.china.soft.commons.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description="分页参数")
@Data
public class PageParam implements Serializable {

    private static final Integer PAGE_NO = 1;
    private static final Integer PAGE_SIZE = 10;

    /**
     * 每页条数 - 不分页
     * 例如说，导出接口，可以设置 {@link #pageSize} 为 -1 不分页，查询所有数据。
     */
    public static final Integer PAGE_SIZE_NONE = -1;

    @JsonIgnore
    @TableField(exist = false)
    @Schema(description = "页码，从 1 开始", hidden = true)
    private Integer pageNo = PAGE_NO;

    @JsonIgnore
    @TableField(exist = false)
    @Schema(description = "每页条数，最大值为 100", hidden = true)
    private Integer pageSize = PAGE_SIZE;

}
