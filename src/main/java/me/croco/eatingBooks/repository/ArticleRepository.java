package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleQueryDSLRepository {
    @Query("SELECT a FROM Article a WHERE a.publicYn = 'true' ORDER BY a.createdAt DESC ")
    List<Article> findPublicArticles();
}
