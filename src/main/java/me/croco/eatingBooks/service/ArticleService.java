package me.croco.eatingBooks.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.config.jwt.TokenProvider;
import me.croco.eatingBooks.domain.Article;
import me.croco.eatingBooks.domain.ArticleTemplate;
import me.croco.eatingBooks.dto.ArticleAddRequest;
import me.croco.eatingBooks.dto.ArticleUpdateRequest;
import me.croco.eatingBooks.repository.ArticleRepository;
import me.croco.eatingBooks.repository.ArticleTemplatesRepository;
import me.croco.eatingBooks.util.HttpHeaderChecker;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleTemplatesRepository articleTemplatesRepository;
    private final HttpHeaderChecker httpHeaderChecker;

    // 글 작성
    public Article save(ArticleAddRequest request) {
        request.setWriter(SecurityContextHolder.getContext().getAuthentication().getName());
        return articleRepository.save(request.toEntity());
    }

    // 글 조회
    public Article findById(long id, HttpServletRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글 찾을 수 없음 : " + id));

        if (article.getPublicYn().equals("false")) { // 비공개 글인 경우
            // 로그인 상태인지 확인
            boolean validToken = httpHeaderChecker.checkAuthorizationHeader(request);

            if (!validToken) {   // 비로그인 상태
                throw new AuthenticationCredentialsNotFoundException("로그인 필요");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 로그인 상태인 경우
            if(!article.getWriter().equals(authentication.getName())) { //로그인 사용자와 작성자 비교
                throw new AccessDeniedException("조회 권한 없음");
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
