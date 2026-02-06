package com.china.soft.system.provider.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.mapper.ViewUserMapper;
import com.china.soft.system.provider.dal.entity.ViewUser;
import com.china.soft.system.api.service.ViewUserService;
@Service
public class ViewUserServiceImpl extends ServiceImpl<ViewUserMapper, ViewUser> implements ViewUserService{

    @Override
    public int insertSelective(ViewUser record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(ViewUser record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(ViewUser record) {
        return baseMapper.updateByPrimaryKey(record);
    }

    @Override
    public ViewUser selectByPrimaryKey(Integer id) {
        return null;
    }
}
