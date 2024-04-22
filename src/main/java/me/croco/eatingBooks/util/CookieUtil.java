package me.croco.eatingBooks.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

public class CookieUtil {

    // 요청값(이름, 값, 만료기간)으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");    // 쿠키가 모든 경로에서 사용될 수 있다.
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    public static String getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // 쿠키의 이름을 받아 해당 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return;
        }

        for(Cookie cookie: cookies) {
            if(name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()   // Base64 인코더 가져옴 (URL에서 사용하기 적합)
                .encodeToString(    //바이트 배열을 인코딩해서 문자열로 반환
                        SerializationUtils.serialize(obj)   // 객체를 바이트 배열로 직렬화
                );
    }

    // 쿠키를 역직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        /*
         기존 방법
         Serialization의 deserialize()가 deprecated되었음

        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
        */

        // 쿠키의 값을 Base64 디코더로 역직렬화하여 바이트 배열 얻음
        byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());     

        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(data);  // 바이트 배열을 입력 스트림으로 변환
            ObjectInputStream ois = new ObjectInputStream(bis); // 객체 입력 스트림 생성
            Object object = ois.readObject();   // 스트림에서 객체를 읽어 반환
            return cls.cast(object);    // 지정된 타입의 클래스 T로 변환해서 반환

        } catch (Exception e) {
            throw new RuntimeException("역직렬화 중 오류 발생", e);
        }
    }
}
