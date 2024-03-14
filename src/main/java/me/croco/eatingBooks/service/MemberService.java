package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.MemberAddRequest;
import me.croco.eatingBooks.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean existsId(MemberAddRequest request) {
        return memberRepository.existsById(request.getId());
    }

    public String save(MemberAddRequest request) {
        return memberRepository.save(
                Member.builder()
                        .id(request.getId())
                        .nickname(request.getNickname())
                        .password(bCryptPasswordEncoder.encode(request.getPassword()))
                        .build()
                )
                .getId();

    }

    public Member findMemberById(String id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 id 입니다."));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("id : " + username));
    }
}
