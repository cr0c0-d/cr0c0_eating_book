package me.croco.eatingBooks.service;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import me.croco.eatingBooks.domain.Book;
import me.croco.eatingBooks.dto.AladinBooksResponse;
import me.croco.eatingBooks.dto.AladinFindRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class AladinApiService {

    // API 문서
    // https://docs.google.com/document/d/1mX-WxuoGs8Hy-QalhHcvuV17n50uGI2Sg_GHofgiePE/edit

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


    // HttpURLConnection 객체 생성
    public HttpURLConnection getHttpURLConnection(String urlString) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlString);
            System.out.println("알라딘 API 연결 : " + urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); //Method 방식 설정. GET/POST/DELETE/PUT/HEAD/OPTIONS/TRACE
            conn.setConnectTimeout(5000); //연결제한 시간 설정. 5초 간 연결시도
            conn.setRequestProperty("Content-Type", "application/json");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return conn;

    }

    public String getHttpResponse(HttpURLConnection conn) {
        StringBuilder sb = null;

        try {
            if(conn.getResponseCode() == 200) {
                // 정상적으로 데이터를 받았을 경우
                //데이터 가져오기
                sb = readResponseData(conn.getInputStream());
            }else{
                // 정상적으로 데이터를 받지 못했을 경우

                //오류코드, 오류 메시지 표출
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                //오류정보 가져오기
                sb = readResponseData(conn.getErrorStream());
                System.out.println("error : " + sb.toString());
                return null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            conn.disconnect(); //연결 해제
        };
        if(sb == null) return null;

        return sb.toString();
    }

    public StringBuilder readResponseData(InputStream in) {
        if(in == null ) return null;

        StringBuilder sb = new StringBuilder();
        String line = "";

        try (InputStreamReader ir = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(ir)){
            while( (line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb;
    }


    public AladinBooksResponse searchBooks(AladinFindRequest request) {

        HttpURLConnection httpURLConnection = getHttpURLConnection(findUrl + "?ttbkey=" + ttbKey + "&Query=" + request.getKeyword().replace(" ", "+") + "&QueryType=" + request.getQueryType() + "&output=js");
        String result = getHttpResponse(httpURLConnection);

        //ObjectMapper mapper = new ObjectMapper();
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
