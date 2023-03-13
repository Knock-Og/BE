package com.project.comgle.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.comgle.exception.ErrorResponse;
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

@Slf4j // lombok 라이브러리에서 제공하는 어노테이션으로 로깅을 위한 코드를 쉽게 작성할 수 있도록 해준다.
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){ // JWT 토큰이 올바르지 않으면 예외를 처리를한다
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token); //  JWT 토큰으로부터 추출한 사용자 정보를 Claims 객체로 반환한다
            setAuthentication(info.getSubject()); // Security Context와 Authentication 객체를 생성한다
        }
        filterChain.doFilter(request,response); // 필터 체인의 다음 필터를 실행한다
    }

    public void setAuthentication(String username) { // Context와 Authentication 객체를 생성하는 메소드
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) { // 예외 발생시 처리하는 메서드
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(ErrorResponse.of(statusCode,msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
