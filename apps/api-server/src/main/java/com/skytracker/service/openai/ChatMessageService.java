package com.skytracker.service.openai;

import com.skytracker.common.dto.enums.MessageRole;
import com.skytracker.dto.openai.ChatResponseDto;
import com.skytracker.entity.ChatMessage;
import com.skytracker.entity.ChatRoom;
import com.skytracker.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveUserMessage(ChatRoom room, String content) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(room)
                .message(content)
                .role(MessageRole.USER)
                .build();
        chatMessageRepository.save(chatMessage);
    }

    public ChatResponseDto saveAiMessage(ChatRoom room, String content) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(room)
                .message(content)
                .role(MessageRole.AI)
                .build();
        chatMessageRepository.save(chatMessage);

        return ChatResponseDto.from(chatMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDto> getRecentMessages(ChatRoom room) {
        List<ChatMessage> recentAsc =
                chatMessageRepository.findTop20ByChatRoomOrderByCreatedAtAsc(room);

        return recentAsc.stream()
                .map(ChatResponseDto::from)
                .toList();
    }

}
