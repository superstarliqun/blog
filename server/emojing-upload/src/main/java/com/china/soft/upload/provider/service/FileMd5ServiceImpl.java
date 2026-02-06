package com.china.soft.upload.provider.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.commons.utils.PageResult;
import com.china.soft.upload.provider.dal.entity.FileConfig;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.upload.provider.dal.entity.SysFileMd5;
import com.china.soft.upload.provider.dal.mapper.SysFileMd5Mapper;
import com.china.soft.upload.api.service.FileMd5Service;

import java.util.List;

@Service
public class FileMd5ServiceImpl extends ServiceImpl<SysFileMd5Mapper, SysFileMd5> implements FileMd5Service {

    @Override
    public PageResult<SysFileMd5> getPageList(SysFileMd5 reqV0) {
        Page<SysFileMd5> page = new Page<>(reqV0.getPageNo(),reqV0.getPageSize());
        List<SysFileMd5> pageNew = baseMapper.getPageList(page, reqV0);
        return new PageResult<>(pageNew, page.getTotal());
    }

    @Override
    public int insertSelective(SysFileMd5 record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(SysFileMd5 record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(SysFileMd5 record) {
        return baseMapper.updateByPrimaryKey(record);
    }
}
