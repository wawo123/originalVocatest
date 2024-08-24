package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "단어장 Entity")
@Entity
@Getter
@Setter
public class VocaListEntity { //단어장 목록

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "단어장 저자", example = "xxxx@gmail.com")
    private String author; // 단어장의 작성자

    @Schema(description = "단어장 제목", example = "English")
    private String title; // 단어장의 제목

    @Schema(description = "단어장의 sercret 값 0비공개 / 1 공개", example = "0")
    private int secret; // 0 비공개, 1 공개 , 2 판매중

    @Schema(description = "단어장을 받아간 횟수", example = "0")
    private int count; // 받아간 횟수

    @Schema(description = "단어장 가격", example = "0")
    private int price; // 가격



    // 아래부턴 생성자
    public VocaListEntity() {

    }

    public VocaListEntity(String author, String title, int price, int secret, int count) {
        this.author = author;
        this.title = title;
        this.secret = secret;
        this.count = count;
        this.price = price;
    }

    public void addCount(int count){
        this.count += count;
    }

}
