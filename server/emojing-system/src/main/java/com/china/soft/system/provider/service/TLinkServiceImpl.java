package com.china.soft.system.provider.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.mapper.TLinkMapper;
import com.china.soft.system.provider.dal.entity.TLink;
import com.china.soft.system.api.service.TLinkService;
@Service
public class TLinkServiceImpl extends ServiceImpl<TLinkMapper, TLink> implements TLinkService{

    @Override
    public List<TLink> list(TLink record) {
        if(record.getType() == null){
            record.setType("0");
        }
        return baseMapper.list(record);
    }

    @Override
    public int insertSelective(TLink record) {
        record.setType("0");
        record.setStatus("0");
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(TLink record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(TLink record) {
        return baseMapper.updateByPrimaryKey(record);
    }
}
