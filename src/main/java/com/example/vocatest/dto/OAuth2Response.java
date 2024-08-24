package com.example.vocatest.dto;


import org.springframework.stereotype.Component;

import java.util.Map;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    Map<String, Object> getAttributes();
}
