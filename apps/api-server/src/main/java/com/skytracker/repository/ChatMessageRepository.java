package com.skytracker.repository;

import com.skytracker.entity.ChatMessage;
import com.skytracker.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop20ByChatRoomOrderByCreateTimeAsc(ChatRoom chatRoom);
}
