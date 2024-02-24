package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
