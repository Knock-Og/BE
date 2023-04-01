package com.project.comgle.global.config;

import com.project.comgle.global.exception.CustomAccessDeniedHandler;
import com.project.comgle.global.filter.JwtAuthFilter;
import com.project.comgle.global.utils.JwtUtil;
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
import org.springframework.web.cors.CorsConfiguration;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    //.requestMatchers(PathRequest.toH2Console())
                    .antMatchers("/h2-console/**", "/swagger-ui/**",  "/v3/api-docs/**","/api-docs/**")
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //  Spring Security 필터 체인을 구성하는 인터페이스다.

        http.cors().configurationSource(request -> {
            CorsConfiguration cors = new CorsConfiguration();

            cors.setAllowedOriginPatterns(Arrays.asList("*"));

            cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
            cors.setAllowedHeaders(Arrays.asList("*"));
            cors.addExposedHeader("Authorization");
            cors.setAllowCredentials(true);
            return cors;
        });

        http.csrf().disable(); // CSRF 방어 기능을 비활성화한다.

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests() //  요청에 대한 보안 검사를 구성한다.
                .antMatchers("/login").permitAll()
                .antMatchers("/check/**").permitAll()
                .antMatchers("/mail/auth").permitAll()
                .antMatchers("/member/pwd/**").permitAll()
                .antMatchers("/member/email").permitAll()
                .antMatchers("/sms").permitAll()
                .antMatchers("/connect/**").permitAll()
                .anyRequest().authenticated() // 나머지 URL에 대한 접근 권한을 설정합니다. 인증된 사용자만 접근할 수 있다.
                // JWT 인증/인가를 사용하기 위한 설정
                // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정한다. JwtAuthFilter는 JWT 토큰을 검증하고 인증/인가를 처리한다.
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
//        http.formLogin().loginPage("/api/user/login-page").permitAll(); //로그인 페이지를 설정한다

        return http.build();
    }

}
