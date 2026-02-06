package com.china.soft.system.provider.service;

import com.china.soft.system.api.service.AuthorizationService;
import com.china.soft.system.provider.dal.entity.SysAuthorization;
import com.china.soft.system.provider.dal.mapper.SysAuthorizationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Resource
    private SysAuthorizationMapper sysAuthorizationMapper;

    @Override
    public SysAuthorization getById(int id) {
        return sysAuthorizationMapper.selectById(id);
    }

    @Override
    public SysAuthorization getByName(String username) {
       return sysAuthorizationMapper.selectByName(username);
    }
}
