package me.croco.eatingBooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.ArticleTemplate;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.dto.ArticleUpdateRequest;
import me.croco.eatingBooks.repository.ArticleRepository;
import me.croco.eatingBooks.repository.ArticleTemplatesRepository;
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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleTemplatesRepository articleTemplatesRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Member loginMember;

    void setLoginMember(String role) {
        Authorities authorities = role.equals("admin") ? Authorities.ROLE_ADMIN : Authorities.ROLE_USER;

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
    public void deleteAll() {
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        articleTemplatesRepository.deleteAll();
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

        SecurityContextHolder.getContext().setAuthentication(null);

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

    @DisplayName("updateArticle : 글을 수정한다")
    @Test
    void updateArticle() throws Exception {
        // Given
        setLoginMember("user");
        Article article = articleRepository.save(testArticle(Map.of()));
        String newTitle = "newTitle";
        String newContent = "newContent";
        final String url = "/api/articles/";

        ArticleUpdateRequest request = new ArticleUpdateRequest(newTitle, newContent, article.getWriteType(), article.getPublicYn());
        String requestBody = objectMapper.writeValueAsString(request);

        // When
        ResultActions result = mockMvc.perform(put(url+article.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // Then
        result.andExpect(status().isOk());

        Article updatedArticle = articleRepository.findById(article.getId()).get();
        assertThat(updatedArticle.getTitle()).isEqualTo(newTitle);
        assertThat(updatedArticle.getContent()).isEqualTo(newContent);
    }

    @DisplayName("deleteArticle : 글을 삭제한다.")
    @Test
    void deleteArticle() throws Exception {
        // Given
        setLoginMember("user");
        Article article = articleRepository.save(testArticle(Map.of()));
        final String url = "/api/articles/";

        // When
        ResultActions result = mockMvc.perform(delete(url+article.getId()));

        // Then
        result.andExpect(status().isOk());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).isEmpty();


    }

    @DisplayName("findTemplateByType : 유형별 템플릿 조회")
    @Test
    void findTemplateByType() throws Exception {
        // Given
        final String url = "/api/articles/templates/";
        articleTemplatesRepository.save(ArticleTemplate.builder()
                .type("B")
                .num(1)
                .content("test")
                .build());
        articleTemplatesRepository.save(ArticleTemplate.builder()
                .type("A")
                .num(1)
                .content("test")
                .build());

        // When
        ResultActions result = mockMvc.perform(get(url+"B").accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.length()").value(1));
    }

    @DisplayName("findPublicArticlesByIsbn : 도서별 공개글 목록 조회")
    @Test
    void findAllArticlesByIsbn() throws Exception {
        // Given
        setLoginMember("user");
        final String url = "/api/articles/book/";
        Article isbn1Article = articleRepository.save(testArticle(Map.of("isbn", "isbn1")));
        articleRepository.save(testArticle(Map.of("isbn", "isbn2")));

        // When
        ResultActions result = mockMvc.perform(get(url+isbn1Article.getIsbn())
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.length()").value(1));
        result.andExpect(jsonPath("$[0].id").value(isbn1Article.getId()));
    }

    @DisplayName("findArticlesByMemberLoginAdmin : 로그인 사용자가 관리자일 때 다른 유저의 글 목록 조회")
    @Test
    void findArticlesByMemberLoginAdmin() throws Exception {
        // Given
        setLoginMember("user1");
        Long user1Id = loginMember.getId();
        Article publicArticle = articleRepository.save(testArticle(Map.of("publicYn", "true")));
        Article privateArticle = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        setLoginMember("admin");

        final String url = "/api/articles/member/";

        // When
        ResultActions result = mockMvc.perform(get(url + user1Id).accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.publicArticleList.length()").value(1));
        result.andExpect(jsonPath("$.privateArticleList.length()").value(1));
    }

    @DisplayName("findArticlesByMemberLoginUser : 로그인 사용자가 관리자가 아닐 때일 때 다른 유저의 글 목록 조회")
    @Test
    void findArticlesByMemberLoginUser() throws Exception {
        // Given
        setLoginMember("user1");
        Long user1Id = loginMember.getId();
        Article publicArticle = articleRepository.save(testArticle(Map.of("publicYn", "true")));
        Article privateArticle = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        setLoginMember("user2");

        final String url = "/api/articles/member/";

        // When
        ResultActions result = mockMvc.perform(get(url + user1Id).accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.publicArticleList.length()").value(1));
        result.andExpect(jsonPath("$.privateArticleList").doesNotExist());
    }

    @DisplayName("findArticlesByMemberLoginSelf : 로그인 사용자가 관리자가 아닐 때일 때 자신의 글 목록 조회")
    @Test
    void findArticlesByMemberLoginSelf() throws Exception {
        // Given
        setLoginMember("user1");
        Long user1Id = loginMember.getId();
        Article publicArticle = articleRepository.save(testArticle(Map.of("publicYn", "true")));
        Article privateArticle = articleRepository.save(testArticle(Map.of("publicYn", "false")));

        final String url = "/api/articles/member/";

        // When
        ResultActions result = mockMvc.perform(get(url + user1Id).accept(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.publicArticleList.length()").value(1));
        result.andExpect(jsonPath("$.privateArticleList.length()").value(1));
    }
}