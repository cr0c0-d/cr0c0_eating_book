package me.croco.eatingBooks.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.eatingBooks.domain.Article;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleAddRequest {

    private String title;
    private String content;
    private String writer;
    private String isbn;
    private String bookTitle;
    private String writeType;
    private String articleType;
    private String publicYn;


    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .isbn(isbn)
                .writer(writer)
                .bookTitle(bookTitle)
                .writeType(writeType)
                .articleType(articleType)
                .publicYn(publicYn)
                .build();
    }
}
