package com.example.csgs;

import com.alibaba.fastjson.JSON;
import com.example.csgs.entity.User;
import com.example.csgs.pojo.JournalEs;
import lombok.extern.java.Log;
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
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Log
@SpringBootTest
class CsgsApplicationTests {
    @Resource
    RestHighLevelClient client;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    /**
     * 添加索引
     */
    @Test
    void contextLoads() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("communityInfo");
        CreateIndexResponse createIndexResponse = client
                .indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);

    }

    /**
     * 查询索引是否存在
     */
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("user");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    /**
     * 删除索引
     */
    @Test
    void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("user");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /**
     * 添加文档
     */
    @Test
    void testAddDocument() throws IOException {
        JournalEs journal = new JournalEs(1, "融泽嘉园离奇失踪五人", "调查走访", "唐梦予", dateFormat.format(new Date()));
        IndexRequest request = new IndexRequest("user");
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        request.source(JSON.toJSONString(journal), XContentType.JSON);

        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }

    /**
     * 获取文档
     */
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("user", "1");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse);
    }

    /**
     * 更新文档
     */
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("user", "1");
        updateRequest.timeout("1s");

        JournalEs journal = new JournalEs(1, "融泽嘉园离奇失踪五人", "调查走访", "唐梦予", dateFormat.format(new Date()));
        updateRequest.doc(JSON.toJSONString(journal), XContentType.JSON);

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    /**
     * 删除文档
     */
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("user", "1");
        deleteRequest.timeout("1s");

        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
    }

    /**
     * 批量插入文档
     */
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        ArrayList<User> userArrayList = new ArrayList<>();

        userArrayList.add(new User(1L,"510723200011044654","Robin","19980492664","昌平区","融泽嘉园"));
        userArrayList.add(new User(2L,"510723200011044655","Tmy","19980492665","昌平区","融泽嘉园"));
        userArrayList.add(new User(3L,"510723200011044656","Sby","19980492666","昌平区","融泽嘉园"));
        userArrayList.add(new User(4L,"510723200011044657","Hp","19980492667","昌平区","融泽嘉园"));

        for (int i = 0; i < userArrayList.size(); i++) {
            bulkRequest.add(new IndexRequest("user").
                    id("" + (i + 1)).//注意id的起始值
                    source(JSON.toJSONString(userArrayList.get(i)), XContentType.JSON)
            );
        }
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(!bulkResponse.hasFailures());
    }

    /**
     * 文档查询
     */
    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("creator", "秦先富");//精确查询
//        MultiMatchQueryBuilder termQueryBuilder = QueryBuilders.multiMatchQuery("秦先富","creator");//多个字段里找text
        TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery("userid","510722199901032661","510722199901032662");//一个字段里找多个值
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery(); //匹配所有
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(JSON.toJSONString(searchResponse.getHits()));
//        System.out.println("===========================");
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }
    }

}
