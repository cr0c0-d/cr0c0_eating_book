package me.croco.eatingBooks.repository;

import me.croco.eatingBooks.domain.Article;

import java.util.List;

public interface ArticleQueryDSLRepository {

    List<Article> findPublicArticlesByIsbnOrderByCreatedAtDesc(String isbn);

    List<Article> findAllArticlesByMemberId(Long id);

    List<String> findUpcomingReadingIsbnListByMemberEmail(String email, List<String> doneIsbnList);

    List<String> findDoneReadingIsbnListByMemberEmail(String email);

    void updateAllWriterByMemberEmail(Long id, String email);
}
