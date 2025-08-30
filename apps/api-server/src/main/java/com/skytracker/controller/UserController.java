package com.skytracker.controller;

import com.skytracker.security.auth.JwtUtils;
import com.skytracker.service.TokenBlackListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final TokenBlackListService tokenBlackListService;
    private final JwtUtils jwtUtil;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            long expiration = jwtUtil.extractAllClaims(accessToken).getExpiration().getTime() - System.currentTimeMillis();

            tokenBlackListService.addToBlackList(accessToken, expiration);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("로그아웃 완료");
    }

    /**
     * accessToken 재발금
     */

    @PostMapping("/refresh-toekn")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token failed");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(null);

        if (refreshToken == null || jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        }

        if (tokenBlackListService.isBlackList(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Blacklist refresh token expired");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateToken(username);

        return ResponseEntity.ok("{\"newAccessToken\": \"" + newAccessToken + "\"}");
    }
}
