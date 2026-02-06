package com.china.soft.system.provider.service;

import com.china.soft.commons.base.LoginUser;
import com.china.soft.commons.exception.BusinessException;
import com.china.soft.commons.utils.ImageUtils;
import com.china.soft.commons.utils.UserRequestUtil;
import com.china.soft.commons.utils.response.CommonResponse;

import com.china.soft.system.api.service.ArticlesTagsService;
import com.china.soft.system.provider.dal.entity.Tag;
import com.china.soft.system.provider.dal.mapper.ArticlesTagsMapper;
import com.china.soft.upload.utils.QiniuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.china.soft.commons.utils.response.CommonResponse.success;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * @Description: 文章标签实现层
 * @author MingYue
 * @Date 2023/10/10
 */
@Service
@Slf4j
public class ArticlesTagsServiceImpl implements ArticlesTagsService {

    @Autowired
    private ArticlesTagsMapper articlesTagsMapper;

    @Resource
    private QiniuUtils qiniuUtils;

    @Override
    public CommonResponse<List<Tag>> list() {
        List<Tag> list = articlesTagsMapper.list();
        return success(list);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public CommonResponse<Boolean> save(Tag dto) {
        try {
            Tag tag = articlesTagsMapper.selectByName(dto.getTagsName());
            if (tag == null) {
                int insert = articlesTagsMapper.insert(dto);
                if (insert == 0) {
                    throw new BusinessException("文章插入标签失败！");
                }
                tag = new Tag();
                tag.setId(dto.getId());
                tag.setTagsName(dto.getTagsName());
                tag.setCreateTime(LocalDateTime.now());
            }
            tag.setPostId(dto.getPostId());
            // 同时绑定文章与标签的关系
            articlesTagsMapper.fromRelationInsert(tag);
        }catch (Exception e) {
            if (e instanceof BusinessException) {
                log.error(e.getMessage());
                throw new BusinessException(((BusinessException) e).getMsg());
            }
            log.error(e.getMessage());
            throw new BusinessException("插入文章标签关系异常");
        }
        return success(true);
    }

    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public int deleteBatchIds(Integer id) {
        try {
            List<Tag> unused = articlesTagsMapper.unused();
            List<Integer> collect = unused.stream().map(Tag::getId).collect(Collectors.toList());
            if(!collect.isEmpty()){
                articlesTagsMapper.deleteBatchIds(collect);
            }
            return 0;
        }catch (Exception e) {
            throw new BusinessException("批量删除标签异常！");
        }
    }

    @Override
    public void generateImage(String name) throws IOException, FontFormatException {
        BufferedImage nameImage = ImageUtils.generateImageWithName( name );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(nameImage, "png", outputStream);
        // 创建文件名
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestampStr = dateFormat.format(new Date(timestamp));
        String fileName = "COVER" + timestampStr + ".png";
        // 转化文件格式
        String upload = qiniuUtils.update1(new ByteArrayInputStream(outputStream.toByteArray()), fileName);
        log.info("图片生成的对象:{}",upload);

    }

    @Override
    public int fromRelationDelete(Integer postId) {
        return articlesTagsMapper.fromRelationDelete(postId);
    }

    @Override
    public List<Tag> fromRelationSelectByPostId(Integer postId) {
        return articlesTagsMapper.fromRelationSelectByPostId(postId);
    }
}
