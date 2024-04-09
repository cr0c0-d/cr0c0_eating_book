package me.croco.eatingBooks.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.config.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpHeaderChecker {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";     // 요청 헤더에서 인증 정보를 찾기 위한 키값
    private final static String TOKEN_PREFIX = "Bearer ";           // 인증 토큰 앞에 붙는 접두사

    public boolean checkAuthorizationHeader(HttpServletRequest request) {
        // 요청 헤더에서 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값에서 접두사 Bearer 제거
        String token = getAccessToken(authorizationHeader);

        // 가져온 토큰이 유효한지 확인
        if(tokenProvider.validToken(token)) {   // 유효하다면
            Authentication authentication = tokenProvider.getAuthentication(token);     // 토큰에서 인증 객체를 가져와서
            SecurityContextHolder.getContext().setAuthentication(authentication);       // SecurityContextHolder의 SecurityContext에 인증 객체 저장
            return true;
        } else {
            return false;
        }
    }
    private String getAccessToken(String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {   // 요청 헤더의 Authorization 키의 값이 비어있지 않고, 그 값이 접두사 Bearer로 시작하는 경우에만
            return authorizationHeader.substring(TOKEN_PREFIX.length());    // 접두사 Bearer 를 제거하고 반환
        }
        return null;
    }
}
