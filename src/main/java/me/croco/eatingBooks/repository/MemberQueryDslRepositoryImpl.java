package me.croco.eatingBooks.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.domain.QMember;
import me.croco.eatingBooks.util.Authorities;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QMember qMember = QMember.member;

    @Value("${default.profileImg}")
    private String DEFAULT_PROFILE_IMAGE;

    @Override
    public List<Member> findAllOrderByCreatedAtDesc() {

        return jpaQueryFactory.selectFrom(qMember)
                .orderBy(qMember.createdAt.desc())
                .fetch();
    }

    @Override
    public void updateMemberStateDelete(Long id) {
        jpaQueryFactory.update(qMember)
                .set(qMember.email, String.valueOf(id))
                .set(qMember.authorities, Authorities.ROLE_DELETED)
                .set(qMember.profileImg, DEFAULT_PROFILE_IMAGE)
                .set(qMember.nickname, "탈퇴한 사용자")
                .set(qMember.password, "")
                .where(qMember.id.eq(id));

    }
}
