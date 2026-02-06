package com.china.soft.system.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.commons.utils.PageResult;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.mapper.SysEmailConfigMapper;
import com.china.soft.system.provider.dal.entity.SysEmailConfig;
import com.china.soft.system.api.service.SysEmailConfigService;
@Service
public class SysEmailConfigServiceImpl extends ServiceImpl<SysEmailConfigMapper, SysEmailConfig> implements SysEmailConfigService{

    @Override
    public SysEmailConfig selectByPrimaryKey(Integer id) {
        if(id == null){
            LambdaQueryWrapper<SysEmailConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysEmailConfig::getIsDefault,1);
            return baseMapper.selectOne(wrapper);
        }else{
            return baseMapper.selectById(id);
        }
    }

    @Override
    public int insertSelective(SysEmailConfig record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(SysEmailConfig record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(SysEmailConfig record) {
        return baseMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageResult<SysEmailConfig> page(SysEmailConfig pageReq) {
        Page<SysEmailConfig> page = new Page<>(pageReq.getPageNo(),pageReq.getPageSize());
        List<SysEmailConfig> pageNew = baseMapper.getPage(page, pageReq);
        return new PageResult<>(pageNew, page.getTotal());
    }


}
