package com.example.vocatest.config;

import com.example.vocatest.dto.OAuth2Response;
import com.example.vocatest.jwt.JwtFilter;
import com.example.vocatest.jwt.JWTUtil;
import com.example.vocatest.oauth2.CustomSuccessHandler;
import com.example.vocatest.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration // 시큐리티에 얘가 config 임을 등록
@EnableWebSecurity // 시큐리티도 등록
public class SecurityConfig {

    private final UserService userService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;


    public SecurityConfig(UserService custom0Auth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil){
        this.userService = custom0Auth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf.disable());

        http
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll());


        http
                .httpBasic((basic) -> basic.disable());


        http
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(userService))
                        .successHandler(customSuccessHandler)
                );


        http
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll());

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("*"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("*"));

                        return configuration;
                    }
                }));

        return http.build();
    }
}
