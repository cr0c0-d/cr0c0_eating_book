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

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "public_yn", nullable = false)
    private String publicYn;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Article(String title, String content, String writer, String isbn, String type, String publicYn) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.isbn = isbn;
        this.type = type;
        this.publicYn = publicYn;
    }

    public void update(String title, String content, String type, String publicYn) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.publicYn = publicYn;
    }
}

