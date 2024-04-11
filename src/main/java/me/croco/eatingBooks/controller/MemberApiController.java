package me.croco.eatingBooks.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.MemberAddRequest;
import me.croco.eatingBooks.dto.MemberFindResponse;
import me.croco.eatingBooks.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public ResponseEntity<String> signUp(@RequestBody MemberAddRequest request) {
        Long memberId = memberService.saveMember(request);

        if(memberId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("중복된 이메일");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("회원가입 완료");
        }
    }

    @GetMapping("/api/members")
    public ResponseEntity<List<Member>> findAll() {
        try {
            List<Member> memberList = memberService.findAll();
            return ResponseEntity.ok()
                    .body(memberList);

        } catch(AccessDeniedException e) {  // 권한 없음(admin 아님)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<MemberFindResponse> findMember(@PathVariable Long id) {
        try {
            Member member = memberService.findById(id);
            return ResponseEntity.ok()
                    .body(new MemberFindResponse(member));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

//    @GetMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//
//        return ResponseEntity.ok()
//                .body("로그아웃 완료");
//    }

}
