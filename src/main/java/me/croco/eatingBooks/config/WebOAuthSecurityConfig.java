package me.croco.eatingBooks.config;

import lombok.RequiredArgsConstructor;
import me.croco.eatingBooks.config.jwt.TokenProvider;
import me.croco.eatingBooks.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.croco.eatingBooks.config.oauth.OAuth2UserCustomService;
import me.croco.eatingBooks.repository.RefreshTokenRepository;
import me.croco.eatingBooks.service.MemberService;
import me.croco.eatingBooks.util.HttpHeaderChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final HttpHeaderChecker httpHeaderChecker;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {

        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/signup", "/login", // 로그인
                                        "/api/token", // 토큰 발급
//                                        "/api/books", // 책 검색
//                                        "/api/books/{id}",  // 책 상세 정보 조회
                                        "/api/books",   //책 검색
                                        "/api/books/**",    // 책 하위 API
                                        "/api/articles", // 글 목록 조회
                                        "/api/articles/book/{isbn}" // 도서별 글 조회
                                ).permitAll()

                                .requestMatchers(
                                        new AntPathRequestMatcher("/api/articles/{id}", HttpMethod.GET.name()), // 글 조회만 허용
                                        new AntPathRequestMatcher("/api/members/", HttpMethod.POST.name())  // 회원가입만 허용
                                ).permitAll()

                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()

                )

                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler())
                    .clearAuthentication(true)
                .and()

                .formLogin(form ->
                            form.loginPage("/login")
                                    .loginProcessingUrl("/loginProcessing")
                                    .successHandler(customAuthenticationSuccessHandler())
                                    .failureHandler(customAuthenticationFailureHandler)
                                    .usernameParameter("email")
                            )
                .csrf().disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic().disable();


        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
//        http.authorizeHttpRequests((authorize) ->
//                authorize.requestMatchers("/api/token", "/signup").permitAll()
//                        .requestMatchers("/api/**").authenticated()
//                        .anyRequest().permitAll()
//                );

        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                //Authorization 요청과 관련된 상태 저장
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(customAuthenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.logout()
                .logoutSuccessUrl("/login");

        // api로 시작하는 url인 경우 401 상태코드를 반환하도록 예외 처리
        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), new AntPathRequestMatcher("/api/**"));

        return http.build();

    }


//    @Bean
//    public OAuth2SuccessHandler oAuth2SuccessHandler() {
//        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), memberService);
//    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, MemberService memberService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(memberService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(httpHeaderChecker);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), memberService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // 허용할 오리진 설정
        configuration.addAllowedOrigin("http://25.10.86.27:3000");
        configuration.addAllowedOrigin("http://192.168.0.2:3000");
        configuration.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        configuration.addAllowedHeader("Content-Type"); // 헤더 허용
        configuration.addAllowedHeader("Authorization"); // 헤더 허용
        configuration.setAllowCredentials(true); // 쿠키 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }



}
