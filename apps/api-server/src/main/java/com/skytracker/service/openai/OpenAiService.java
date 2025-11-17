package com.skytracker.service.openai;

import com.skytracker.dto.openai.ChatResponseDto;
import com.skytracker.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OpenAiService {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final OpenAiClient openAiClient;

    public ChatResponseDto ask(String userMessage, Long userId) {

        ChatRoom chatRoom = chatRoomService.getOrCreate(userId);
        chatMessageService.saveUserMessage(chatRoom, userMessage);

        String aiMessage = openAiClient.aiMessage(userMessage);
        ChatResponseDto chatResponseDto = chatMessageService.saveAiMessage(chatRoom, aiMessage);

        return chatResponseDto;

    }
}
