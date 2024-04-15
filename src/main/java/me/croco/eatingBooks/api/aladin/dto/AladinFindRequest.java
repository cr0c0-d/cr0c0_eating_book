package me.croco.eatingBooks.api.aladin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AladinFindRequest {

    private String keyword;
    private String queryType;
    private int start;





}
