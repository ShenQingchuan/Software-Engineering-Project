package com.example.csgs.utils;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ElasticSearchUtil {
    @Resource
    RestHighLevelClient client;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private SearchRequest searchRequest;
    private SearchSourceBuilder sourceBuilder;

    /**
     * 添加索引
     */
    public void contextLoads() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("journal");
        CreateIndexResponse createIndexResponse = client
                .indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);

    }

    /**
     * 查询索引是否存在
     */
    public void existIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("journal");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    /**
     * 删除索引
     */
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("journal");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /**
     * 添加文档
     */
    public void addDocument(String indexName, Object object) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        request.source(JSON.toJSONString(object), XContentType.JSON);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }

    /**
     * 获取文档
     */
    public void getDocument(String indexName, String id) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse);
    }

    /**
     * 更新文档
     */
    public void updateDocument(Object object, String indexName, String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName, id);
        updateRequest.timeout("1s");

        updateRequest.doc(JSON.toJSONString(object), XContentType.JSON);

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    /**
     * 删除文档
     */
    public void deleteDocument(String indexName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
        deleteRequest.timeout("1s");

        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    /**
     * 批量插入文档
     */
    public <T> void bulkRequest(ArrayList<T> dataList, String indexName) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        for (int i = 0; i < dataList.size(); i++) {
            bulkRequest.add(new IndexRequest(indexName).
                    id("" + (i + 1)).//注意id的起始值
                    source(JSON.toJSONString(dataList.get(i)), XContentType.JSON)
            );
        }

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(!bulkResponse.hasFailures());
    }

    private void buildSearchRequest() {
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
    }

    private void createSearchObject(String indexName) {
        searchRequest = new SearchRequest(indexName);
        sourceBuilder = new SearchSourceBuilder();
    }

    /**
     * 索引中doc数量查询
     * @param indexName 索引名
     */
    public CountResponse countQuery(String indexName) throws IOException {
        CountRequest countRequest = new CountRequest(indexName);
        return client.count(countRequest,RequestOptions.DEFAULT);
    }

    /**
     * 文档精确查询
     */
    public SearchResponse termQuery(String indexName,TermQueryBuilder termQueryBuilder,String page) throws IOException {
        createSearchObject(indexName);
        sourceBuilder.from((Integer.parseInt(page)-1)*10);
        sourceBuilder.size(10);
        sourceBuilder.query(termQueryBuilder);
        buildSearchRequest();
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }



    /**
     * 文档一个name找多个values查询
     */
    public SearchResponse termsQuery(String indexName,TermsQueryBuilder termsQueryBuilder,String page) throws IOException {
        createSearchObject(indexName);
        sourceBuilder.from((Integer.parseInt(page)-1)*10);
        sourceBuilder.size(10);
        sourceBuilder.query(termsQueryBuilder);
        buildSearchRequest();
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 文档一个text在多个fieldNames中查询
     */
    public SearchResponse multiMatchQuery(String indexName,MultiMatchQueryBuilder multiMatchQueryBuilder,String page) throws IOException {
        createSearchObject(indexName);
//        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("秦先富","creator");//多个字段里找text
        sourceBuilder.from((Integer.parseInt(page)-1)*10);
        sourceBuilder.size(10);
        sourceBuilder.query(multiMatchQueryBuilder);
        buildSearchRequest();
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 文档所有匹配查询
     */
    public SearchResponse matchAllQuery(String indexName,String page) throws IOException {
        createSearchObject(indexName);
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery(); //匹配所有
        sourceBuilder.from((Integer.parseInt(page)-1)*10);
        sourceBuilder.size(10);
        sourceBuilder.query(matchAllQueryBuilder);
        buildSearchRequest();
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}
