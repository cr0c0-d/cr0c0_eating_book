package me.croco.eatingBooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.MemberAddRequest;
import me.croco.eatingBooks.dto.MemberUpdateRequest;
import me.croco.eatingBooks.repository.MemberRepository;
import me.croco.eatingBooks.util.Authorities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.transform.Result;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String DEFAULT_PROFILE_IMAGE = "https://i.ibb.co/LzfM6Mx/member1712982423627.jpg";

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
    }

    Member loginMember;

    void setLoginMember(String role) {
        Authorities authorities = Authorities.ROLE_USER;

        if (role.equals("admin")) {
            authorities = Authorities.ROLE_ADMIN;
        }

        loginMember = memberRepository.save(Member.builder()
                .nickname(role)
                .email(role+"@gmail.com")
                .password(role)
                .authorities(authorities)
                .build()
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginMember, loginMember.getPassword(), loginMember.getAuthorities()));

    }

    @DisplayName("signUp : 회원가입 테스트")
    @Test
    void signUp() throws Exception {
        // Given
        final String url = "/signup";
        final String email = "test@test.com";
        final String password = "1234";
        final String nickname = "테스트유저";
        final MemberAddRequest request = new MemberAddRequest(email, password, nickname);

        final String requestBody = objectMapper.writeValueAsString(request);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
        );

        // then
        result.andExpect(status().isCreated());

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일"));

        assertThat(bCryptPasswordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(member.getNickname()).isEqualTo(nickname);
    }

    @DisplayName("findAllAdmin : 로그인 사용자가 관리자일 때 모든 멤버 조회")
    @Test
    void findAllAdmin() throws Exception {
        // Given
        final String url = "/api/members";
        setLoginMember("admin");
        Member member = createMember();

        // when
        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].nickname").value(member.getNickname()));
        result.andExpect(jsonPath("$[1].nickname").value(loginMember.getNickname()));
    }

    @DisplayName("findAllUser : 로그인 사용자가 관리자가 아닐 때 모든 멤버 조회")
    @Test
    void findAllUser() throws Exception {
        // Given
        final String url = "/api/members";
        setLoginMember("user");

        // when
        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("findMemberByIdAdmin : 로그인 사용자가 관리자일 때 다른 멤버 조회")
    @Test
    void findMemberByIdAdmin() throws Exception{
        // Given
        final String url = "/api/members/";
        setLoginMember("admin");
        Member member = createMember();

        // When
        ResultActions result = mockMvc.perform(get(url+member.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.email").value(member.getEmail()));
    }

    @DisplayName("findMemberByIdUser : 로그인 사용자가 관리자가 아닐 때 다른 멤버 조회")
    @Test
    void findMemberByIdUser() throws Exception{
        // Given
        final String url = "/api/members/";
        setLoginMember("user");
        Member member = createMember();

        // When
        ResultActions result = mockMvc.perform(get(url+member.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isForbidden());
    }

    @DisplayName("findMemberByIdSelf : 로그인 사용자가 관리자가 아닐 때 자신 조회")
    @Test
    void findMemberByIdSelf() throws Exception{
        // Given
        final String url = "/api/members/";
        setLoginMember("user");

        // When
        ResultActions result = mockMvc.perform(get(url+loginMember.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.email").value(loginMember.getEmail()));
    }

    @DisplayName("updateMember : 사용자 정보 수정")
    @Test
    void updateMember() throws Exception {
        // Given
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final String url = "/api/members";

        Member member = createMember();

        String newPassword = "updatedPassword";
        String newNickname = "수정된 닉네임";
        String newProfileImg = "수정된 이미지";

        MemberUpdateRequest request = new MemberUpdateRequest(member.getId(), member.getEmail(), newPassword, newNickname, newProfileImg);

        String requestBody = objectMapper.writeValueAsString(request);

        // When
        ResultActions result = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // Then
        result.andExpect(status().isOk());
        Member updatedMember = memberRepository.findById(member.getId()).get();
        assertThat(bCryptPasswordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
        assertThat(updatedMember.getNickname()).isEqualTo(newNickname);
        assertThat(updatedMember.getProfileImg()).isEqualTo(newProfileImg);
    }

    // 테스트용 멤버 삽입
    Member createMember() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return memberRepository.save(
                Member.builder()
                        .email("test1@test.com")
                        .nickname("테스트 유저1")
                        .password(bCryptPasswordEncoder.encode("1234"))
                        .profileImg(DEFAULT_PROFILE_IMAGE)
                        .authorities(Authorities.ROLE_USER)
                        .build()
        );
    }
}