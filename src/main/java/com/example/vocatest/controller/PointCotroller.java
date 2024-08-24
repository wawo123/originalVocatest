package com.example.vocatest.controller;


import com.example.vocatest.dto.PointRequestDto;
import com.example.vocatest.entity.PointRequestEntity;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.PointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "포인트", description = "미사용")
@Slf4j
@RestController
@RequestMapping("/api/point")
public class PointCotroller {

    private final PointService pointService;

    private final UserService userService;
    public PointCotroller(PointService pointService, UserService userService){
        this.pointService = pointService;
        this.userService = userService;
    }
    @PostMapping()
    public ResponseEntity<PointRequestEntity> requestPoint(@AuthenticationPrincipal OAuth2User OAuth2User, @RequestBody PointRequestDto pointRequestDto){
        if (OAuth2User != null){
            String email = OAuth2User.getAttribute("email");
            PointRequestEntity pointRequestEntity = pointRequestDto.toEntity(email);
            pointService.savePointRequest(pointRequestEntity);
            return ResponseEntity.status(HttpStatus.OK).body(pointRequestEntity);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/view/mypointrequest") //포인트 요청 내역 조회
    public List<PointRequestEntity> showPointRequest(@AuthenticationPrincipal OAuth2User OAuth2User){
        if (OAuth2User != null){
            String email = OAuth2User.getAttribute("email");
            List<PointRequestEntity> pointRequestEntity = pointService.showPointRequest(email);
            return pointRequestEntity;
        } else{
            return null;
        }
    }

    @GetMapping("/rejectpointrequest/{id}")
    public ResponseEntity<String> rejectPointRequestStatus(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id") Long id) {
        if (oAuth2User != null && oAuth2User.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {

            Optional<PointRequestEntity> optionalPointRequest = pointService.findPointRequestEntityById(id);

            if (optionalPointRequest.isPresent()) {
                PointRequestEntity pointRequestEntity = optionalPointRequest.get();
                pointRequestEntity.setStatus("rejected");
                pointService.savePointRequest(pointRequestEntity);

                return ResponseEntity.status(HttpStatus.OK).body("거절 완료");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Point request not found.");
            }

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
    }

    @GetMapping("/approvepointrequest/{id}")
    public ResponseEntity<String> approvePointRequestStatus(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id") Long id) {
        if (oAuth2User != null && oAuth2User.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {

            Optional<PointRequestEntity> optionalPointRequest = pointService.findPointRequestEntityById(id);

            if (optionalPointRequest.isPresent()) {
                PointRequestEntity pointRequestEntity = optionalPointRequest.get();
                if ("approved".equals(pointRequestEntity.getStatus())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 승인된 상태");
                } else {
                    pointRequestEntity.setStatus("approved");
                    pointService.savePointRequest(pointRequestEntity);
                    String requestUserEmail = pointRequestEntity.getEmail();
                    int requestPoint = pointRequestEntity.getRequestPoint();
                    log.info("{}", requestPoint);

                    UserEntity userEntity = userService.findUserByEmail(requestUserEmail);
                    //userEntity.addPoint(requestPoint);
                    userService.saveUserEntity(userEntity);
                    return ResponseEntity.status(HttpStatus.OK).body("승인 완료");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Point request not found.");
            }

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
    }


}
