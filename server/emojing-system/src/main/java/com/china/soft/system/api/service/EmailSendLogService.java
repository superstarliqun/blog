package com.china.soft.system.api.service;

import com.china.soft.system.provider.dal.entity.EmailSendLog;
import com.baomidou.mybatisplus.extension.service.IService;
public interface EmailSendLogService extends IService<EmailSendLog>{

    int insertSelective(EmailSendLog record);

    int updateByPrimaryKeySelective(EmailSendLog record);

    int updateByPrimaryKey(EmailSendLog record);

}
