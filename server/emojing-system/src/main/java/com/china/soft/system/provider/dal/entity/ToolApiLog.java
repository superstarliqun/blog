package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName(value = "online_blog_db.tool_api_log")
public class ToolApiLog implements Serializable {
    /**
     * 唯一标识
     */
    @TableField(value = "unique_code")
    @Size(max = 255,message = "唯一标识最大长度要小于 255")
    private String uniqueCode;

    /**
     * 存储定量数据
     */
    @TableField(value = "json")
    private String json;

    /**
     * 删除标志
     */
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    private static final long serialVersionUID = 1L;
}