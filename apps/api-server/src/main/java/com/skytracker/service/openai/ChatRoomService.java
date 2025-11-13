package com.skytracker.service.openai;

import com.skytracker.entity.ChatRoom;
import com.skytracker.entity.User;
import com.skytracker.repository.ChatRoomRepository;
import com.skytracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoom getOrCreate(Long userId) {
        return chatRoomRepository.findByUserId(userId)
                .orElseGet(() -> create(userId));
    }

    private ChatRoom create(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

}
