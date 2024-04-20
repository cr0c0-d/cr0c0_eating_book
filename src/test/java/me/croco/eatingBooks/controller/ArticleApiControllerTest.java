package me.croco.eatingBooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.repository.ArticleRepository;
import me.croco.eatingBooks.repository.MemberRepository;
import me.croco.eatingBooks.util.Authorities;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    public void deleteArticlesAll() {
        articleRepository.deleteAll();
    }

    @BeforeEach
    public void deleteMembersAll() {
        memberRepository.deleteAll();
    }

    public Article testArticle(Map<String, String> param) {
        return Article.builder()
                    .title("title")
                    .content("content")
                    .writer(param.getOrDefault("writer", loginMember.getEmail()))
                    .isbn(param.getOrDefault("isbn", "testIsbn"))
                    .bookTitle("bookTitle")
                    .writeType("templates")
                    .articleType(param.getOrDefault("articleType", "B"))
                    .publicYn(param.getOrDefault("publicYn", "true"))
                .build();
    }

    @DisplayName("addArticle : 글 추가")
    @Test
    void addArticle() throws Exception {
        // Given
        setLoginMember("user");
        final String url = "/api/articles";

        String title = "title test";
        String content = "content test";
        String writer = loginMember.getEmail();
        String isbn = "isbn test";
        String bookTitle = "bookTitle test";
        String writeType = "templates";
        String articleType = "B";
        String publicYn = "true";

        ArticleAddRequest request = new ArticleAddRequest(
          title, content, writer, isbn, bookTitle, writeType, articleType, publicYn
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // Then
        result.andExpect(status().isCreated());
        Article article = articleRepository.findAll().get(0);

        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
        assertThat(article.getWriter()).isEqualTo(writer);
        assertThat(article.getIsbn()).isEqualTo(isbn);
        assertThat(article.getBookTitle()).isEqualTo(bookTitle);
        assertThat(article.getWriteType()).isEqualTo(writeType);
        assertThat(article.getArticleType()).isEqualTo(articleType);
        assertThat(article.getPublicYn()).isEqualTo(publicYn);

    }
    @DisplayName("findPublicArticleLoginAdmin : 로그인 사용자가 관리자가 아닐 때 공개글 조회")
    @Test
    void findPublicArticleLoginAdmin() throws Exception {
        // Given
        setLoginMember("admin");
        Article article = articleRepository.save(testArticle(Map.of("publicYn", "true")));

        final String url = "/api/articles/";

        // When
        ResultActions resultActions = mockMvc.perform(get(url+article.getId()).accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.title").value(article.getTitle()));
        resultActions.andExpect(jsonPath("$.content").value(article.getContent()));
    }
    @DisplayName("findPublicArticleLoginUser : 로그인 사용자가 관리자가 아닐 때 공개글 조회")
    @Test
    void findPublicArticleLoginUser() throws Exception {
        // Given
        setLoginMember("user");
        Article article = articleRepository.save(testArticle(Map.of("publicYn", "true")));

        final String url = "/api/articles/";

        // When
        ResultActions resultActions = mockMvc.perform(get(url+article.getId()).accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.title").value(article.getTitle()));
        resultActions.andExpect(jsonPath("$.content").value(article.getContent()));
    }

    @DisplayName("findPrivateArticleLoginAdmin : 로그인 사용자가 관리자일 때 비공개글 조회")
    @Test
    void findPrivateArticleLoginAdmin() throws Exception {
        // Given
        setLoginMember("admin");
        Article article = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        final String url = "/api/articles/";

        // When
        ResultActions resultActions = mockMvc.perform(get(url+article.getId()).accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.title").value(article.getTitle()));
        resultActions.andExpect(jsonPath("$.content").value(article.getContent()));
    }

    @DisplayName("findPrivateArticleLoginUser : 로그인 사용자가 관리자가 아닐 때 타인의 비공개글 조회")
    @Test
    void findPrivateArticleLoginUser() throws Exception {
        // Given
        setLoginMember("user");
        Article article = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        setLoginMember("user1");

        final String url = "/api/articles/";

        // When
        ResultActions resultActions = mockMvc.perform(get(url+article.getId()).accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @DisplayName("findPrivateArticleLoginSelf : 로그인 사용자가 관리자가 아닐 때 자신의 비공개글 조회")
    @Test
    void findPrivateArticleLoginSelf() throws Exception {
        // Given
        setLoginMember("user");
        Article article = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        final String url = "/api/articles/";

        // When
        ResultActions resultActions = mockMvc.perform(get(url+article.getId()).accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.title").value(article.getTitle()));
        resultActions.andExpect(jsonPath("$.content").value(article.getContent()));
    }

    @DisplayName("findPrivateArticleLoginNone : 로그인 상태가 아닐 때 비공개글 조회")
    @Test
    void findPrivateArticleLoginNone() throws Exception {
        // Given
        setLoginMember("user");
        Article article = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        SecurityContextHolder.clearContext();

        final String url = "/api/articles/";

        // When
        ResultActions resultActions = mockMvc.perform(get(url+article.getId()).accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @DisplayName("findAllPublicArticles : 공개 글 목록을 조회한다")
    @Test
    void findAllPublicArticles() throws Exception {
        // Given
        setLoginMember("user");
        for(int i = 0; i < 3; i++) {
            articleRepository.save(testArticle(Map.of("publicYn", "true")));
            articleRepository.save(testArticle(Map.of("publicYn", "false")));
        }

        final String url = "/api/articles";

        // When
        ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void updateArticle() {
    }

    @Test
    void deleteArticle() {
    }

    @Test
    void findTemplateByType() {
    }

    @Test
    void findAllArticlesByIsbn() {
    }

    @Test
    void findPublicArticlesByMember() {
    }
}