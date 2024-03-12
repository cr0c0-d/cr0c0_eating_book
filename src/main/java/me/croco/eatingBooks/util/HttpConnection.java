package me.croco.eatingBooks.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@NoArgsConstructor
public class HttpConnection {

    // HttpURLConnection 객체 생성
    public static HttpURLConnection getHttpURLConnection(String urlString) {
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlString);
            System.out.println("API 연결 : " + urlString);
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

    public static String getHttpResponse(HttpURLConnection conn) {
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

    public static StringBuilder readResponseData(InputStream in) {
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
}
