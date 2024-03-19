package me.croco.eatingBooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.util.Authorities;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddRequest {

    private String email;

    private String password;

    private String nickname;

    public Member toEntity() {
        return Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .authorities(Authorities.ROLE_USER)
                    .build();
    }
}
