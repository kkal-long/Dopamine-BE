package com.mutsa.springboot_auction.global.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mutsa.springboot_auction.entity.CustomOAuth2User;
import com.mutsa.springboot_auction.entity.User;
import com.mutsa.springboot_auction.global.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * OAuth2 로그인 성공 시 실행되는 메서드
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authentication 인증 정보 (로그인한 사용자 정보 포함)
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String accessToken = jwtUtil.generateAccessToken(user.getSocialId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getSocialId());

        // 프론트엔드 redirect URL + 토큰 전달
        String redirectUrl = frontendUrl + "/oauth2/redirect"
            + "?accessToken=" + accessToken
            + "&refreshToken=" + refreshToken;

        response.sendRedirect(redirectUrl);
    }
}