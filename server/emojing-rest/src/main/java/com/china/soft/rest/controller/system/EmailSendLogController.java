package com.china.soft.rest.controller.system;
import com.china.soft.system.provider.dal.entity.EmailSendLog;
import com.china.soft.system.api.service.EmailSendLogService;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static com.china.soft.commons.utils.response.CommonResponse.success;

/**
 * description 邮件发送记录表前端控制器
 * @author  MingYue
 * @version 1.0
 * @since   2025/3/21
 */
@RestController
@RequestMapping("/email-send-log")
public class EmailSendLogController {

    /**  服务对象 */
    @Autowired
    private EmailSendLogService emailSendLogService;

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("{id}")
    public CommonResponse<EmailSendLog> queryById(@PathVariable("id") Integer id) {
        return success(emailSendLogService.getById(id));
    }

    /**
    * 新增数据
    *
    * @param emailSendLog 实体
    * @return 新增结果
    */
    @PostMapping(value = "/add")
    public CommonResponse< Boolean> add(EmailSendLog emailSendLog) {
        emailSendLogService.insertSelective(emailSendLog);
        return success(true);
    }

    /**
    * 编辑数据
    *
    * @param emailSendLog 实体
    * @return 编辑结果
    */
    @PutMapping(value = "/update")
    public CommonResponse< Boolean> edit(EmailSendLog emailSendLog) {
        emailSendLogService.updateByPrimaryKeySelective(emailSendLog);
        return success(true);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    public CommonResponse< Boolean> deleteById(@PathVariable("id") Integer id) {
        emailSendLogService.removeById(id);
        return success(true);
    }
}
