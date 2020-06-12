package com.example.csgs.service.impl;

import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.CommunityInfo;
import com.example.csgs.entity.CommunityInfoEntity;
import com.example.csgs.entity.PageQuery;
import com.example.csgs.mapper.AnnouncementMapper;
import com.example.csgs.mapper.ProfileMapper;
import com.example.csgs.service.LuceneService;
import com.example.csgs.service.ResidentService;
import com.example.csgs.utils.CalculatePageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j
@Service
public class LuceneServiceImpl implements LuceneService {
    @Resource
    RestHighLevelClient client;
    private SearchRequest searchRequest;
    private SearchSourceBuilder sourceBuilder;

    /**
     * 在ElasticSearch中复合查询
     * @param indexName 索引名
     * @param matchQueryBuilder 复合查询条件
     * @param page 页数
     * @return
     * @throws IOException
     */
    @Override
    public List<Map<String, Object>> multiMatchQuery(String indexName, MultiMatchQueryBuilder matchQueryBuilder,
                                                     String page) throws IOException {
        createSearchObject(indexName);
        sourceBuilder.from((Integer.parseInt(page) - 1) * 10);
        sourceBuilder.size(10);
        sourceBuilder.query(matchQueryBuilder);
        buildSearchRequest(); //构建SearchRequest
        setKeywordHighlight();//设置相应字段高亮
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        ArrayList<Map<String, Object>> list = null;
        if (searchResponse.getHits().getHits().length != 0) {
            list = new ArrayList<>();
            for (SearchHit documentFields : searchResponse.getHits().getHits()) {
                //替换成高亮结果
                analysisHighlight(documentFields,"content");
                analysisHighlight(documentFields,"title_name");
                list.add(documentFields.getSourceAsMap());
            }
        }
        return list;
    }

    private void analysisHighlight(SearchHit documentFields,String fieldName) {
        Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
        HighlightField field = highlightFields.get(fieldName);
        Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
        if (field != null) {
            Text[] fragments = field.fragments();
            StringBuilder newField = new StringBuilder();
            for (Text fragment : fragments) {
                newField.append(fragment);
            }
            sourceAsMap.put(fieldName,newField);
        }
    }

    private void setKeywordHighlight() {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title_name");
        highlightBuilder.field("content");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
    }

    private void buildSearchRequest() {
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
    }

    private void createSearchObject(String indexName) {
        searchRequest = new SearchRequest(indexName);
        sourceBuilder = new SearchSourceBuilder();
    }


}

