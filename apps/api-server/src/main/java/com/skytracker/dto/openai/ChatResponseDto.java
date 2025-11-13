package com.skytracker.dto.openai;

import com.skytracker.common.dto.enums.MessageRole;
import com.skytracker.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {

    private Long messageId;
    private String role;
    private String content;
    private LocalDateTime createdAt;

    public static ChatResponseDto from(ChatMessage message) {
        return ChatResponseDto.builder()
                .messageId(message.getId())
                .role(message.getRole().name().toLowerCase())
                .content(message.getMessage())
                .createdAt(message.getCreateTime())
                .build();
    }

}
