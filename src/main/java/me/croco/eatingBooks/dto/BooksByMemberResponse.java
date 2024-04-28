package me.croco.eatingBooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.croco.eatingBooks.domain.Book;

import java.util.List;

@AllArgsConstructor
@Getter
public class BooksByMemberResponse {
    List<Book> upcomingBooks;

    List<Book> doneBooks;
}
