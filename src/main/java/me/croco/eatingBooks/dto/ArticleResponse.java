package me.croco.eatingBooks.dto;

import lombok.Getter;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private String isbn;
    private String writeType;
    private String articleType;
    private String publicYn;
    private String createdAt;
    private String updatedAt;

    private Map<Long, String> articleTemplateMap;

    private Member writer;


    public ArticleResponse(Article article, Map<Long, String> articleTemplateMap, Member writer) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.writer = writer;
        this.isbn = article.getIsbn();
        this.writeType = article.getWriteType();
        this.articleType = article.getArticleType();
        this.publicYn = article.getPublicYn();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        this.createdAt = article.getCreatedAt().format(formatter);
        this.updatedAt = article.getUpdatedAt().format(formatter);
        this.articleTemplateMap = articleTemplateMap;
    }
}
