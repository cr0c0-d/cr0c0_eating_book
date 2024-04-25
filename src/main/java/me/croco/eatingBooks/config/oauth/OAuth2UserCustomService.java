package me.croco.eatingBooks.config.oauth;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.repository.MemberRepository;
import me.croco.eatingBooks.util.Authorities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Value("default.profileImg")
    private String DEFAULT_PROFILE_IMAGE;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    private Member saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
//        Member member = memberRepository.findByEmail(email)
//                .map(entity -> entity.update(name))
//                .orElse(Member.builder()
//                        .email(email)
//                        .nickname(name)
//                        .authorities(Authorities.ROLE_USER)
//                        .build()
//                );
//        return memberRepository.save(member);

        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElseGet(() -> memberRepository.save(Member.builder()
                .email(email)
                .nickname(name)
                .password("google")
                .authorities(Authorities.ROLE_USER)
                .profileImg(DEFAULT_PROFILE_IMAGE)
                .build()));
    }
}
