package me.croco.eatingBooks.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.QArticle;
import me.croco.eatingBooks.domain.QMember;

import java.util.List;

public class ArticleQueryDSLRepositoryImpl implements ArticleQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ArticleQueryDSLRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private final QMember qMember = QMember.member;

    private final QArticle qArticle = QArticle.article;

    @Override
    public List<Article> findPublicArticlesByIsbnOrderByCreatedAtDesc(String isbn) {

        return jpaQueryFactory.selectFrom(qArticle)
                .where(qArticle.isbn.eq(isbn), qArticle.publicYn.eq("true"))
                .orderBy(qArticle.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Article> findAllArticlesByMemberId(Long id) {
        return jpaQueryFactory.selectFrom(qArticle)
                .innerJoin(qMember)
                .on(QArticle.article.writer.eq(qMember.email), qMember.id.eq(id))
                .orderBy(qArticle.createdAt.desc())
                .fetch();
    }

    @Override
    public List<String> findUpcomingReadingIsbnListByMemberEmail(String email, List<String> doneIsbnList) {
        // 작성자 체크
        BooleanExpression isWriter = qArticle.writer.eq(email);

        // 식전문
        BooleanExpression isTypeB = qArticle.articleType.eq("B");

        return jpaQueryFactory.select(qArticle.isbn)
                .from(qArticle)
                .where(isWriter
                        .and(isTypeB)
                        .and(qArticle.isbn.notIn(doneIsbnList))
                )
                .orderBy(qArticle.createdAt.desc())
                .fetch();
    }

    @Override
    public List<String> findDoneReadingIsbnListByMemberEmail(String email) {
        // 작성자 체크
        BooleanExpression isWriter = qArticle.writer.eq(email);

        // 식후문
        BooleanExpression isTypeA = qArticle.articleType.eq("A");

        return jpaQueryFactory.select(qArticle.isbn)
                .from(qArticle)
                .where(isWriter.and(isTypeA))
                .orderBy(qArticle.createdAt.desc())
                .fetch();
    }
}
