package com.china.soft.rest.controller.system;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import com.china.soft.commons.utils.CryptoJSCodec;
import com.china.soft.system.provider.dal.entity.TLink;
import com.china.soft.system.api.service.TLinkService;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.china.soft.commons.utils.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.china.soft.commons.utils.response.CommonResponse.error;
import static com.china.soft.commons.utils.response.CommonResponse.success;
import static com.china.soft.commons.utils.response.GlobalErrorCodeConstants.UNKNOWN;

/**
* 友链表前端控制器
* Sun Dec 14 10:57:37 CST 2025
* @author MingYue
*/
@RestController
@RequestMapping("/link")
public class LinkController {

    /**  服务对象 */
    @Autowired
    private TLinkService tLinkService;

    @GetMapping("/list")
    @PermitAll
    public CommonResponse<List<TLink>> list(TLink entity){
        return success(tLinkService.list(entity));
    }

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("{id}")
    public CommonResponse<TLink> queryById(@PathVariable("id") Integer id) {
        return success(tLinkService.getById(id));
    }

    /**
    * 新增数据
    *
    * @param tLink 实体
    * @return 新增结果
    */
    @PostMapping(value = "/add")
    @PermitAll
    @Transactional
    public CommonResponse< Boolean> add(@RequestHeader(value = "Accept-Apply-Link") String linkToken,@RequestBody TLink tLink) {
        // @MingYue：上方都是防止被人找到瞎TM传递参数
        if(linkToken == null){
            return error(UNKNOWN);
        }
        try {
            String decrypt = CryptoJSCodec.decrypt(linkToken, "witpot-norsoz-hAvqa2");
            String[] split = decrypt.split("\\.");
            String serverSign = DigestUtils.md5Hex(split[0] + "fixed_salt");
            if(!split[1].equals(serverSign)){
                return error(UNKNOWN);
            }
        } catch (Exception e) {
            return error(UNKNOWN);
        }
        String htmlTemplate = ResourceUtil.readUtf8Str("templates/index.html");
        Map<String, String> data = new HashMap<>();
        data.put("name", tLink.getName());
        data.put("url", tLink.getUrl());
        data.put("description", tLink.getDescription());
        data.put("dateTime", DateUtil.formatDateTime(new Date()));
        data.put("email", tLink.getEmail());
        // 替换模板占位符（Hutool StrUtil 一键替换）
        String htmlContent = StrUtil.format(htmlTemplate, data);
        MailUtil.send("1161913566@qq.com", "有朋友申请友情链接啦，请及时审核",htmlContent , true);
        tLinkService.insertSelective(tLink);
        return success(true);
    }

    /**
    * 编辑数据
    *
    * @param tLink 实体
    * @return 编辑结果
    */
    @PutMapping(value = "/update")
    public CommonResponse< Boolean> edit(TLink tLink) {
        tLinkService.updateByPrimaryKeySelective(tLink);
        return success(true);
    }

    /**
    * 删除数据
    * @param id 主键
    * @return 删除是否成功
    */
    @PostMapping(value = "/delete/{id}")
    public CommonResponse< Boolean> deleteById(@PathVariable("id") Integer id) {
        tLinkService.removeById(id);
        return success(true);
    }
}
