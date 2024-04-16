package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Article;

import java.util.List;

public interface ArticleQueryDSLRepository {

    List<Article> findByIsbnOrderByCreatedAtDesc(String isbn);

    List<Article> findPublicArticlesByMember(String email);
}
