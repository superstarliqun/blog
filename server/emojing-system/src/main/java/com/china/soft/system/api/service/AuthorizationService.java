package com.china.soft.system.api.service;

import com.china.soft.system.provider.dal.entity.SysAuthorization;

public interface AuthorizationService {

    SysAuthorization getById(int id);

    SysAuthorization getByName(String username);

}
