package com.elastic_serach.example.demo.Controller;


import com.elastic_serach.example.demo.index.Article;
import com.elastic_serach.example.demo.repo.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
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
        IndexOperations io = operations.indexOps(Article.class);
        System.out.println("exists = " + io.exists());
        System.out.println("settings = " + io.getSettings()); // analysis 포함
        System.out.println("mapping = " + io.getMapping());   // properties 확인

        return "articles 인덱스 존재 여부: "+ io.exists();
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
