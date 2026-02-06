package com.china.soft.upload.api.service;

import com.china.soft.commons.utils.PageResult;
import com.china.soft.upload.core.FileClient;
import com.china.soft.upload.provider.dal.entity.FileConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;

public interface FileConfigService extends IService<FileConfig>{

    /**
     * 获取上传源配置文件
     * @param reqV0 查询入参
     */
    PageResult<FileConfig> getPageList(@Valid FileConfig reqV0);

    int insertSelective(FileConfig record);

    int updateByPrimaryKeySelective(FileConfig record);

    int updateByPrimaryKey(FileConfig record);

    /**
     * 获取当前连接的文件客户端
     */
    FileClient getMasterFileClient();

    /**
     * 获取指定文件客户端
     */
    FileClient getFileClient(Long id);

}
