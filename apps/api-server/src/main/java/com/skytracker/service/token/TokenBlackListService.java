package com.skytracker.service.token;

import com.skytracker.core.service.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.skytracker.core.constants.RedisKeys.BLACKLIST_PREFIX;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {

    private final RedisClient redisClient;

    public void addToBlackList(String token, long expirationTime) {
        redisClient.setBlackList(BLACKLIST_PREFIX + token, true, expirationTime, TimeUnit.MILLISECONDS);
    }

    public boolean isBlackList(String token) {
        return redisClient.isBlackListed(BLACKLIST_PREFIX + token);
    }
}
