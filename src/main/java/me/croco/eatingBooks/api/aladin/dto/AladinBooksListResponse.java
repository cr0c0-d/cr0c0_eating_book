package me.croco.eatingBooks.api.aladin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.eatingBooks.api.aladin.domain.AladinBook;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinBooksListResponse {
    private int version; // API Version
    private String title; // API 결과의 제목
    private String link; // API 결과와 관련된 알라딘 페이지 URL 주소
    private String pubDate; // API 출력일
    private int totalResults; // API의 총 결과수
    private int startIndex; // Page수
    private int itemsPerPage; // 한 페이지에 출력될 상품 수
    private String query; // API로 조회한 쿼리
    private int searchCategoryId; // 분야로 조회한 경우 해당 분야의 ID
    private String searchCategoryName; // 분야로 조회한 경우 해당 분야의 분야명



    private List<AladinBook> item;

}
