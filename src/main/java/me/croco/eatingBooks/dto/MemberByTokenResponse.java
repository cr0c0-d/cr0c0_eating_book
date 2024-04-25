package me.croco.eatingBooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.util.Authorities;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public class MemberByTokenResponse {
    private Long id;
    private String nickname;
    private String role;

    public MemberByTokenResponse(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.role = member.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch((authority)-> authority.equals(Authorities.ROLE_ADMIN.getAuthorityName())) ? Authorities.ROLE_ADMIN.getAuthorityName() : Authorities.ROLE_USER.getAuthorityName();
    }
}
