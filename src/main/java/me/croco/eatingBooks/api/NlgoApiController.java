package me.croco.eatingBooks.api;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.dto.AladinBookResponse;
import me.croco.eatingBooks.dto.AladinBooksListResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.dto.NlgoFindRequest;
import me.croco.eatingBooks.service.AladinApiService;
import me.croco.eatingBooks.service.NlgoApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NlgoApiController {
//    private final NlgoApiService nlgoApiService;
//
//    @PostMapping("/api/books")
//    public ResponseEntity<AladinBooksListResponse> searchBooks(@RequestBody NlgoFindRequest request) {
//        return ResponseEntity.ok()
//                .body(nlgoApiService.searchBooks(request));
//    }
//
//    @GetMapping("/api/books/{isbn}")
//    public ResponseEntity<AladinBookResponse> findBook(@PathVariable String isbn) {
//        return ResponseEntity.ok()
//                .body(nlgoApiService.findBook(isbn));
//    }
}
