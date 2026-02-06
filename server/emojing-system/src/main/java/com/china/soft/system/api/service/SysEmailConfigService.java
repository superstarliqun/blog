package com.china.soft.system.api.service;

import com.china.soft.commons.utils.PageResult;
import com.china.soft.system.provider.dal.entity.SysEmailConfig;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysEmailConfigService extends IService<SysEmailConfig>{

    /**
     * 查询邮箱配置详情 注释：如果ID没有传递则会查询默认发送源
     * @param id 邮箱主键
     */
    SysEmailConfig selectByPrimaryKey(Integer id);

    int insertSelective(SysEmailConfig record);

    int updateByPrimaryKeySelective(SysEmailConfig record);

    int updateByPrimaryKey(SysEmailConfig record);

    PageResult<SysEmailConfig> page(SysEmailConfig entity);
}
