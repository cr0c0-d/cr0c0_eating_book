package me.croco.eatingBooks.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NaverBookFindRequest {

    private String keyword;
    private String queryType;
    private int start;





}
