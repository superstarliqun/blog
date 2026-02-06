package com.china.soft.system.api.service;

import com.china.soft.system.provider.dal.entity.ViewUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ViewUserService extends IService<ViewUser>{


    int insertSelective(ViewUser record);

    int updateByPrimaryKeySelective(ViewUser record);

    int updateByPrimaryKey(ViewUser record);

    ViewUser selectByPrimaryKey(Integer id);

}
