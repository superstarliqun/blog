package com.china.soft.system.api.service;

import com.china.soft.system.provider.dal.entity.TLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TLinkService extends IService<TLink>{

    List<TLink> list(TLink record);

    int insertSelective(TLink record);

    int updateByPrimaryKeySelective(TLink record);

    int updateByPrimaryKey(TLink record);

}
