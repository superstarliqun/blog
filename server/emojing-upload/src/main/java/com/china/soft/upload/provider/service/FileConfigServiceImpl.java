package com.china.soft.upload.provider.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.china.soft.commons.utils.PageResult;
import com.china.soft.upload.core.FileClient;
import com.china.soft.upload.core.FileClientConfig;
import com.china.soft.upload.core.FileClientFactory;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.upload.provider.dal.mapper.FileConfigMapper;
import com.china.soft.upload.provider.dal.entity.FileConfig;
import com.china.soft.upload.api.service.FileConfigService;

import javax.annotation.Resource;

import static com.china.soft.upload.utils.cache.CacheUtils.buildAsyncReloadingCache;

@Service
public class FileConfigServiceImpl extends ServiceImpl<FileConfigMapper, FileConfig> implements FileConfigService{

    private static final Long CACHE_MASTER_ID = 1L;

    @Resource
    private FileClientFactory fileClientFactory;

    @Override
    public PageResult<FileConfig> getPageList(FileConfig reqV0) {
        Page<FileConfig> page = new Page<>(reqV0.getPageNo(),reqV0.getPageSize());
        List<FileConfig> pageNew = baseMapper.getPageList(page, reqV0);
        return new PageResult<>(pageNew, page.getTotal());
    }

    @Override
    public int insertSelective(FileConfig record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(FileConfig record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(FileConfig record) {
        return baseMapper.updateByPrimaryKey(record);
    }

    @Getter
    private final LoadingCache<Long, FileClient> clientCache = buildAsyncReloadingCache(10,
            new CacheLoader<Long, FileClient>() {
                @Override
                public FileClient load(Long id) {
                    FileConfig config = Objects.equals(CACHE_MASTER_ID, id) ? baseMapper.selectByMaster() : baseMapper.selectById(id);
                    if (config != null) {
                        fileClientFactory.createOrUpdateFileClient(config.getId(), config.getStorageType(), config.getConfig());
                    }
                    return fileClientFactory.getFileClient(null == config ? id : config.getId());
                }
            });

    @Override
    public FileClient getMasterFileClient() {
        return clientCache.getUnchecked(CACHE_MASTER_ID);
    }

    @Override
    public FileClient getFileClient(Long id) {
        return clientCache.getUnchecked(id);
    }
}
