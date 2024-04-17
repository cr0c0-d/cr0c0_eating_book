package me.croco.eatingBooks.dto;

import lombok.Getter;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class ArticleListResponse {
    private Long id;
    private String title;
    private Long writerId;
    private String writerNickname;
    private String articleType;
    private String isbn;
    private String bookTitle;
    private String createdAt;

    public ArticleListResponse(Article article, Member writer) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.writerId = writer.getId();
        this.writerNickname = writer.getNickname();
        this.articleType = article.getArticleType();
        this.isbn = article.getIsbn();
        this.bookTitle = article.getBookTitle();

        LocalDate today = LocalDate.now();
        if(article.getCreatedAt().toLocalDate().equals(today)) {    // 오늘 작성된 글인 경우
            this.createdAt = article.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm"));   // 시간만
        } else {    // 아니면 날짜만
            this.createdAt = article.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }

    }
}
