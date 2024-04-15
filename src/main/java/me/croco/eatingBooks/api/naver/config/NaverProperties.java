package me.croco.eatingBooks.api.naver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver") // 프로퍼티값을 가져와서 사용하겠다는 뜻
public class NaverProperties {
    private String clientId;
    private String clientSecret;
}
