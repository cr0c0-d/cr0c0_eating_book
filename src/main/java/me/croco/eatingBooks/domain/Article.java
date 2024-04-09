package me.croco.eatingBooks.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "write_type", nullable = false)
    private String writeType;

    @Column(name = "article_type", nullable = false)
    private String articleType;

    @Column(name = "public_yn", nullable = false)
    private String publicYn;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Article(String title, String content, String writer, String isbn, String bookTitle, String writeType, String articleType, String publicYn) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.writeType = writeType;
        this.articleType = articleType;
        this.publicYn = publicYn;
    }

    public void update(String title, String content, String writeType, String publicYn) {
        this.title = title;
        this.content = content;
        this.writeType = writeType;
        this.publicYn = publicYn;
    }
}

