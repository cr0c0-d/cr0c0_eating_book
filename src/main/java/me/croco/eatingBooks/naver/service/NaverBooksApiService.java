package me.croco.eatingBooks.naver.service;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.dto.AladinBookResponse;
import me.croco.eatingBooks.dto.AladinBooksListResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.naver.config.NaverProperties;
import me.croco.eatingBooks.naver.dto.NaverBookFindRequest;
import me.croco.eatingBooks.naver.dto.NaverBookResponse;
import me.croco.eatingBooks.naver.dto.NaverBooksListResponse;
import me.croco.eatingBooks.util.HttpConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static me.croco.eatingBooks.util.HttpConnection.getHttpResponse;
import static me.croco.eatingBooks.util.HttpConnection.getHttpURLConnection;

@Service
@RequiredArgsConstructor
public class NaverBooksApiService {

    // API 문서
    // https://docs.google.com/document/d/1mX-WxuoGs8Hy-QalhHcvuV17n50uGI2Sg_GHofgiePE/edit

    private final NaverProperties naverProperties;
    private HttpConnection httpConnection;

    // 상품 검색 URL
    // 필수값 : ttbkey, Query(검색어)
    private final String findUrl = "https://openapi.naver.com/v1/search/book.json";

    // 상품 조회 URL
    // 필수값 : ttbkey, ItemId(알라딘 고유값 정수 혹은 ISBN), ItemIdType(ISBN(기본값) : ISBN 10자리, ISBN13 : ISBN 13자리, ItemId)
    //                                                        조회용 파라미터인 ItemId가 ISBN으로 입력되었는지, 알라딘고유의"ItemId"값으로 입력되었는지 선택
    //                                                        가급적 13자리 ISBN을 이용해주세요
    private final String findByIdUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";





    public NaverBooksListResponse searchBooks(NaverBookFindRequest request) {
//        String keyword = request.getKeyword();
//        keyword = keyword.contains(" ") ? keyword.replace(" ", "+") : keyword;  // 검색어에 공백 존재시 '+'로 바꿔서 검색

        int start = request.getStart();
        start = start == 0 ? 1 : start;

        String apiUrl = UriComponentsBuilder.fromUriString(findUrl)
                .queryParam("query", request.getKeyword())
                .queryParam("display", 10)
                .queryParam("start", start)
                .build()
                .encode()
                .toUriString();

        HttpURLConnection httpURLConnection = getHttpURLConnection(apiUrl);
        httpURLConnection.setRequestProperty("X-Naver-Client-Id", naverProperties.getClientId());
        httpURLConnection.setRequestProperty("X-Naver-Client-Secret", naverProperties.getClientSecret());
        String result = getHttpResponse(httpURLConnection);

        ObjectMapper mapper = JsonMapper.builder()
                                    .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                                    .build();
        NaverBooksListResponse response = null;

        try {
            response = mapper.readValue(result, NaverBooksListResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
//
//    public NaverBookResponse findBook(String isbn) {
//        String apiUrl = UriComponentsBuilder.fromUriString(findByIdUrl)
//                .queryParam("ttbkey", ttbKey)
//                .queryParam("ItemId", isbn)
//                .queryParam("ItemIdType", "ISBN13")
//                .queryParam("Cover", "Big")
//                .queryParam("Output", "js")
//                .queryParam("Version", "20131101")
//                .build()
//                .encode()
//                .toUriString();
//
//        HttpURLConnection httpURLConnection = getHttpURLConnection(apiUrl);
//
//        String result = getHttpResponse(httpURLConnection);
//
//        ObjectMapper mapper = JsonMapper.builder()
//                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
//                .build();
//
//        NaverBookResponse response = null;
//
//        try {
//            response = mapper.readValue(result, NaverBookResponse.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response;
//
//
//
//    }

}
