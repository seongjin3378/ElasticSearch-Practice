package com.elastic_serach.example.demo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class EsConfig /*extends ElasticsearchConfiguration*/ {

/*    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("https://localhost:9200")        // 호스트:포트
                .usingSsl("32:5D:4E:23:CE:20:39:F5:A8:9A:31:F4:CC:85:83:7A:C1:60:BA:37:91:C9:A2:ED:E9:2E:4B:14:21:3C:5B:DB")   // ← 여기 fingerprint
                .withBasicAuth("elastic", "123456")
                .build();
    }*/
}
