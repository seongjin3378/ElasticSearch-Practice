package com.elastic_serach.example.demo.Controller;


import com.elastic_serach.example.demo.index.Article;
import com.elastic_serach.example.demo.repo.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CreateIndexController {

    private final ElasticsearchOperations operations;

    private final ArticleRepository articleRepository;

    @GetMapping("/check-index")
    public String checkIndex(){
        boolean exists = operations.indexOps(Article.class).exists();

        return "articles 인덱스 존재 여부: "+ exists;
    }

    @GetMapping("/insert-article")
    public void insertArticle(){
        Article article = new Article();
        article.setTitle("symefo.nzx jong");
        article.setCreatedAt(LocalDateTime.now());
        articleRepository.save(article);


        Iterable<Article> results = articleRepository.findAll();

        for (Article result : results) {
            System.out.println(result.getId()+" "+result.getTitle()+" " + result.getCreatedAt());
        }

    }


}
