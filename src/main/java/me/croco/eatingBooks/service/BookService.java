package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.api.aladin.service.AladinApiService;
import me.croco.eatingBooks.api.naver.service.NaverBooksApiService;
import me.croco.eatingBooks.domain.Book;
import me.croco.eatingBooks.dto.BookFindRequest;
import me.croco.eatingBooks.dto.BookFindResponse;
import me.croco.eatingBooks.repository.ArticleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final ArticleRepository articleRepository;
    private final AladinApiService bookApiService;
    //private final NaverBooksApiService bookApiService;

    public BookFindResponse searchBooks(BookFindRequest request) {
        return new BookFindResponse(bookApiService.searchBooks(request));
    }

    public BookFindResponse findBook(String isbn) {
        return new BookFindResponse(bookApiService.findBook(isbn));
    }

    public BookFindResponse findBestSellerList() {
        return new BookFindResponse(bookApiService.findBestSellerList());
    }


    /**
     * 식전문의 수가 많은 책의 ISBN을 리스트로 반환
     */
    public List<Book> findBestBeforeArticle() {
        Pageable topFive = PageRequest.of(0, 5);
        List<Object[]> isbnList = articleRepository.findBestIsbnBeforeArticle(topFive);
        List<Book> books = new ArrayList<>();

        isbnList.forEach((isbn)->
                books.addAll(
                        bookApiService.findBook((String)isbn[0]).getItem()
                                .stream().map(Book::new).toList()
                )
        );

        return books;
    }

    /**
     * 식후문의 수가 많은 책의 ISBN을 리스트로 반환
     */
    public List<Book> findBestAfterArticle() {
        Pageable topFive = PageRequest.of(0, 5);
        List<Object[]> isbnList = articleRepository.findBestIsbnAfterArticle(topFive);
        List<Book> books = new ArrayList<>();

        isbnList.forEach((isbn)->
                books.addAll(
                        bookApiService.findBook((String)isbn[0]).getItem()
                                .stream().map(Book::new).toList()
                )
        );

        return books;
    }

}
