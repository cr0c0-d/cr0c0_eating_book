package me.croco.eatingBooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
}
