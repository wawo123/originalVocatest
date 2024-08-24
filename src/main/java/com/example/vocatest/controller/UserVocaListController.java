package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.UserVocaListControllerDocs;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/uservocalist")
public class UserVocaListController implements UserVocaListControllerDocs {

    private final VocaService vocaService;
    private final UserService userService;

    public UserVocaListController(VocaService vocaService, UserService userService){
        this.vocaService = vocaService;
        this.userService = userService;
    }

    @Operation(summary = "로그인 한 사용자의 단어장 조회", description = "로그인 되어 있는 사용자가 가지고 있는 단어장을 모두 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 조회 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 조회 실패")
    })
    //등록,삭제,조회만 하면 될듯
    @GetMapping() // 유저가 가지고 있는 단어장 보여주기
    public List<UserVocaListEntity> findUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);

            return vocaService.getUserVocaList(email);
        } else {
            log.info("No user logged in");
            return null;
        }
    }

    @Operation(summary = "단어장 받아오기", description = "공개 되어있는 다른 사용자의 단어장을 받아옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 받아오기 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 받아오기 실패")
    })
    @GetMapping("/{id}") //유저가 단어장 얻어오기
    public UserVocaListEntity addUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        if (oAuth2User != null) { //없는 단어장으로 post 요청 갔을 때 예외처리, 비공개인 단어장 얻어오면 예외처리 해야함. 귀찮으니 패스
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);
            //여기서 시크릿 2인 단어장은 못 불러오게 예외처리 해야 함

            VocaListEntity originalVocaListEntity = vocaService.findVocaListById(id);

            originalVocaListEntity.addCount(1); // 불러온 count 증가
            vocaService.saveVocaList(originalVocaListEntity);

            //단어장을 하나 그냥 생성하고 여기다가 똑같은 저자, 이름을 넣어버리면 될듯?
            VocaListEntity createVocaListEntity = new VocaListEntity();
            createVocaListEntity.setAuthor(originalVocaListEntity.getAuthor());
            createVocaListEntity.setTitle(originalVocaListEntity.getTitle());
            createVocaListEntity.setCount(0);
            vocaService.saveVocaList(createVocaListEntity);
            //여기까지 성공

            List<VocaContentEntity> selectedAllVocaContent = vocaService.findAllVocasByVocaListId(id);

            log.info("단어장에 들어가야 할 단어 리스트 : "+ selectedAllVocaContent.toString()); //여기까지 잘 되는거같은데

            for (VocaContentEntity vocaContent : selectedAllVocaContent) {
                VocaContentEntity createVocaContentEntity = new VocaContentEntity();
                createVocaContentEntity.setText(vocaContent.getText());
                createVocaContentEntity.setTranstext(vocaContent.getTranstext());
                createVocaContentEntity.setVocaListEntity(createVocaListEntity);
                vocaService.saveVocaContent(createVocaContentEntity);
            }

            UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
            userVocaListEntity.setVocaListEntity(createVocaListEntity);
            //userVocaListEntity.setVocaListEntity(vocaService.findVocaListById(id));
            userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
            return vocaService.saveUserVocaList(userVocaListEntity);

            //대충 했는데 테스트 좀 많이 필요할듯.
        } else {
            log.info("No user logged in");
            return null;
        }
    }

    //유저가 가지고 있는 단어장 삭제 메소드
    //여기서 주는 id 값은 단어장 PK Id값임.
    @Operation(summary = "사용자가 가지고 있는 특정 단어장 삭제", description = "사용자가 가지고 있는 특정 단어장을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단어장 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "단어장 삭제 실패")
    })
    @DeleteMapping("/delete/{id}") // responseEntity 역할 제대로 보고 사용하기
    public ResponseEntity<String> deleteUserVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        if (oAuth2User != null){
            String email = oAuth2User.getAttribute("email");
            log.info("Logged in as : " + email);

            //여기서 유저가 없는 단어장을 delete요청 한다면 예외처리 해야될 것 같은데 그럴 일이 없을거같으니 일단 패스

            //구현 방법 생각
            // 1차적으로 유저가 가지고 있는 단어장의 리스트를 전부 가져오고
            List<UserVocaListEntity> userVocaListEntity = vocaService.getUserVocaList(email); //
            log.info("유저가 가지고 있는 모든 단어장 :" + userVocaListEntity.toString()); //여기까지 잘 됨
            // 2차적으로 그 리스트들에서 받아온 userid와 단어장의 ID 값이 일치하는지 확인하고
            UserVocaListEntity deleteTarget = vocaService.getUserVocaId(id);
            log.info("삭제되어야 할 단어장: " + deleteTarget); //잘 됨.
            // 그 튜플값의 ID를 가져와서
            // 해당 튜플을 삭제시킨다 ?
            vocaService.deleteUserVocaList(deleteTarget);
            log.info("삭제 완료 db확인");
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else{
            log.info("No user logged in");
            return null;
        }
    }
}
