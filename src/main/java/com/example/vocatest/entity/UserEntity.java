package com.example.vocatest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "유저 Entity")
@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Schema(description = "유저 이름", example = "google")
    private String username;

    @Schema(description = "유저 이메일", example = "xxxx@gmail.com")
    private String email;

    @Schema(description = "역할", example = "USER")
    private String role;

    private int point;


}
