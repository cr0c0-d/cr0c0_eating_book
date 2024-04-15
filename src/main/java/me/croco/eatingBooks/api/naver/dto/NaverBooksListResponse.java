package me.croco.eatingBooks.api.naver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.eatingBooks.api.naver.domain.NaverBook;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverBooksListResponse {
    private int total;  // 총 검색 결과 개수
    private int start;  // 검색 시작 위치
    private int display;    // 한 번에 표시할 검색 결과 개수
    private List<NaverBook> items;  // 책 검색 결과
}
