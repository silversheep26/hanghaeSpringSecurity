package com.sparta.hanghaejwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaejwt.dto.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    // 인증처리하는 부분 : 수정 삭제 와 같이 인증이 필요할 때 사용
    // 모든 부분에서 permitAll 이 아니기 때문에 분기 처리를 해주어야 로그인, 회원가입, 게시글 확인 등에서 인증 처리 안 하고 넘어갈 수 있다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // token 가져 오기
        String token = jwtUtil.resolveToken(request);

        // 토큰이 request Header 에 있는지 없는지 확인
        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value()); // 아래의 메서드로 반환
                return;
            }
            // 토큰 에러가 없다면 유저의 정보를 가져온다.
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        // 여기까지 오면 모든 인증이 완료 된 것으로 Security 가 인지하고 Controller 에 요청이 넘어간다.
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 토큰 오류 났을 때 Client 로 커스터마이징 하여 예외 처리 값을 알려주는 메소드
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
