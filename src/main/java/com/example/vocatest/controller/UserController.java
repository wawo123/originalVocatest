package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.UserControllerDocs;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping
    public List<UserEntity> findAllUser(){
       return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity findUserById(@PathVariable("id")Long id){ // 일케 안 하면 오류남.
        return userService.findUserById(id);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id")Long id){
        UserEntity userEntity = userService.findUserById(id);
        if (userEntity == null){
            return "단어장을 모두 삭제하고 탈퇴해 주세요.";
        }
        userService.delete(userEntity);
        return "탈퇴 완료";
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        return ResponseEntity.ok("Logged out successfully");
    }

}
