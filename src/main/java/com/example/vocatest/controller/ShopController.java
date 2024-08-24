package com.example.vocatest.controller;

import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "상점", description = "미사용")
@Slf4j
@RestController
@RequestMapping("/api/shop")
public class ShopController {

    private final VocaService vocaService;
    private final UserService userService;

    public ShopController(VocaService vocaService, UserService userService){
        this.vocaService = vocaService;
        this.userService = userService;
    }

//    @Operation(summary = "미사용", description = "미사용")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "미사용"),
//            @ApiResponse(responseCode = "400", description = "미사용")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<?> buyPaidVocaList(@PathVariable("id")Long id, @AuthenticationPrincipal OAuth2User oAuth2User){
//        if (oAuth2User != null){
//            String email = oAuth2User.getAttribute("email");
//            UserEntity loggedUser = userService.findUserByEmail(email);
//            VocaListEntity selectedVocaListEntity = vocaService.findVocaListById(id);
//            if(selectedVocaListEntity.getSecret() == 2){
//                if(loggedUser.getPoint() >= selectedVocaListEntity.getPrice()){
//                    loggedUser.subPoint(selectedVocaListEntity.getPrice());
//                    userService.saveUserEntity(loggedUser);
//
//                    selectedVocaListEntity.addCount(1); // 구매한 count 증가
//                    vocaService.saveVocaList(selectedVocaListEntity); //저장
//
//
//                    VocaListEntity createVocaListEntity = new VocaListEntity();//단어장 만들기
//                    createVocaListEntity.setAuthor(selectedVocaListEntity.getAuthor());
//                    createVocaListEntity.setTitle(selectedVocaListEntity.getTitle());
//                    createVocaListEntity.setCount(0);
//                    createVocaListEntity.setSecret(2);// 강제로 2 설정해서 공유 못 하게
//                    vocaService.saveVocaList(createVocaListEntity);
//
//                    List<VocaContentEntity> selectedAllVocaContent = vocaService.findAllVocasByVocaListId(id); // 단어 만들기
//                    for (VocaContentEntity vocaContent : selectedAllVocaContent) {
//                        VocaContentEntity createVocaContentEntity = new VocaContentEntity();
//                        createVocaContentEntity.setText(vocaContent.getText());
//                        createVocaContentEntity.setTranstext(vocaContent.getTranstext());
//                        createVocaContentEntity.setVocaListEntity(createVocaListEntity);
//                        vocaService.saveVocaContent(createVocaContentEntity);
//                    }
//                    UserVocaListEntity userVocaListEntity = new UserVocaListEntity();//유저 단어장 추가
//                    userVocaListEntity.setVocaListEntity(createVocaListEntity);
//                    userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
//                    vocaService.saveUserVocaList(userVocaListEntity);
//                    return ResponseEntity.status(HttpStatus.OK).body("구매 완료");
//                } else {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트 부족. 현재 유저가 가지고 있는 포인트 : " + loggedUser.getPoint());
//                }
//            } else{
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("판매 중인 단어장이 아님.");
//            }
//        } else{
//            log.info("No user logged in");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
//        }
//    }
}
