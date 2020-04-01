package com.movie.search.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import com.movie.search.config.EsConfiguration;
import com.movie.search.dto.SelectParam;

public class EsClient {
    
    private RestHighLevelClient client = EsConfiguration.client();
    
    public List searchByKeyWord(SelectParam param)
        throws IOException {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        getBoolQueryBuilder(builder, param);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        sourceBuilder.highlighter(new HighlightBuilder());
        SearchRequest request = new SearchRequest(param.getIndex());
        request.types(param.getType());
        request.source(sourceBuilder);
        SearchResponse search = client.search(request);
        SearchHit[] hits = search.getHits().getHits();
        ArrayList<SearchHit> searchHits = new ArrayList<>();
        searchHits.addAll(Arrays.asList(hits));
        
        return searchHits.stream().map(SearchHit::getSourceAsMap).collect(Collectors.toList());
    }
    
    private void getBoolQueryBuilder(BoolQueryBuilder builder, SelectParam param) {
        List<SelectParam.Param> selectParams = param.getSelectParam();
        selectParams.forEach(p -> {
            if (p.isMust()) {
                builder.must(new MatchQueryBuilder(p.getField(), p.getKeyWord()));
            }
            else if (!(p.isMust())) {
                builder.should(new MatchQueryBuilder(p.getField(), p.getKeyWord()));
            }
        });
        List<SelectParam.Param> filterParams = param.getFilterParam();
    }
}
