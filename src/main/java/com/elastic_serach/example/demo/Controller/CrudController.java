package com.elastic_serach.example.demo.Controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.elastic_serach.example.demo.index.Article;
import com.elastic_serach.example.demo.repo.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrudController {
    private final ArticleRepository repository;
    private final ElasticsearchTemplate template;
    private final ElasticsearchClient client;
    @GetMapping("/crud-test")
    public String crudTest() {
        StringBuilder log = new StringBuilder();

        Article article = new Article();
        article.setTitle("조아무개 & 친아무개 & 임아무개 네트워크 실습 개론");

        article.setCreatedAt(LocalDateTime.now());
        article.setViews(8);

        //create
        repository.save(article);
        log.append("created: ").append(article).append("\n");

        // read : 검색엔진이라 search 쓰기 때문에 잘안씀
        Article found = repository.findById("B_2YsZgBMZuOPzEeEzle").orElse(null);
        log.append("📖 Read by Id: ").append(found).append("\n");


        // Update
        if(found != null) { //문서를 전체적으로 지웠다가 다시 색인하여 생성하기 땜에 잘 안씀
            found.setTitle("업데이트된 내용");
            repository.save(found);
            log.append("✏️ Updated: ").append(found).append("\n");
        }
        // Search (by title)
        Iterable<Article> searchResult = repository.findByTitle("업데이트된 내용");
        log.append("🔍 Search Result: ").append(searchResult).append("\n");

        // 5. Delete
        repository.deleteById("B_2YsZgBMZuOPzEeEzle");
        log.append("🗑️ Deleted: id=B_2YsZgBMZuOPzEeEzle").append("\n");


        return log.toString();
    }

    @GetMapping("/bulk-insert")
    public String bulkInsert() {
        List<IndexQuery> queries = new ArrayList<>();
        queries.add(new IndexQueryBuilder()
                .withId("1")
                .withObject(new Article("1", "제목1", 30, LocalDateTime.now()))
                .build());

        queries.add(new IndexQueryBuilder()
                .withId("2")
                .withObject(new Article("2", "제목1", 30, LocalDateTime.now()))
                .build());


        template.bulkIndex(queries, IndexCoordinates.of("articles"));
        template.indexOps(Article.class).refresh();

        Iterable<Article> searchResult = repository.findByTitle("제목");

        StringBuilder sb = new StringBuilder();
        for (Article a : searchResult) {
            sb.append(a).append("\n");
        }
        return sb.toString();
    }

    @GetMapping("/bulk-insert-new") //더 최신방식 권장
    public String bulkInsertNew() throws IOException {
        // 시간은 초 단위까지만
        LocalDateTime nowTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        // 삽입할 문서 리스트 (가변적으로 확장 가능)
        List<Article> articles = List.of(
                new Article("1", "제목1", 30, nowTime),
                new Article("2", "제목1", 30, nowTime)
        );

        // Article -> BulkOperation 변환
        List<BulkOperation> operations = articles.stream()
                .map(article -> BulkOperation.of(op -> op
                        .index(idx -> idx
                                .index("articles")
                                .id(article.getId())
                                .document(article)
                        )
                ))
                .toList();

        // BulkRequest 빌드
        BulkRequest request = BulkRequest.of(b -> b.operations(operations));

        // 실행
        client.bulk(request);

        // 결과 조회 (테스트용)
        Iterable<Article> searchResult = repository.findByTitle("제목");
        StringBuilder sb = new StringBuilder();
        for (Article a : searchResult) {
            sb.append(a).append("\n");
        }

        return sb.toString();


    }
}
