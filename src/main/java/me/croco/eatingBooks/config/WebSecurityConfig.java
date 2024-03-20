//package me.croco.eatingBooks.config;
//
//import lombok.RequiredArgsConstructor;
//import me.croco.eatingBooks.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
//    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
//
//    // 스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/static/**");
//    }
//
//    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
//
//         http
//                .authorizeHttpRequests(authorize ->
//                        authorize.requestMatchers("/signup", "/user").permitAll()
//                                .anyRequest().authenticated()
//                )
//                 .formLogin(form ->
//                         form.loginPage("/login")
//                                 .loginProcessingUrl("/loginProcessing")
//                                 .successHandler(customAuthenticationSuccessHandler)
//                                 .failureHandler(customAuthenticationFailureHandler)
//                                 .usernameParameter("email")
//                 )
//                 .logout(logout ->
//                         logout.logoutSuccessUrl("/login")
//                                 .invalidateHttpSession(true)
//                 )
//                 .csrf().disable()
//                 .cors().configurationSource(corsConfigurationSource());
//
//         return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, MemberService memberService) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(memberService)
//                .passwordEncoder(bCryptPasswordEncoder)
//                .and()
//                .build();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("http://localhost:3000"); // 허용할 오리진 설정
//        configuration.addAllowedOrigin("http://25.10.86.27:3000");
//        configuration.addAllowedOrigin("http://192.168.0.2:3000");
//        configuration.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
//        configuration.addAllowedHeader("*"); // 모든 헤더 허용
//        configuration.setAllowCredentials(true); // 쿠키 허용
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
//        return source;
//    }
//
//
//}
