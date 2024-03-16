package me.croco.eatingBooks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JasyptTest {
    @Value("${jasypt.encryptor.password}")
    private String password;

    public String getPassword() {
        return password;
    }
}
