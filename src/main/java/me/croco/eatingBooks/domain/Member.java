package me.croco.eatingBooks.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.croco.eatingBooks.dto.MemberUpdateRequest;
import me.croco.eatingBooks.util.Authorities;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Authorities authorities;

    @Builder
    public Member(String email, String nickname, String password, String profileImg, Authorities authorities) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImg = profileImg;
        this.authorities = authorities;
    }

    @Override   // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorities.getAuthorityName()));
    }

    // 사용자 닉네임 변경
    public Member update(MemberUpdateRequest updateInfo) {
        this.profileImg = updateInfo.getProfileImg();
        this.nickname = updateInfo.getNickname();
        if(!updateInfo.getPassword().isEmpty()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            this.password = bCryptPasswordEncoder.encode(updateInfo.getPassword());
        }
        return this;
    }
//    public Member updateProfileImg(String profileImg) {
//        this.profileImg = profileImg;
//        return this;
//    }
//
//    public Member updatePassword(String password) {
//        this.password = password;
//        return this;
//    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료 여부 확인 로직
        return true;    // true : 만료되지 않음
    }

    // 계정 잠금여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;    // true : 잠금되지 않음
    }

    // 비밀번호 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;    // true : 만료되지 않음
    }

    // 계정 사용가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;    // true : 사용가능
    }


}
