package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
