package me.croco.eatingBooks.service;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Book;
import me.croco.eatingBooks.dto.AladinBooksResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import me.croco.eatingBooks.util.HttpConnection;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class AladinApiService {

    // API 문서
    // https://docs.google.com/document/d/1mX-WxuoGs8Hy-QalhHcvuV17n50uGI2Sg_GHofgiePE/edit

    private HttpConnection httpConnection;

    private final String ttbKey = "ttbhyde69ciel2017001";

    // 상품 검색 URL
    // 필수값 : ttbkey, Query(검색어)
    private final String findUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";

    // 상품 리스트 URL
    // 필수값 : ttbkey, QueryType( ItemNewAll : 신간 전체 리스트
    //                              ItemNewSpecial : 주목할 만한 신간 리스트
    //                              ItemEditorChoice : 편집자 추천 리스트 (카테고리로만 조회 가능 - 국내도서/음반/외서만 지원)
    //                              Bestseller : 베스트셀러
    //                              BlogBest : 블로거 베스트셀러 (국내도서만 조회 가능)
    //                            )
    private final String findListUrl = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";

    // 상품 조회 URL
    // 필수값 : ttbkey, ItemId(알라딘 고유값 정수 혹은 ISBN), ItemIdType(ISBN(기본값) : ISBN 10자리, ISBN13 : ISBN 13자리, ItemId)
    //                                                        조회용 파라미터인 ItemId가 ISBN으로 입력되었는지, 알라딘고유의"ItemId"값으로 입력되었는지 선택
    //                                                        가급적 13자리 ISBN을 이용해주세요
    private final String findByIdUrl = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";





    public AladinBooksResponse searchBooks(AladinFindRequest request) {
        String keyword = request.getKeyword();
        keyword = keyword.contains(" ") ? keyword.replace(" ", "+") : keyword;  // 검색어에 공백 존재시 '+'로 바꿔서 검색

        int start = request.getStart();
        start = start == 0 ? 1 : start;

        HttpURLConnection httpURLConnection = httpConnection.getHttpURLConnection(findUrl + "?ttbkey=" + ttbKey
                + "&Query=" + keyword
                + "&QueryType=" + request.getQueryType()
                + "&Start=" +  start    // 페이지
                
                + "&Sort=SalesPoint"    // 정렬 순서(판매량 순)
                + "&Version=20131101"   // 검색 API 버전
                + "&Cover=Big"  // 표지 이미지 크기
                + "&MaxResults=10"  // 한 페이지 최대 출력 개수
                + "&SearchTarget=Book"  // 검색 대상 : 도서
                + "&output=js"   // 검색 결과 : JSON
        );
        String result = httpConnection.getHttpResponse(httpURLConnection);

        ObjectMapper mapper = JsonMapper.builder()
                                    .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                                    .build();
        AladinBooksResponse response = null;

        try {
            response = mapper.readValue(result, AladinBooksResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
