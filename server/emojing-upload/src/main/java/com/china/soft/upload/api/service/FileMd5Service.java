package com.china.soft.upload.api.service;

import com.china.soft.commons.utils.PageResult;
import com.china.soft.upload.provider.dal.entity.SysFileMd5;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;

public interface FileMd5Service extends IService<SysFileMd5>{

    /**
     * 获取文件唯一标识
     * @param reqV0 查询入参
     */
    PageResult<SysFileMd5> getPageList(@Valid SysFileMd5 reqV0);

    int insertSelective(SysFileMd5 record);

    int updateByPrimaryKeySelective(SysFileMd5 record);

    int updateByPrimaryKey(SysFileMd5 record);


}
