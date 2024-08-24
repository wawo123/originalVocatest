package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "유저 단어장 Entity")
@Entity
@Getter
@Setter
public class UserVocaListEntity { //유저가 가지고 있는 단어장

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "단어장 참조값")
    @ManyToOne
    @JoinColumn(name = "voca_id")
    private VocaListEntity vocaListEntity;

    @Schema(description = "유저 참조값")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public UserVocaListEntity(){

    }

}
