package com.example.vocatest.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDto userDto;

    public CustomOAuth2User(UserDto userDto) {
        this.userDto = userDto;
        System.out.println("CustomOAuth2User 생성: username: " + userDto.getName());
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", userDto.getName());
        attributes.put("username", userDto.getUsername());
        attributes.put("email", userDto.getEmail());
        attributes.put("role", userDto.getRole());
        return attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() { // 왜 이건 안됨 ?
        return userDto.getName();
    }

    public String getEmail() {
        return userDto.getEmail();
    }

    public String getUsername() { //이건되는데
        return userDto.getUsername();
    }

}