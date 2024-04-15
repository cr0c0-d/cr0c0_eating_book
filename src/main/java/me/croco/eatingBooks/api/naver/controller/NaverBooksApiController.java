package me.croco.eatingBooks.api.naver.controller;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.api.naver.dto.NaverBookResponse;
import me.croco.eatingBooks.api.naver.dto.NaverBooksListResponse;
import me.croco.eatingBooks.api.naver.service.NaverBooksApiService;
import me.croco.eatingBooks.api.naver.dto.NaverBookFindRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NaverBooksApiController {
//
//    private final NaverBooksApiService naverBooksApiService;
//
//    @PostMapping("/api/books")
//    public ResponseEntity<NaverBooksListResponse> searchBooks(@RequestBody NaverBookFindRequest request) {
//        return ResponseEntity.ok()
//                .body(naverBooksApiService.searchBooks(request));
//    }
//
//    @GetMapping("/api/books/{isbn}")
//    public ResponseEntity<NaverBookResponse> findBook(@PathVariable String isbn) {
//        return ResponseEntity.ok()
//                .body(naverBooksApiService.findBook(isbn));
//    }
}
