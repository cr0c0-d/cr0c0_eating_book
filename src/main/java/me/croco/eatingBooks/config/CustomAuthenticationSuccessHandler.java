package me.croco.eatingBooks.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.config.jwt.TokenProvider;
import me.croco.eatingBooks.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.domain.RefreshToken;
import me.croco.eatingBooks.repository.RefreshTokenRepository;
import me.croco.eatingBooks.service.MemberService;
import me.croco.eatingBooks.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 리프레시 토큰을 저장할 쿠키명
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    // 리프레시 토큰의 유효기간
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    // 액세스 토큰의 유효기간
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

    public static final String REDIRECT_PATH = "http://localhost:3000/search";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberService memberService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Member member;

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            member = memberService.findByEmail((String)oAuth2User.getAttributes().get("email"));
        } else {
            member = (Member) authentication.getPrincipal();
        }

        // 리프레시 토큰 생성 -> DB에 저장 -> 응답 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken); // DB에 저장
        addRefreshTokenToCookie(request, response, refreshToken);   // 응답 쿠키에 저장

        // 액세스 토큰 생성 -> 응답에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> userMap = new HashMap<>();

        userMap.put("accessToken", accessToken);
        userMap.put("nickname", member.getNickname()); // 닉네임
        userMap.put("id", "" + member.getId()); // Long 타입 아이디
        userMap.put("role", member.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0)); // 권한

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(userMap);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();

        // 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);
    }

    // 생성된 리프레시 토큰을 전달받아 데이터베이스에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);	// 기존 리프레시 토큰 삭제
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge, true);	// 새로운 리프레시 토큰 저장
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 액세스 토큰을 패스에 추가
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
