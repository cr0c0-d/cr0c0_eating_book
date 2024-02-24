package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.repository.ArticleRepository;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article save(ArticleAddRequest request) {
        return articleRepository.save(request.toEntity());
    }
}
