package com.china.soft.commons.utils.tree;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TreeEntity {
    /**
     * 节点唯一ID
     */
    private Long id;

    /**
     * 父节点ID（根节点父ID通常为0或null）
     */
    private Long parentId;

    /**
     * 层级设置方法
     */
    private Integer level;

    private LocalDateTime createTime;

    /**
     * 子节点列表
     */
    private List<? extends TreeEntity> children;
}