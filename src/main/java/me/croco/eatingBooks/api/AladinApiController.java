package me.croco.eatingBooks.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.dto.AladinBooksResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.service.AladinApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
@RequiredArgsConstructor
public class AladinApiController {

    private final AladinApiService aladinApiService;

    @GetMapping("/api/books")
    public String searchBooks(String keyword, String queryType, Model model) {
        System.out.println(keyword + " :::: " + queryType);
        AladinBooksResponse result = aladinApiService.searchBooks(keyword, queryType);
        model.addAttribute("result", result);
        return "searchBooks";
    }

    @GetMapping("/home")
    public String home() {
        return "searchBooks";
    }


}
