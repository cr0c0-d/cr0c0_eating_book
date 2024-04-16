package me.croco.eatingBooks.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.ArticleTemplate;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.*;
import me.croco.eatingBooks.service.ArticleService;
import me.croco.eatingBooks.service.MemberService;
import org.apache.coyote.Response;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ArticleApiController {

    private final ArticleService articleService;
    private final MemberService memberService;

    // 글 작성
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleAddRequest request) {
        Article savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);

    }
    // 글 조회
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id, HttpServletRequest request) {
        try {
            Article article = articleService.findById(id, request); // 글 정보
            Map<Long, String> articleTemplateMap = articleService.findTemplateMapByType(article.getArticleType());  // 글에 포함된 템플릿 정보
            Member writer = memberService.findByEmail(article.getWriter());

            boolean editableYn = articleService.findEditable(article.getWriter(), request);

            return ResponseEntity.ok()
                    .body(new ArticleResponse(article, articleTemplateMap, writer, editableYn));

        } catch (IllegalArgumentException e) {  // 글 id 오류
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } catch (AccessDeniedException e) { // 권한 없음(비공개 글)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (AuthenticationCredentialsNotFoundException e) {    // 로그인 필요
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 글 목록 조회
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleListResponse>> findAllArticles() {
        List<Article> articleList = articleService.findPublicArticles();

        List<ArticleListResponse> articleResponseList = articleList
                .stream()
                .map((article) -> new ArticleListResponse(article, memberService.findByEmail(article.getWriter()).getNickname()))
                .toList();

        return ResponseEntity.ok()
                .body(articleResponseList);
    }

    // 글 수정
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody ArticleUpdateRequest request) {
        Article article = articleService.update(id, request);

        return ResponseEntity.ok()
                .body(article);
    }

    // 글 삭제
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/articles/templates/{type}")
    public ResponseEntity<List<ArticleTemplate>> findTemplateByType(@PathVariable String type) {
        List<ArticleTemplate> articleTemplates = articleService.findTemplateByType(type);

        return ResponseEntity.ok()
                .body(articleTemplates);

    }

    @GetMapping("/api/articles/book/{isbn}")
    public ResponseEntity<List<ArticleListResponse>> findAllArticlesByIsbn(@PathVariable String isbn) {
        List<Article> articleList = articleService.findAllArticlesByIsbn(isbn);

        List<ArticleListResponse> articleResponseList = articleList
                .stream()
                .map((article) -> new ArticleListResponse(article, memberService.findByEmail(article.getWriter()).getNickname()))
                .toList();

        return ResponseEntity.ok()
                .body(articleResponseList);
    }

    @GetMapping("/api/articles/member/{id}")
    public ResponseEntity<MemberArticleFindResponse> findPublicArticlesByMember(@PathVariable Long id, HttpServletRequest request) {
        Member member = memberService.findById(id);

        List<Article> articleList = articleService.findAllArticlesByMemberId(id, request);

        boolean includePrivateArticles = articleService.findEditable(member.getEmail(), request);

        return ResponseEntity.ok()
                .body(new MemberArticleFindResponse(member, articleList, includePrivateArticles));
    }

}
