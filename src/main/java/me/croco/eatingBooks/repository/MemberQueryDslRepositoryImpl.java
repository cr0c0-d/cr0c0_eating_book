package me.croco.eatingBooks.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.domain.QMember;

import java.util.List;

@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;



    @Override
    public List<Member> findAllOrderByCreatedAtDesc() {
        QMember qMember = QMember.member;

        return jpaQueryFactory.selectFrom(qMember)
                .orderBy(qMember.createdAt.desc())
                .fetch();
    }
}
