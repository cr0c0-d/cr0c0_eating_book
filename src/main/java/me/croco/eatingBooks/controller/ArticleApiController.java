package me.croco.eatingBooks.controller;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
public class ArticleApiController {

    private final ArticleService articleService;

    // 글 작성
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody ArticleAddRequest request) {
        Article savedArticle = articleService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);

    }
    // 글 조회
    // 글 수정
    // 글 삭제
}
