package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.VocaListControllerDocs;
import com.example.vocatest.dto.*;
import com.example.vocatest.entity.UserVocaListEntity;
import com.example.vocatest.entity.VocaListEntity;
import com.example.vocatest.jwt.JWTUtil;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/vocalist")

public class VocaListController implements VocaListControllerDocs {

    private final VocaService vocaService;

    private final JWTUtil jwtUtil;
    private final UserService userService;

    public VocaListController(VocaService vocaService, UserService userService, JWTUtil jwtUtil){
        this.vocaService = vocaService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<VocaListEntity> findAllVocaList(){ // 단어장의 모든 리스트를 보여주기
        List<VocaListEntity> vocaListEntity = vocaService.findAllVocaList();
        List<VocaListEntity> openedVocaListEntity = vocaService.findNoSecretVocaList(1);
        return openedVocaListEntity;
    }


    @GetMapping("/shop")
    public List<VocaListEntity> findAllPaidVocaList(){ // 유료 단어장의 모든 리스트를 보여주기
        List<VocaListEntity> vocaListEntity = vocaService.findAllVocaList();
        List<VocaListEntity> paidVocaListEntity = vocaService.findNoSecretVocaList(2);
        return paidVocaListEntity;
    }


    @GetMapping("{id}") // 선택한 단어장 보기
    public VocaListEntity findVocaListById(@PathVariable("id")Long id){
        return vocaService.findVocaListById(id);
    }


    @ResponseBody
    @PostMapping // 단어장 생성
    public VocaListEntity createVocaList(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody VocaListDto vocaListDto) {
        if (customOAuth2User != null){
            String email = customOAuth2User.getAttribute("email");
            VocaListEntity vocaListEntity = vocaListDto.createVocalistToEntity(email);
            vocaService.saveVocaList(vocaListEntity);

            UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
            userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
            userVocaListEntity.setVocaListEntity(vocaListEntity);
            vocaService.saveUserVocaList(userVocaListEntity);
            return vocaListEntity;
        } else{
            log.info("로그인 되어있지 않음.");
            return null;
        }
    }



    @PostMapping("createpaidvocalist") //유료 단어장 생성
    public ResponseEntity<VocaListEntity> createPaidVocaList(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody VocaListDto vocaListDto){
        if (oAuth2User != null && oAuth2User.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))){
            String email = oAuth2User.getAttribute("email");
            VocaListEntity vocaListEntity = vocaListDto.createPaidVocaListToEntity(vocaListDto.getTitle(), 0);
            vocaService.saveVocaList(vocaListEntity);

            UserVocaListEntity userVocaListEntity = new UserVocaListEntity();
            userVocaListEntity.setUserEntity(userService.findUserByEmail(email));
            userVocaListEntity.setVocaListEntity(vocaListEntity);
            vocaService.saveUserVocaList(userVocaListEntity);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else{
            log.info("로그인 되어있지 않거나, 권한이 없음.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }


    @PatchMapping("{id}") // 단어장 수정
    public ResponseEntity<VocaListEntity> updateVocaList(@PathVariable("id")Long id, @RequestBody VocaListDto vocaListDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        String email = customOAuth2User.getAttribute("email");
        VocaListEntity target = vocaService.findVocaListById(id);

        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        target.setAuthor(email);
        target.setTitle(vocaListDto.getTitle());

        VocaListEntity updated = vocaService.saveVocaList(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<VocaListEntity> deleteVocaList(@PathVariable("id")Long id){ // 단어장 삭제
        VocaListEntity vocaListEntity = vocaService.findVocaListById(id);
        if(vocaListEntity == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        vocaService.deleteVocaList(vocaListEntity);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @GetMapping("{id}/editsecret/open")
    public String openVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("id")Long id){
        String email = customOAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(id).getAuthor();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(id).getAuthor());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(id).setSecret(1);
            vocaService.saveVocaList(findVocaListById(id));
            log.info("공개 설정 완료 db확인");
            return("공개 설정 완료");
        } else {
            return("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }

    }

    @GetMapping("{id}/editsecret/close")
    public String closeVocaListSecret(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @PathVariable("id")Long id){
        String email = customOAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(id).getAuthor();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(id).getAuthor());

        if (email != null && email.equals(authorEmail)) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(id).setSecret(0);
            vocaService.saveVocaList(findVocaListById(id));
            return("비공개 설정 완료");
        } else {
            return("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }
    }

    @GetMapping("{id}/editsecret/paid")
    public void paidVocaListSecret(@AuthenticationPrincipal OAuth2User oAuth2User, @PathVariable("id")Long id){
        String email = oAuth2User.getAttribute("email");
        String authorEmail = vocaService.findVocaListById(id).getAuthor();
        log.info("접근하는 유저의 이메일 " + email);
        log.info("단어장 저자의 이메일 " + vocaService.findVocaListById(id).getAuthor());

        if (oAuth2User != null && oAuth2User.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
            log.info("수정 가능한 이용자");
            vocaService.findVocaListById(id).setSecret(2);
            vocaService.saveVocaList(findVocaListById(id));
            log.info("비공개 설정 완료 db확인");
        } else {
            log.info("수정 가능한 이용자가 아니거나 로그인 되어있지 않음.");
        }
    }
}
