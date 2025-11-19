package com.skytracker.security.auth;

import com.skytracker.service.TokenBlackListService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final TokenBlackListService tokenBlackListService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("path = {}", request.getRequestURI());
        log.info("auth = {}", request.getHeader("Authorization"));

        String token = resolveToken(request);

        if (!StringUtils.hasText(token)) {
            log.info("Jwt token is empty");
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenBlackListService.isBlackList(token)) {
            log.info("Blacklisted token: {}", token);
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.extractUsername(token);

        if (username == null) {
            log.info("Invalid token, Incorrect username : {}", username);
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (jwtUtils.isValidToken(token, userDetails.getEmail())){
            log.info("Successfully validate token");
            setAuthentication(userDetails, request);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    private void setAuthentication(CustomUserDetails customUserDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}