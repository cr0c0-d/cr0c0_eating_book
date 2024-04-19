package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryDslRepository {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
