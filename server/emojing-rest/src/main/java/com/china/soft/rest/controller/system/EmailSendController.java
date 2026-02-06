package com.china.soft.rest.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.china.soft.commons.utils.response.CommonResponse;
import com.china.soft.system.api.service.SysEmailConfigService;
import com.china.soft.system.provider.dal.entity.EmailSendLog;
import com.china.soft.system.provider.dal.entity.SysEmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.china.soft.commons.utils.response.CommonResponse.success;
/**
 * description 发送邮件前端控制器
 * @author  MingYue
 * @version 1.0
 * @since   2025/3/21
 */
@RestController
@RequestMapping("/email")
@Slf4j
public class EmailSendController {

    @Resource
    SysEmailConfigService emailConfigService;

    @PostMapping(value = "/send")
    public CommonResponse<Boolean> send(@RequestBody EmailSendLog param){
        SysEmailConfig entity = emailConfigService.selectByPrimaryKey(param.getConfigId());
        if(ObjectUtil.isNotNull(entity)){
            MailAccount account = BeanUtil.toBean(entity, MailAccount.class);
            String result = MailUtil.send(account, CollUtil.newArrayList(param.getRecipientEmail()), param.getSubject(), param.getBody(), false);
            log.info("发送邮件结果集：{}",result);
            return success(true);
        }
        throw new RuntimeException("发送邮件异常");
    }


}
