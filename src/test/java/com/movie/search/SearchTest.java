package com.movie.search;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import com.movie.base.utils.JsonUtil;
import com.movie.search.client.EsClient;
import com.movie.search.config.EsConfiguration;
import com.movie.search.dto.SelectParam;

public class SearchTest {
    
    private static String index = "movie";
    
    private static String type = "movie";
    
    private RestHighLevelClient client = EsConfiguration.client();
    
    @Test
    public void get()
        throws IOException {
        GetRequest request = new GetRequest(index, type, "8");
        GetResponse documentFields = client.get(request);
        System.out.println("get:" + JsonUtil.toJson(documentFields));
        
    }
    
    @Test
    public void select()
        throws IOException {
        MatchQueryBuilder builder = new MatchQueryBuilder("movie_cn_name", "毒液");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(builder);
        
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        SearchRequest request = new SearchRequest(index);
        request.types(type);
        request.source(searchSourceBuilder);
        SearchResponse search = client.search(request);
        System.out.println(JsonUtil.toJson(search.getHits().getHits()[0].getSourceAsMap()));
    }
    
    @Test
    public void query()
        throws IOException {
        SelectParam param = new SelectParam();
        param.setIndex("movie");
        param.setType("movie");
        param.setBoost(2.0f);
        List<SelectParam.Param> paramList = param.getSelectParam();
        paramList.add(new SelectParam.Param("movie_cn_name", "狮子王", false));
        paramList.add(new SelectParam.Param("movie_actor", "狮子王", false));
        List list = new EsClient().searchByKeyWord(param);
        System.out.println(JsonUtil.toJson(list));
    }
}
