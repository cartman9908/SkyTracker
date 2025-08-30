package com.skytracker.service;

import com.skytracker.core.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.skytracker.core.constants.RedisKeys.BLACKLIST_PREFIX;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final RedisService redisService;

    public void addToBlackList(String token, long expirationTime) {
        redisService.setBlackList(BLACKLIST_PREFIX + token, true, expirationTime, TimeUnit.MILLISECONDS);
    }

    public boolean isBlackList(String token) {
        return redisService.isBlackListed(BLACKLIST_PREFIX + token);
    }
}
