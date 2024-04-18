package me.croco.eatingBooks.api.aladin.controller;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.api.aladin.dto.AladinBooksListResponse;
import me.croco.eatingBooks.api.aladin.dto.AladinFindRequest;
import me.croco.eatingBooks.api.aladin.service.AladinApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AladinViewController {
//
//    private final AladinApiService aladinApiService;
//
//    @PostMapping("/books")
//    public String searchBooks(AladinFindRequest request, Model model) {
//        AladinBooksListResponse result = aladinApiService.searchBooks(request);
//        model.addAttribute("result", result);
//        return "searchBooks";
//    }
//
//    @GetMapping("/*")
//    public String home() {
//        return "searchBooks";
//    }
//
//    @GetMapping("/navbar.html")
//    public String nav() {
//        return "navbar";
//    }
//

}
