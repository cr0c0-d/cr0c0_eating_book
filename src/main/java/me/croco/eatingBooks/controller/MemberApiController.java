package me.croco.eatingBooks.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.MemberAddRequest;
import me.croco.eatingBooks.dto.MemberFindResponse;
import me.croco.eatingBooks.dto.MemberUpdateRequest;
import me.croco.eatingBooks.service.MemberService;
import me.croco.eatingBooks.util.Authorities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody MemberAddRequest request) {
        try {
            Long memberId = memberService.saveMember(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("회원가입 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("중복된 이메일");
        }
    }

    @GetMapping("/api/members")
    public ResponseEntity<List<Member>> findAll() {
        // 로그인 사용자가 ADMIN인지 확인
        String loginUserAuthority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0);

        if(!loginUserAuthority.equals(Authorities.ROLE_ADMIN.getAuthorityName())) { // admin 아님 -> 조회 권한 없음

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        } else {

            List<Member> memberList = memberService.findAll();
            return ResponseEntity.ok()
                    .body(memberList);

        }
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<MemberFindResponse> findMemberById(@PathVariable Long id) {
        Member member = memberService.findById(id);

        Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 사용자가 admin이 아니고, 로그인 사용자의 정보를 조회하는 것도 아닌 경우
        if(!loginUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().get(0).equals(Authorities.ROLE_ADMIN.getAuthorityName())
                &&
                !member.getEmail().equals(loginUser.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();

        } else {    // admin이거나, 본인 정보를 조회하는 경우
            return ResponseEntity.ok()
                    .body(new MemberFindResponse(member));
        }
    }

    @PutMapping("/api/members")
    public ResponseEntity<String> updateMember(@RequestBody MemberUpdateRequest request) {
        Long memberId = memberService.updateMember(request);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/api/members/{id}")
    public ResponseEntity deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok()
                .build();
    }

//    @GetMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//
//        return ResponseEntity.ok()
//                .body("로그아웃 완료");
//    }

}
