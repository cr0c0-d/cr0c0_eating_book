package me.croco.eatingBooks.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "num", nullable = false)
    private int num;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public ArticleTemplate(String type, int num, String content) {
        this.type = type;
        this.num = num;
        this.content = content;
    }
}
