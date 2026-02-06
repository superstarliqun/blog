package com.china.soft.system.provider.dal.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description: 用户表
 * @author MingYue
 * @Date 2023/10/12
 */
@Data
@TableName(value = "sys_user")
public class SysUser implements Serializable {
    /**
     * 用户key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 账号状态 0正常 1警告  2封存
     */
    @TableField(value = "`state`")
    private Integer state;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 用户授权信息集合
     */
    @TableField(exist = false)
    private List<SysAuthorization> authorizations;

    private static final long serialVersionUID = 1L;
}