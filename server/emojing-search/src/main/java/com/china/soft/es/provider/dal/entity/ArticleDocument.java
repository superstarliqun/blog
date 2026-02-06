package com.china.soft.es.provider.dal.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * elasticSearch索引 ArticleDocument
 * @author  MingYue
 * @version 1.0
 * @since   2025/3/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDocument {

    /**
     * 文章标题
     * 中文分词设置，前提是您的es已经安装了中文分词ik插件
     * 中文分词有两种形式：
     * ik_max_word：会将文本做最细粒度的拆分
     * ik_smart：会将文本做最粗粒度的拆分
     */

    private String id;

    private String mysqlId;

    /** 文章标题 */
    private String title;

    /** 摘要 */
    private String summary;

    /** 内容标记 */
    private String content;

    /** 去掉markdown标记的文本 */
    private String contentText;

    /** 分类 */
    private String category;

    /** 标签*/
    private String tags;

    /** 用来封装高亮的结果*/
    private Map<String, List<String>> highlights;

}
