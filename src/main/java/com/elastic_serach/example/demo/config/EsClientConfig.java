package com.elastic_serach.example.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//LocalDateTime bulk 시 objectMapper가 없기 때문에 createdAt같은 경우 직렬화를 못함
//따라서 mapper를 등록해줘야함
@Configuration
public class EsClientConfig {

    @Bean
    public ObjectMapper esObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Boot가 RestClient 빈을 이미 만들어주는 환경이라면 주입 받아서 사용
    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient, ObjectMapper esObjectMapper) {
        ElasticsearchTransport transport =
                new RestClientTransport(restClient, new JacksonJsonpMapper(esObjectMapper));
        return new ElasticsearchClient(transport);
    }
}
