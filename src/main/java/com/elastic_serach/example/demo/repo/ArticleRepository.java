package com.elastic_serach.example.demo.repo;

import com.elastic_serach.example.demo.index.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    Iterable<Article> findByTitle(String title);
}
