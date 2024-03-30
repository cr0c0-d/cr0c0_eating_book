package me.croco.eatingBooks.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    // JWT 토큰 생성
    public String makeToken(Date expiry, Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)       // 헤더 - typ(토큰 타입) : JWT
                .setIssuer(jwtProperties.getIssuer())               // 내용 - iss(토큰 발급자) : properties 설정값
                .setIssuedAt(now)                                   // 내용 - iat(발급시간) : 현재 시간
                .setExpiration(expiry)                              // 내용 - exp(만료시간) : expiry 변수값
                .setSubject(member.getEmail())                      // 내용 - sub(토큰 제목) : 유저 이메일
                .claim("id", member.getId())                    // 클레임 id : 유저 id
                .claim("role", member.getAuthorities())        // 클레임 roles : 유저의 권한
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())   // 서명 : 비밀값, 해시값 HS256로 암호화
                .compact();
    }

    // JWT 토큰 유효성 검증
    public boolean validToken(String token) {
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())        // 비밀값으로 복호화
                    .parseClaimsJws(token);     // 주어진 토큰을 파싱하여 Jws<Claims> 반환 시도 -> 유효하지 않은 토큰일 시 예외 발생
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 기반 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority((String) getClaims(token).get("role")));

        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 토큰 기반 유저 id 가져오기
    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
