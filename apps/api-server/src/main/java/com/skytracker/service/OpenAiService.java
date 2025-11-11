package com.skytracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    private static final String OPENAI_CHAT_URL = "https://api.openai.com/v1/chat/completions";

    public String ask(String message, String sessionId) {

        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", List.of(Map.of("role", "user", "content", message))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map<String, Object> response = restTemplate.postForObject(OPENAI_CHAT_URL, request, Map.class);

        if (response != null && response.containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
                if (messageMap != null) {
                    return (String) messageMap.get("content");
                }
            }
        }
        return "응답을 가져오지 못했습니다.";
    }
}
