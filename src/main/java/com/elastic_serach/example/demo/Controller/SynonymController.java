package com.elastic_serach.example.demo.Controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SynonymController {

    private final ElasticsearchClient es;


    @GetMapping("/check-synonym")
    public String checkSynonym() throws IOException {
        AnalyzeRequest request = AnalyzeRequest.of(a ->
                a.index("articles")
                        .analyzer("my_ko_syn")
                        .text("1. 임아무개 TV를 봄\n" +
                                "2.친아무개 같이 TV를 봄\n" +
                                "3.임남아무개 TV 얘기를 함\n" +
                                "4.이연 고향을 떠남")
        );
        AnalyzeResponse response = es.indices().analyze(request);

        StringBuilder sb = new StringBuilder();

        for (AnalyzeToken token : response.tokens()) {
            sb.append(token.token()).append(" ");
        }

        String result = sb.toString().trim();

        System.out.println(result);
        return response.toString();
    }

}
