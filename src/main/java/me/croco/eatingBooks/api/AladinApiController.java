package me.croco.eatingBooks.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.dto.AladinBooksResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.service.AladinApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequiredArgsConstructor
public class AladinApiController {

    private final AladinApiService aladinApiService;

    @PostMapping("/api/books")
    public ResponseEntity<AladinBooksResponse> searchBooks(@RequestBody AladinFindRequest request) {
        return ResponseEntity.ok()
                .body(aladinApiService.searchBooks(request));
    }

}
