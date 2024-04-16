package me.croco.eatingBooks.repository;

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

    @Override
    public List<Article> findByIsbnOrderByCreatedAtDesc(String isbn) {
        QArticle qArticle = QArticle.article;
        return jpaQueryFactory.selectFrom(qArticle)
                .where(qArticle.isbn.eq(isbn), qArticle.publicYn.eq("true"))
                .orderBy(qArticle.createdAt.desc())
                .fetch();
    }

//
//    @Override
//    public List<Article> findPublicArticlesByMember(String email) {
//        QArticle qArticle = QArticle.article;
//        return jpaQueryFactory.selectFrom(qArticle)
//                .where(qArticle.writer.eq(email), qArticle.publicYn.eq("true"))
//                .orderBy(qArticle.createdAt.desc())
//                .fetch();
//    }

    @Override
    public List<Article> findAllArticlesByMemberId(Long id) {
        return jpaQueryFactory.selectFrom(QArticle.article)
                .innerJoin(QMember.member)
                .on(QArticle.article.writer.eq(QMember.member.email), QMember.member.id.eq(id))
                .fetch();
    }

}
