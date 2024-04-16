package me.croco.eatingBooks.dto;

import lombok.Getter;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.Member;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class MemberArticleFindResponse {
    private String nickname;
    private String profileImg;
    private int articlesAllCount;

    private List<ArticleListResponse> publicArticleList;
    private List<ArticleListResponse> privateArticleList;

    public MemberArticleFindResponse(Member member, List<Article> articleList, boolean includePrivateArticles) {
        this.nickname = member.getNickname();
        this.profileImg = member.getProfileImg();
        this.articlesAllCount = articleList.size();
        this.publicArticleList = articleList.stream().filter((article) -> article.getPublicYn().equals("true")).map((article) -> new ArticleListResponse(article, member.getNickname())).toList();

        this.privateArticleList =
                includePrivateArticles
                ? articleList.stream().filter((article) -> article.getPublicYn().equals("false")).map((article) -> new ArticleListResponse(article, member.getNickname())).toList()
                : null;
    }
}
