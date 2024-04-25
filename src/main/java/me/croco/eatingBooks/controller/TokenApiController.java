package me.croco.eatingBooks.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.config.CustomAuthenticationSuccessHandler;
import me.croco.eatingBooks.dto.AccessTokenCreateResponse;
import me.croco.eatingBooks.dto.MemberByTokenResponse;
import me.croco.eatingBooks.service.TokenService;
import me.croco.eatingBooks.util.CookieUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenCreateResponse> createNewAccessToken(HttpServletRequest request) {
        String refresh_token = CookieUtil.getCookie(CustomAuthenticationSuccessHandler.REFRESH_TOKEN_COOKIE_NAME, request);
        String newAccessToken = tokenService.createNewAccessToken(refresh_token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccessTokenCreateResponse(newAccessToken));
    }

    @GetMapping("/api/token/{accessToken}")
    public ResponseEntity<MemberByTokenResponse> findMemberByToken(@PathVariable String accessToken) {
        return ResponseEntity.ok()
                .body(tokenService.findMemberByAccessToken(accessToken));
    }
}
