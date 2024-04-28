package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.api.aladin.service.AladinApiService;
import me.croco.eatingBooks.domain.Book;
import me.croco.eatingBooks.dto.BookFindRequest;
import me.croco.eatingBooks.dto.BookFindResponse;
import me.croco.eatingBooks.dto.BooksByMemberResponse;
import me.croco.eatingBooks.repository.ArticleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public BooksByMemberResponse findBooksByMember(String email) {
        List<String> doneList = articleRepository.findDoneReadingIsbnListByMemberEmail(email);
        List<Book> doneBooks = new ArrayList<>();

        doneList.forEach(isbn ->
                doneBooks.addAll(
                        bookApiService.findBook(isbn).getItem()
                                .stream().map(Book::new).toList()
                )
        );

        List<String> upcomingList = articleRepository.findUpcomingReadingIsbnListByMemberEmail(email, doneList);
        List<Book> upcomingBooks = new ArrayList<>();
        upcomingList.forEach(isbn ->
                upcomingBooks.addAll(
                        bookApiService.findBook(isbn).getItem()
                                .stream().map(Book::new).toList()
                )
        );
        return new BooksByMemberResponse(upcomingBooks, doneBooks);
    }



}
