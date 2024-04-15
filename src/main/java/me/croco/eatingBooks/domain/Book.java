package me.croco.eatingBooks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.eatingBooks.api.aladin.domain.AladinBook;
import me.croco.eatingBooks.api.naver.domain.NaverBook;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * 도서 검색 API의 검색 결과를 하나의 클래스로 만드는 책 클래스
 */
public class Book {
    private String title;
    private String link;
    private String image;
    private String author;
    private String discount;
    private String publisher;
    private String isbn;
    private String description;
    private String pubdate;

    public Book(NaverBook naverBook) {
        this.title = naverBook.getTitle();
        this.link = naverBook.getLink();
        this.image = naverBook.getImage();
        this.author = naverBook.getAuthor();
        this.discount = naverBook.getDiscount();
        this.publisher = naverBook.getPublisher();
        this.isbn = naverBook.getIsbn();
        this.description = naverBook.getDescription();
        this.pubdate = naverBook.getPubdate();
    }

    public Book(AladinBook aladinBook) {
        this.title = aladinBook.getTitle();
        this.link = aladinBook.getLink();
        this.image = aladinBook.getCover();
        this.author = aladinBook.getAuthor();
        this.discount = String.valueOf(aladinBook.getPricestandard());
        this.publisher = aladinBook.getPublisher();
        this.isbn = aladinBook.getIsbn13();
        this.description = aladinBook.getDescription();
        this.pubdate = aladinBook.getPubDate();
    }
}
