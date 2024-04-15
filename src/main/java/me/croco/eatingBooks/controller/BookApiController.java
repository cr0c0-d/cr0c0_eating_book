package me.croco.eatingBooks.controller;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.api.aladin.service.AladinApiService;
import me.croco.eatingBooks.api.naver.service.NaverBooksApiService;
import me.croco.eatingBooks.dto.BookFindRequest;
import me.croco.eatingBooks.dto.BookFindResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final AladinApiService bookApiService;
    //private final NaverBooksApiService bookApiService;

    @PostMapping("/api/books")
    public ResponseEntity<BookFindResponse> searchBooks(@RequestBody BookFindRequest request) {
        return ResponseEntity.ok()
                .body(new BookFindResponse(bookApiService.searchBooks(request)));
    }

    @GetMapping("/api/books/{isbn}")
    public ResponseEntity<BookFindResponse> findBook(@PathVariable String isbn) {
        return ResponseEntity.ok()
                .body(new BookFindResponse(bookApiService.findBook(isbn)));
    }
}
