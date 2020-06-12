package com.example.csgs.service;

import com.example.csgs.entity.Announcement;
import com.example.csgs.entity.CommunityInfo;
import com.example.csgs.entity.PageQuery;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface LuceneService {

    /**
     * 在ElasticSearch中复合查询
     * @param indexName 索引名
     * @param matchQueryBuilder 复合查询条件
     * @param page 页数
     * @return
     * @throws IOException
     */
    List<Map<String,Object>> multiMatchQuery(String indexName, MultiMatchQueryBuilder matchQueryBuilder,
                                             String page) throws IOException;

}
