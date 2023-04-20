package com.project.comgle.global.config;

import com.project.comgle.global.exception.CustomAccessDeniedHandler;
import com.project.comgle.global.filter.JwtAuthFilter;
import com.project.comgle.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                    .antMatchers("/h2-console/**", "/swagger-ui/**",  "/v3/api-docs/**","/api-docs/**", "/s3.amazonaws.com/**")
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().configurationSource(request -> {
            CorsConfiguration cors = new CorsConfiguration();

            cors.setAllowedOriginPatterns(Arrays.asList("*"));
            cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
            cors.setAllowedHeaders(Arrays.asList("*"));
            cors.addExposedHeader("Authorization");
            cors.setAllowCredentials(true);

            return cors;
        });

        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/login").permitAll()
                .antMatchers("/check/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/member/**").permitAll()
                .antMatchers("/connect/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }

}
