package me.croco.eatingBooks.service;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.dto.AladinBookResponse;
import me.croco.eatingBooks.dto.AladinBooksListResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.dto.NlgoFindRequest;
import me.croco.eatingBooks.util.HttpConnection;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static me.croco.eatingBooks.util.HttpConnection.getHttpResponse;
import static me.croco.eatingBooks.util.HttpConnection.getHttpURLConnection;

@Service
@RequiredArgsConstructor
public class NlgoApiService {

    private HttpConnection httpConnection;

    private final String key = "cf18a16cb4d6cd8f7ad44ca24487bb5203950c39167ace5532e8056081b4971c";

    // 일반검색 요청URL
    // 필수값 : key, pageNum
    private final String findUrl = "https://www.nl.go.kr/NL/search/openApi/search.do";

    // 상품 조회 URL
    // 필수값 : ttbkey, ItemId(알라딘 고유값 정수 혹은 ISBN), ItemIdType(ISBN(기본값) : ISBN 10자리, ISBN13 : ISBN 13자리, ItemId)
    //                                                        조회용 파라미터인 ItemId가 ISBN으로 입력되었는지, 알라딘고유의"ItemId"값으로 입력되었는지 선택
    //                                                        가급적 13자리 ISBN을 이용해주세요
    private final String findByIdUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";





    public AladinBooksListResponse searchBooks(NlgoFindRequest request) {
//        String keyword = request.getKeyword();
//        keyword = keyword.contains(" ") ? keyword.replace(" ", "+") : keyword;  // 검색어에 공백 존재시 '+'로 바꿔서 검색

//        int start = request.getStart();
//        start = start == 0 ? 1 : start;

        String apiUrl = UriComponentsBuilder.fromUriString(findUrl)
                .queryParam("key", key)
                .queryParam("srchTarget", request.getQueryType())  // total(전체), title(제목), author(저자), publisher(발행자)
                .queryParam("kwd", request.getKeyword())
                .queryParam("pageNum", request.getStart())
                .queryParam("pageSize", 10)
                .queryParam("systemType", "오프라인자료")
                .queryParam("category", "도서")
                .queryParam("apiType", "json")
                .build()
                .encode()
                .toUriString();



        HttpURLConnection httpURLConnection = getHttpURLConnection(apiUrl);
        String result = getHttpResponse(httpURLConnection);

        ObjectMapper mapper = JsonMapper.builder()
                                    .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                                    .build();
        AladinBooksListResponse response = null;

        try {
            response = mapper.readValue(result, AladinBooksListResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public AladinBookResponse findBook(String isbn) {
        String apiUrl = UriComponentsBuilder.fromUriString(findByIdUrl)
                .queryParam("ttbkey", key)
                .queryParam("ItemId", isbn)
                .queryParam("ItemIdType", "ISBN13")
                .queryParam("Cover", "Big")
                .queryParam("Output", "js")
                .queryParam("Version", "20131101")
                .build()
                .encode()
                .toUriString();

        HttpURLConnection httpURLConnection = getHttpURLConnection(apiUrl);

        String result = getHttpResponse(httpURLConnection);

        ObjectMapper mapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .build();

        AladinBookResponse response = null;

        try {
            response = mapper.readValue(result, AladinBookResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;



    }

}
