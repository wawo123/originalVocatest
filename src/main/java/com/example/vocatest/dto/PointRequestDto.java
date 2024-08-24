package com.example.vocatest.dto;

import com.example.vocatest.entity.PointRequestEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRequestDto {
    private String requestContent; // 클라이언트에서 받을 타입들이 두개라는 거 임
    private int requestPoint;

    public PointRequestEntity toEntity(String email){
        return new PointRequestEntity(email, requestContent, requestPoint);// 이게 post요청 했을 때 데이터 들어가는거네
    }
}
