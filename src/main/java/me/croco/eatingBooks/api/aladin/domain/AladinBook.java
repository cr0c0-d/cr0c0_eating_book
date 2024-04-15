package me.croco.eatingBooks.api.aladin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AladinBook {
    private String title; // 상품명
    private String link; // 상품 링크 URL
    private String author; // 저자/아티스트
    private String pubdate; // 출간일(출시일)
    private String description; // 상품설명 (요약)
    private String isbn; // 10자리 ISBN
    private String isbn13; // 13자리 ISBN
    private int pricesales; // 판매가
    private int pricestandard; // 정가
    private String mallType; // 상품의 몰타입(국내도서:BOOK, 음반:MUSIC, Dvd:DVD, 외서:FOREIGN, 전자책:EBOOK, 중고상품:USED)
    private String stockstatus; // 재고상태(정상유통일 경우 비어있음, 품절/절판 등)
    private int mileage; // 마일리지
    private String cover; // 커버(표지)
    private String publisher; // 출판사(제작사/출시사)
    private int salesPoint; // 판매지수
    private boolean adult; // 성인 등급 여부 (true인 경우 성인 등급 도서)
    private boolean fixedPrice; // (종이책/전자책인 경우) 정가제 여부 (true인 경우 정가제 해당 도서)
    private int customerReviewRank; // 회원 리뷰 평점(별점 평균) : 0~10점(별0.5개당 1점)
    private String bestDuration; // (베스트셀러인 경우만 노출) 베스트셀러 순위 관련 추가 정보
    private int bestRank; // (베스트셀러인 경우만 노출) 베스트셀러 순위 정보

}
