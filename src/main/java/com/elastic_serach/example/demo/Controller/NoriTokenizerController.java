package com.elastic_serach.example.demo.Controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoriTokenizerController {

    private final ElasticsearchClient es;

    @GetMapping("/check-my-analyzer")
    public String checkMyAnalyzer() throws Exception {
        AnalyzeResponse res = es.indices().analyze(a ->
                a.index("articles")
                        .analyzer("my_ko")
                        .text("세종시는 특별 자치시입니다.")
        );

        System.out.println("=== tokens ===");
        res.tokens().forEach(t ->
                System.out.printf("%s [%d,%d]%n", t.token(), t.startOffset(), t.endOffset())
        );

        return res.toString();
    }
}
