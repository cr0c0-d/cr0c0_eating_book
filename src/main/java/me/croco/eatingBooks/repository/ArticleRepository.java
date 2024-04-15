package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.publicYn = 'true' ORDER BY a.createdAt DESC ")
    List<Article> findPublicArticles();

    @Query("SELECT a.isbn, COUNT(a.id) as count FROM Article a WHERE a.articleType = 'B' GROUP BY a.isbn ORDER BY count DESC")
    List<Object[]> findBestIsbnBeforeArticle(Pageable pageable);

    @Query("SELECT a.isbn, COUNT(a.id) as count FROM Article a WHERE a.articleType = 'A' GROUP BY a.isbn ORDER BY count DESC")
    List<Object[]> findBestIsbnAfterArticle(Pageable pageable);

    List<Article> findByIsbnOrderByCreatedAtDesc(String isbn);
}
