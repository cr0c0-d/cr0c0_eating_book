package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Article;

import java.util.List;

public interface ArticleQueryDSLRepository {

    List<Article> findPublicArticlesByIsbnOrderByCreatedAtDesc(String isbn);

    List<Article> findAllArticlesByMemberId(Long id);
}
