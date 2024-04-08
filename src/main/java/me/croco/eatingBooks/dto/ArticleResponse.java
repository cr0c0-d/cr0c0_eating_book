package me.croco.eatingBooks.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.ArticleTemplate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String isbn;
    private String writeType;
    private String articleType;
    private String publicYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Map<Long, String> articleTemplateMap;


    public ArticleResponse(Article article, Map<Long, String> articleTemplateMap) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.writer = article.getWriter();
        this.isbn = article.getIsbn();
        this.writeType = article.getWriteType();
        this.articleType = article.getArticleType();
        this.publicYn = article.getPublicYn();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.articleTemplateMap = articleTemplateMap;
    }
}
