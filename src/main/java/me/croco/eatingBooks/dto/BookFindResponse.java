package me.croco.eatingBooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.eatingBooks.api.aladin.dto.AladinBookResponse;
import me.croco.eatingBooks.api.naver.domain.NaverBook;
import me.croco.eatingBooks.api.naver.dto.NaverBookResponse;
import me.croco.eatingBooks.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookFindResponse {
    private int total;
    private int start;
    private int display;
    private List<Book> items;

    public BookFindResponse(NaverBookResponse naverBookResponse) {
        this.total = naverBookResponse.getTotal();
        this.start = (naverBookResponse.getStart() - 1) / 10 + 1;
        this.display = naverBookResponse.getDisplay();
        this.items = naverBookResponse.getItems().stream().map((Book::new)).collect(Collectors.toList());
    }

    public BookFindResponse(AladinBookResponse aladinBookResponse) {
        this.total = aladinBookResponse.getTotalResults();
        this.start = aladinBookResponse.getStartIndex();
        this.display = aladinBookResponse.getItemsPerPage();
        this.items = aladinBookResponse.getItem().stream().map((Book::new)).collect(Collectors.toList());
    }
}
