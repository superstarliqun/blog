package com.china.soft.system.provider.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.mapper.EmailSendLogMapper;
import com.china.soft.system.provider.dal.entity.EmailSendLog;
import com.china.soft.system.api.service.EmailSendLogService;
@Service
public class EmailSendLogServiceImpl extends ServiceImpl<EmailSendLogMapper, EmailSendLog> implements EmailSendLogService{

    @Override
    public int insertSelective(EmailSendLog record) {
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(EmailSendLog record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(EmailSendLog record) {
        return baseMapper.updateByPrimaryKey(record);
    }
}
