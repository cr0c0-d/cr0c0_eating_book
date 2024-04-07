package me.croco.eatingBooks.api;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.dto.AladinBookResponse;
import me.croco.eatingBooks.dto.AladinBooksListResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.service.AladinApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AladinApiController {
//
//    private final AladinApiService aladinApiService;
//
//    @PostMapping("/api/books")
//    public ResponseEntity<AladinBooksListResponse> searchBooks(@RequestBody AladinFindRequest request) {
//        return ResponseEntity.ok()
//                .body(aladinApiService.searchBooks(request));
//    }
//
//    @GetMapping("/api/books/{isbn}")
//    public ResponseEntity<AladinBookResponse> findBook(@PathVariable String isbn) {
//        return ResponseEntity.ok()
//                .body(aladinApiService.findBook(isbn));
//    }
}
