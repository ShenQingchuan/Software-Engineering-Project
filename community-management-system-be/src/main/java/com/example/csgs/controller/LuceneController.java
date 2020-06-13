package com.example.csgs.controller;

import com.example.csgs.service.LuceneService;
import com.example.csgs.utils.ElasticSearchUtil;
import com.example.csgs.utils.ResultUtil;
import lombok.extern.log4j.Log4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lucene")
@Log4j
public class LuceneController {
    @Resource
    LuceneService luceneService;

    @GetMapping(value = "/{indexName}")
    public Object luceneJournal(@RequestParam String keyword, @RequestParam String page, @PathVariable String indexName) throws IOException {
        List<Map<String, Object>> mapList = luceneService.multiMatchQuery(indexName, QueryBuilders.multiMatchQuery(
                keyword, "title_name", "content"), page);
        if (mapList != null) {
            return ResultUtil.success(mapList,"全文搜索<"+ keyword +">成功!");
        }
        return ResultUtil.error("未找到相关结果！");
    }
}
