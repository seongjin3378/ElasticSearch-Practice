package com.elastic_serach.example.demo.index;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Document(indexName = "articles", createIndex = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setting(shards = 1, replicas = 1, refreshInterval = "1s", settingPath = "/es/articles-settings.json")          // 위에서 만든 settings
@Mapping(mappingPath = "/es/articles-mapping.json")

public class Article {

    @Id
    private String id;



    @Field(type = FieldType.Text, analyzer = "nori", searchAnalyzer = "nori")
    private String title;

/*    @Field(type = FieldType.Keyword, name = "title.keyword")
    private String titleKeyword;*/

    @Field(type = FieldType.Integer)
    private Integer views;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS||epoch_millis")
    private LocalDateTime createdAt;


}


