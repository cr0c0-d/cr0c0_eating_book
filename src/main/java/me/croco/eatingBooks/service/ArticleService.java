package me.croco.eatingBooks.service;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.ArticleTemplate;
import me.croco.eatingBooks.domain.Member;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.dto.ArticleUpdateRequest;
import me.croco.eatingBooks.repository.ArticleRepository;
import me.croco.eatingBooks.repository.ArticleTemplatesRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTemplatesRepository articleTemplatesRepository;

    // 글 작성
    public Article save(ArticleAddRequest request) {
        request.setWriter(SecurityContextHolder.getContext().getAuthentication().getName());
        return articleRepository.save(request.toEntity());
    }

    // 글 조회
    public Article findById(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글 찾을 수 없음 : " + id));
        if (article.getPublicYn().equals("false")) { // 비공개 글인 경우
            if(article.getWriter().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new AuthenticationServiceException("조회 권한 없음");
            }
        }
        return article;
    }

    // 글 목록 조회
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    // 글 수정
    @Transactional
    public Article update(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글 찾을 수 없음 : " + id));

        article.update(request.getTitle(), request.getContent(), request.getWriteType(), request.getPublicYn());

        return article;
    }

    // 글 삭제
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    public List<ArticleTemplate> findTemplateByType(String type) {
        return articleTemplatesRepository.findByTypeOrderByNumAsc(type);
    }

    /**
     * 글 유형을 받아 템플릿을 Map으로 반환(글 조회 화면용)
     * {템플릿 ID : 템플릿 내용}
     * @param type
     * @return
     */
    public Map<Long, String> findTemplateMapByType(String type) {
        Map<Long, String> returnMap = new HashMap<>();
        List<ArticleTemplate> articleTemplateList = articleTemplatesRepository.findByTypeOrderByNumAsc(type);
        for (ArticleTemplate at : articleTemplateList) {
            returnMap.put(at.getId(), at.getContent());
        }
        return returnMap;
    }

}
