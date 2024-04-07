package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.ArticleTemplate;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.dto.ArticleUpdateRequest;
import me.croco.eatingBooks.repository.ArticleRepository;
import me.croco.eatingBooks.repository.ArticleTemplatesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTemplatesRepository articleTemplatesRepository;

    // 글 작성
    public Article save(ArticleAddRequest request) {
        return articleRepository.save(request.toEntity());
    }

    // 글 조회
    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글 찾을 수 없음 : " + id));
    }

    // 글 목록 조회
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    // 글 수정
    @Transactional
    public Article update(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글 찾을 수 없음 : " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 글 삭제
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    public List<ArticleTemplate> findTemplateByType(String type) {
        return articleTemplatesRepository.findByTypeOrderByNumAsc(type);

    }

}
