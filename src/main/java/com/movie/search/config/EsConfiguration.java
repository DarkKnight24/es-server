package com.movie.search.config;

import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class EsConfiguration {
    private static String hosts = "47.94.157.37";
    
    private static int port = 9200;
    
    private static String schema = "http";
    
    private static ArrayList<HttpHost> hostList = null;
    
    private static int connectTimeOut = 1000;
    
    private static int socketTimeOut = 30000;
    
    private static int connectionRequestTimeOut = 500;
    
    private static int maxConnectNum = 100;
    
    private static int maxConnectPerRoute = 100;
    
    static {
        hostList = new ArrayList<>();
        String[] hostStrs = hosts.split(",");
        for (String host : hostStrs) {
            hostList.add(new HttpHost(host, port, schema));
        }
    }
    
    public static RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback()
        {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            }
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback()
        {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                return httpClientBuilder;
            }
        });
        RestHighLevelClient client = new RestHighLevelClient(builder.build());
        return client;
    }
    
    public static RestClient elasticsearchRestClient() {
        HttpHost[] httpHostArray = new HttpHost[hostList.size()];
        for (int i = 0; i < httpHostArray.length; i++) {
            httpHostArray[i] = hostList.get(i);
        }
        return RestClient.builder(httpHostArray).build();
    }
}
