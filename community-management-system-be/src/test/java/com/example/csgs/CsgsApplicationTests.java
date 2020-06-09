package com.example.csgs;

import lombok.extern.java.Log;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@Log
@SpringBootTest
class CsgsApplicationTests {

    @Resource
    RestHighLevelClient restHighLevelClient;
    @Test
    void contextLoads() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("boss");
        CreateIndexResponse createIndexResponse = restHighLevelClient
                .indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);

    }

}
