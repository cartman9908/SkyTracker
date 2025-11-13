package com.skytracker.service.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    private static final String OPENAI_CHAT_URL = "https://api.openai.com/v1/chat/completions";

    public String aiMessage(String message) {

        Map<String, Object> systemPrompt = Map.of(
                "role", "system",
                "content", "당신은 항공권 검색 도우미입니다. 친절하고 명확하게 답변합니다."
        );

        Map<String, Object> userMsg = Map.of(
                "role", "user",
                "content", message
        );

        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", List.of(systemPrompt, userMsg)
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