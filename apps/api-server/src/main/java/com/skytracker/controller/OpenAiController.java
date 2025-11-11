package com.skytracker.controller;

import com.skytracker.dto.openai.ChatRequestDto;
import com.skytracker.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @PostMapping("/chat")
    public String ask(@RequestBody ChatRequestDto chatRequestDto) {
        return openAiService.ask(chatRequestDto.getMessage(), chatRequestDto.getSessionId());
    }

}
