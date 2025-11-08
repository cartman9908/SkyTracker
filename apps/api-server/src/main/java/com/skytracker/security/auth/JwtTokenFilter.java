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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final TokenBlackListService tokenBlackListService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePatterns = {
                "/oauth2/**",
                "/login/oauth2/**",
                "/api/flights/search",
                "/api/flights/hot-routes",
                "/api/user/refresh-token"
        };

        String path = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        return Arrays.stream(excludePatterns).anyMatch(p -> matcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String referer = request.getHeader("Referer");
        String ua = request.getHeader("User-Agent");

        String header = request.getHeader("Authorization");

        log.info("Req {} {}{} | Referer={} | UA={} | Authorization={}",
                method, uri, (query != null ? "?" + query : ""), referer, ua, header);

        log.info("Authorization Header = {}", header);

        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = header.substring(7);

        log.info("token = {}", token);

        if (tokenBlackListService.isBlackList(token)) {
            log.info("Blacklisted token: {}", token);
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.extractUsername(token);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("Invalid token, Incorrect username : {}", username);
            filterChain.doFilter(request, response);
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        if (jwtUtils.isValidToken(token, userDetails.getUsername())){
            log.info("Successfully validate token");
            setAuthentication(userDetails, request);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(CustomUserDetails customUserDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
