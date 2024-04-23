package me.croco.eatingBooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import me.croco.eatingBooks.config.jwt.JwtFactory;
import me.croco.eatingBooks.config.jwt.JwtProperties;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.domain.RefreshToken;
import me.croco.eatingBooks.dto.AccessTokenCreateRequest;
import me.croco.eatingBooks.repository.MemberRepository;
import me.croco.eatingBooks.repository.RefreshTokenRepository;
import me.croco.eatingBooks.util.Authorities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        memberRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken : 새로운 액세스 토큰 발급")
    @Test
    public void createNewAccessToken() throws Exception {
        // Given
        final String url = "/api/token";

        Member testMember = memberRepository.save(
                Member.builder()
                        .email("user@gmail.com")
                        .password("1234")
                        .nickname("테스트유저")
                        .authorities(Authorities.ROLE_USER)
                        .build()
        );

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testMember.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testMember.getId(), refreshToken));

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.ofDays(14).toSeconds());
        cookie.setHttpOnly(true);

        // When
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .cookie(cookie)
        );

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }



}