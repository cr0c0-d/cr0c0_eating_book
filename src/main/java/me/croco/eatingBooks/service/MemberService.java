package me.croco.eatingBooks.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.MemberAddRequest;
import me.croco.eatingBooks.repository.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signup(MemberAddRequest request) {
        return memberRepository.save(request.toEntity());

    }
}
