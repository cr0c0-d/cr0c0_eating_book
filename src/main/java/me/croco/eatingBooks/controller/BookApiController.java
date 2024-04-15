package me.croco.eatingBooks.controller;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.api.aladin.service.AladinApiService;
import me.croco.eatingBooks.api.naver.service.NaverBooksApiService;
import me.croco.eatingBooks.domain.Book;
import me.croco.eatingBooks.dto.BookFindRequest;
import me.croco.eatingBooks.dto.BookFindResponse;
import me.croco.eatingBooks.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookApiService;
    //private final NaverBooksApiService bookApiService;

    @PostMapping("/api/books")
    public ResponseEntity<BookFindResponse> searchBooks(@RequestBody BookFindRequest request) {
        return ResponseEntity.ok()
                .body(bookApiService.searchBooks(request));
    }

    @GetMapping("/api/books/{isbn}")
    public ResponseEntity<BookFindResponse> findBook(@PathVariable String isbn) {
        return ResponseEntity.ok()
                .body(bookApiService.findBook(isbn));
    }

    @GetMapping("/api/books/best")
    public ResponseEntity<BookFindResponse> findBestSellerList() {
        return ResponseEntity.ok()
                .body(bookApiService.findBestSellerList());
    }

    @GetMapping("/api/books/best/before")
    public ResponseEntity<List<Book>> findBestBeforeArticle() {
        return ResponseEntity.ok()
                .body(bookApiService.findBestBeforeArticle());
    }

    @GetMapping("/api/books/best/after")
    public ResponseEntity<List<Book>> findBestAfterArticle() {
        return ResponseEntity.ok()
                .body(bookApiService.findBestAfterArticle());
    }
}
