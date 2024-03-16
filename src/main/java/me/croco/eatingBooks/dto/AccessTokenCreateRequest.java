package me.croco.eatingBooks.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessTokenCreateRequest {
    private String refreshToken;
}
