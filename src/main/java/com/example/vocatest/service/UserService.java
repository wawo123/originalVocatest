package com.example.vocatest.service;

import com.example.vocatest.dto.CustomOAuth2User;
import com.example.vocatest.dto.UserDto;
import com.example.vocatest.dto.GoogleResponse;
import com.example.vocatest.dto.OAuth2Response;
import com.example.vocatest.entity.UserEntity;
import com.example.vocatest.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends DefaultOAuth2UserService {



    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            UserDto userDTO = new UserDto();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            UserDto userDTO = new UserDto();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }


    public List<UserEntity> findAllUsers() {
            return userRepository.findAll();
        }

        public UserEntity findUserById(Long id) {
            return userRepository.findById(id).orElse(null);
        }

        public void delete(UserEntity userEntity) { //반환 해 줄 이유가 없는듯.
            userRepository.delete(userEntity);
        }

        public UserEntity findUserByEmail(String email) {
            return userRepository.findByemail(email);
        }

        public UserEntity saveUserEntity(UserEntity userEntity) {
            return userRepository.save(userEntity);
        }
    }
