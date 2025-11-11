package com.skytracker.security.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements Oauth2UserInfo{

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("provider_id");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = getKakaoAccount(attributes);

        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = getKakaoAccount(attributes);

        return (String) kakaoAccount.get("name");
    }

    private Map<String, Object> getKakaoAccount(Map<String, Object> attributes) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
        };

        Object kakaoAccount = attributes.get("kakao_account");

        return objectMapper.convertValue(kakaoAccount, typeRef);
    }
}