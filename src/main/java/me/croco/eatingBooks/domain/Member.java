package me.croco.eatingBooks.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @CreatedDate
    @Column(name="created_at")
    private Timestamp createdAt;

    @Builder
    public Member(String id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    @Override   // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return id;
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
