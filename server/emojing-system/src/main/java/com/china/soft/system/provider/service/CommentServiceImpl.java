package com.china.soft.system.provider.service;

import cn.hutool.core.bean.BeanUtil;
import com.china.soft.commons.utils.IpUtil;
import com.china.soft.commons.utils.tree.TreeRecursionUtil;
import com.china.soft.dto.a.CommentDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.china.soft.system.provider.dal.mapper.CommentMapper;
import com.china.soft.system.provider.dal.entity.Comment;
import com.china.soft.system.api.service.CommentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService{

    /**
     * 通过文档的postId查询评论
     */
    @Override
    public List<CommentDto> tree(Integer postId) {
        List<Comment> comments = baseMapper.selectByPostId(postId);
        List<CommentDto> nodes = BeanUtil.copyToList(comments, CommentDto.class);
        if(!nodes.isEmpty()){
            nodes.get(0).setTotal(nodes.size());
            return TreeRecursionUtil.buildTree(nodes, 0L);
        }else{
            return new ArrayList<>();
        }

    }

    @Override
    public int insertSelective(Comment record) {
        record.setCreateTime(LocalDateTime.now());
        record.setIpAddress(IpUtil.getIpAddr());
        // 通过 https://weavatar.com 服务 获取头像
        record.setAvatar("https://weavatar.com/avatar/" + DigestUtils.sha256Hex(record.getEmail())+"?d=wavatar");
        return baseMapper.insertSelective(record);
    }
    @Override
    public int updateByPrimaryKeySelective(Comment record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int updateByPrimaryKey(Comment record) {
        return baseMapper.updateByPrimaryKey(record);
    }


}
