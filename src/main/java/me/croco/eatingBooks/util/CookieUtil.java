package me.croco.eatingBooks.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

public class CookieUtil {

    // 요청값(이름, 값, 만료기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");    // 쿠키가 모든 경로에서 사용될 수 있다.
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();;
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
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
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

        byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());

        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object object = ois.readObject();
            return cls.cast(object);

        } catch (Exception e) {
            throw new RuntimeException("역직렬화 중 오류 발생", e);
        }
    }
}
