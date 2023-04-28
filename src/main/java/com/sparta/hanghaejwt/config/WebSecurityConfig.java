package com.sparta.hanghaejwt.config;

import com.sparta.hanghaejwt.jwt.JwtAuthFilter;
import com.sparta.hanghaejwt.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    // 비밀번호 암호화 기능
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 게시글 하나보기 및 게시글 전체 보기는 인증 없이 확인 가능, 그 이외는 인증 필요
        http.authorizeRequests()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers("/board/list").permitAll()
                .antMatchers("/board/{id}").permitAll()
                .anyRequest().authenticated()
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//        http.formLogin().loginPage("/api/user/login-page").permitAll();  // 로그인 실패시 Custom 로그인 페이지로 이동
//        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");

        // 접근 제한 페이지 이동 설정
//        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
        return http.build();
    }

}


/*
csrf : 사이트간 요청 위조 (Cross-site request forgery) -> disable 로 처리
공격자가 인증이된 브라우저에 저장된 쿠킥의 세션 정보를 활용하여 웹서버에 사용자가 의도하지 않은 요청 전달

permitAll() : 전부 허용 -> 이러한 설정은 매우 다양하다.
 */