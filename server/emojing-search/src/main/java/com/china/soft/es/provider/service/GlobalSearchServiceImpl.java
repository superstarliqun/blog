package com.china.soft.es.provider.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.china.soft.commons.utils.response.ServiceException;
import com.china.soft.es.api.service.GlobalSearchService;
import com.china.soft.es.provider.dal.entity.ArticleDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class GlobalSearchServiceImpl implements GlobalSearchService {

    @Resource
    ElasticsearchClient client;

    private static final String indexName = "online_article";

    /**
     * 高亮查询关键字文章
     * @param keywords 搜索关键字
     * @param pageIndex 当前页
     * @param pageSize 总页数
     */
    @Override
    public List<ArticleDocument> queryHighlight(String keywords, Integer pageIndex, Integer pageSize) {
        try {
            createIndex();
            List<ArticleDocument> resultList = new ArrayList<>();
            SearchRequest request = SearchRequest.of(s -> s.index(indexName)
                    .query(q -> q.multiMatch(mm -> mm.query(keywords).fields("contentText", "title").fuzziness("AUTO")))
                    .highlight(h -> h.preTags("<em>").postTags("</em>").fields("title", f -> f).fields("contentText", f -> f.fragmentSize(150).numberOfFragments(1)))
            );
            SearchResponse<ArticleDocument> response = client.search(request, ArticleDocument.class);
            // 设置代码高亮
            List<Hit<ArticleDocument>> hits = response.hits().hits();
            for (Hit<ArticleDocument> hit : hits) {
                ArticleDocument doc = hit.source();
                if( doc != null ){
                    doc.setHighlights(hit.highlight());
                }
                resultList.add(doc);
            }
            return resultList;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(ArticleDocument articles) {
        try {
            createIndex();
            client.index(i -> i.index(indexName).document(articles));
        }catch (Exception e){
            throw new ServiceException(501,"错误");
        }
    }

    @Override
    public ArticleDocument findById(String id) {
        try {
            createIndex();
            BoolQuery.Builder boolQuery = new BoolQuery.Builder();
            // 精确查询（例如根据日志级别精确匹配）
            boolQuery.filter(f -> f.term(t -> t.field("mysqlId").value(id)));
            SearchResponse<ArticleDocument> response = client.search(s -> s.index(indexName).query(q -> q.bool(boolQuery.build())), ArticleDocument.class );
            List<ArticleDocument> list = response.hits().hits().stream().map(hit -> {
                ArticleDocument source = hit.source();
                if (source != null) {
                    source.setId(hit.id());
                }
                return source;
            }).collect(Collectors.toList());
            return list.stream().findFirst().orElse(null);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            client.delete(d -> d.index(indexName).id(id));
        }catch (Exception e){
            throw new ServiceException(501,"错误");
        }
    }

    @Override
    public void updateById(ArticleDocument entity) {
        try {
            ArticleDocument queryEntity = findById(entity.getMysqlId());
            if(queryEntity == null){
                save(entity);
            }else{
                UpdateRequest<ArticleDocument,ArticleDocument> request = UpdateRequest.of(r -> r
                        .index(indexName)
                        .id(queryEntity.getId())
                        .doc(entity)
                );
                client.update(request,ArticleDocument.class); // 此处需确认客户端版本是否支持
            }
        } catch (Exception e) {
            log.error("更新ES失败，ID: {}", entity.getId(), e);
            throw new ServiceException(501, "更新失败");
        }
    }

    @Override
    public List<ArticleDocument> findList(String keywords, Integer pageIndex, Integer pageSize) {

        return null;
    }


    public void createIndex() {
        try {
            BooleanResponse existsResponse = client.indices().exists(b -> b.index(indexName));
            if (!existsResponse.value()) {
                Map<String, Property> properties = new HashMap<>();
                properties.put("mysqlId", new Property.Builder().keyword(k -> k).build());
                properties.put("title", new Property.Builder().text(t -> t.analyzer("ik_max_word")).build());
                properties.put("summary", new Property.Builder().text(t -> t.analyzer("ik_max_word")).build());
                properties.put("content", new Property.Builder().text(t -> t.analyzer("ik_max_word")).build());
                properties.put("contentText", new Property.Builder().text(t -> t.analyzer("ik_max_word")).build());
                properties.put("category", new Property.Builder().keyword(k -> k).build());
                properties.put("tags", new Property.Builder().keyword(k -> k).build());
                CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                        .index(indexName)
                        .mappings(m -> m.properties(properties))
                        .build();
                CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest);
                if (!createIndexResponse.acknowledged()) {
                    log.error("索引创建失败");
                    throw new RuntimeException("索引创建失败");
                }
            }
        }catch (Exception e){
            throw new RuntimeException("索引创建失败");
        }
    }
}
