package com.china.soft.system.provider.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.mapper.UserTokenInfoMapper;
import com.china.soft.system.provider.dal.entity.UserTokenInfo;
import com.china.soft.system.api.service.UserTokenInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTokenInfoServiceImpl extends ServiceImpl<UserTokenInfoMapper, UserTokenInfo> implements UserTokenInfoService{

    @Override
    public UserTokenInfo getByJti(String jti) {
        return baseMapper.getByJti(jti);
    }

    @Override
    @Transactional
    public int insertSelective(UserTokenInfo record) {
        // 删除当前用户所有其他RefreshToken
        baseMapper.deleteByUserId(record.getUserId());
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(UserTokenInfo record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(UserTokenInfo record) {
        return baseMapper.updateByPrimaryKey(record);
    }
}
