package com.skytracker.handler;

import com.skytracker.security.auth.CustomUserDetails;
import com.skytracker.security.auth.JwtUtils;
import com.skytracker.service.TokenBlackListService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final TokenBlackListService tokenBlackListService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = customUserDetails.getEmail();

        String accessToken = jwtUtils.generateToken(email);
        String refreshToken = jwtUtils.generateRefreshToken(email);

        if(tokenBlackListService.isBlackList(accessToken)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"accessToken\": \"" + accessToken + "\"" + ", "
                + "\"refreshToken\": \"" + refreshToken + "\"}");
    }
}