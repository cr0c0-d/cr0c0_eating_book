package me.croco.eatingBooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.croco.eatingBooks.domain.Article;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticleAddRequest {

    private String title;
    private String content;
    private String writer;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }
}
