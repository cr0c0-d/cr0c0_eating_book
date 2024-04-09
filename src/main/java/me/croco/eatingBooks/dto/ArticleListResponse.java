package me.croco.eatingBooks.dto;

import lombok.Getter;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.Member;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
public class ArticleListResponse {
    private Long id;
    private String title;
    private String writer;
    private String writerNickname;
    private String articleType;
    private String isbn;
    private String bookTitle;
    private String createdAt;

    public ArticleListResponse(Article article, String writerNickname) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.writer = article.getWriter();
        this.writerNickname = writerNickname;
        this.articleType = article.getArticleType();
        this.isbn = article.getIsbn();
        this.bookTitle = article.getBookTitle();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        this.createdAt = article.getCreatedAt().format(formatter);
    }
}
