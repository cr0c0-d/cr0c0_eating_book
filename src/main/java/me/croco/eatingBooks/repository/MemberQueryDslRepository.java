package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Member;

import java.util.List;

public interface MemberQueryDslRepository {

    public List<Member> findAllOrderByCreatedAtDesc();

    void updateMemberStateDelete(Long id);
}
