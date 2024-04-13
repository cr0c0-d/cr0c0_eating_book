package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.MemberAddRequest;
import me.croco.eatingBooks.dto.MemberUpdateRequest;
import me.croco.eatingBooks.repository.MemberRepository;
import me.croco.eatingBooks.util.Authorities;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email : " + username));
    }

    public List<Member> findAll() {
        // 로그인 사용자가 ADMIN인지 확인
        String loginUserAuthority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);
        if(loginUserAuthority.equals(Authorities.ROLE_ADMIN.getAuthorityName())) { // admin 아님 -> 조회 권한 없음
            throw new AccessDeniedException("조회 권한 없음");
        } else {
            return memberRepository.findAll();
        }
    }

    public Member findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id : " + id));
        Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 사용자가 admin이 아니고, 로그인 사용자의 정보를 조회하는 것도 아닌 경우
        if(!loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0).equals(Authorities.ROLE_ADMIN.getAuthorityName())
                &&
                !member.getEmail().equals(loginUser.getName())) {
            throw new AccessDeniedException("조회 권한 없음");

        } else {    // admin이거나, 본인 정보를 조회하는 경우
            return member;
        }
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email : " + email));
    }

    public Long saveMember(MemberAddRequest request) {

        if(memberRepository.existsByEmail(request.getEmail())) {    // 이메일 중복확인
            throw new IllegalArgumentException("중복된 이메일");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return memberRepository.save(
                        Member.builder()
                                .email(request.getEmail())
                                .nickname(request.getNickname())
                                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                                .authorities(Authorities.ROLE_USER)
                                .build()
                )
                .getId();
    }

    @Transactional
    public Long updateMember(MemberUpdateRequest request) {
        Member member = memberRepository.findById(request.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id : " + request.getId()));

        // 비밀번호를 변경했다면
        if(!request.getPassword().equals("")) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            member.updatePassword(bCryptPasswordEncoder.encode(request.getPassword()));
        }

        // 닉네임을 변경했다면
        if(!member.getNickname().equals(request.getNickname())) {
            member.update(request.getNickname());
        }

        // 프로필 이미지를 변경했다면
        if(!member.getProfileImg().equals(request.getProfileImg())) {
            member.updateProfileImg(request.getProfileImg());
        }

        return member.getId();
    }
}
