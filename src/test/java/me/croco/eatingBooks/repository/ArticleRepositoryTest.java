package me.croco.eatingBooks.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.QArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static me.croco.eatingBooks.domain.QArticle.article;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ArticleRepositoryTest {

    //private final JPAQueryFactory jpaQueryFactory;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        EntityManagerFactory emfMock = Mockito.mock(EntityManagerFactory.class);
        Mockito.when(entityManager.getEntityManagerFactory()).thenReturn(emfMock);
    }

    @Test
    @DisplayName("사용자 email을 받아 publicYn이 true인 Article을 CreatedAt 내림차 순으로 조회한다.")
    void queryDsl_findPublicArticlesByEmailOrderByCreatedAtDesc() {
        String writerEmail = "hyde69ciel@naver.com";



        QArticle qArticle = article;
        JPAQuery<?> query = new JPAQuery<Void>(entityManager);

        List<Article> articleList = query
                .select(article)
                .from(article)
                .where(article.writer.eq(writerEmail), article.publicYn.eq("true"))
                .orderBy(article.createdAt.desc())
                .fetch();

        assertThat(articleList.size()).isEqualTo(5);


    }
}
