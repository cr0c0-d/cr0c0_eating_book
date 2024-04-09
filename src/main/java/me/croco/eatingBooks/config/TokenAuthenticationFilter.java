package me.croco.eatingBooks.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.util.HttpHeaderChecker;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {	// OncePerRequestFilter를 상속받아, 요청당 한 번씩만 필터가 실행되도록 함

    private final HttpHeaderChecker httpHeaderChecker;

    @Override   	// OncePerRequestFilter에서 정의된 doFilterInternal 메서드 오버라이드 -> 필터 로직 구현
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        httpHeaderChecker.checkAuthorizationHeader(request);

        filterChain.doFilter(request, response);    // 다음 필터를 진행하거나 요청을 처리해라
    }
}
