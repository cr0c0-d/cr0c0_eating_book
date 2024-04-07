package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.ArticleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleTemplatesRepository extends JpaRepository<ArticleTemplate, Long> {
    List<ArticleTemplate> findByTypeOrderByNumAsc(String type);
}
