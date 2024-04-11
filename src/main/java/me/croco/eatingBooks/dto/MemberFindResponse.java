package me.croco.eatingBooks.dto;

import me.croco.eatingBooks.domain.Member;

import java.time.format.DateTimeFormatter;

public class MemberFindResponse {
    private Long id;
    private String email;
    private String nickname;
    private String createdAt;

    public MemberFindResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        this.createdAt = formatter.format(member.getCreatedAt());
    }
}
