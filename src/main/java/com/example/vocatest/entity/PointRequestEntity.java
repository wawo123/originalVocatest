package com.example.vocatest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PointRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String requestContent;

    private String status;

    private int requestPoint;

    public PointRequestEntity(String email, String requestContent, int requestPoint){
        this.email = email;
        this.requestContent = requestContent;
        this.status = "waiting";
        this.requestPoint = requestPoint;
    }

    public PointRequestEntity() {
    }
}
