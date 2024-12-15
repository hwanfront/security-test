package com.example.spring_security_test.config;

import com.example.spring_security_test.domain.member.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

// SecurityConfig 자원별 접근 권한 설정
// 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
// 내부적으로 SpringSecurityFilterChain이 동작하여 URL 필터 적용
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Cross site Request forger 설정
                // CSRF 란 사용자가 신뢰하는 웹사이트에 대해 악의적인 요청을 실행하도록 속이는 공격 방식
                // CSRF 방어를 끄면 클라이언트가 CSRF 토큰을 포함하지 않아도 서버에서 요청을 허용함
                // REST API 는 일반적으로 세션이나 브라우저 기반 인증을 사용하지 않음
                // 대신 JWT(JSON Web Token)이나 OAuth 를 사용하는 경우가 많아 CSRF 보호가 불필요함
                .csrf(AbstractHttpConfigurer::disable)
                // X-Frame-Options 헤더 설정
                // 브라우저가 웹페이지를 <iframe>, <frame>, 또는 <object> 태그로 렌더링할 수 있는지 제어하는 보안 HTTP 헤더
                // 클릭재킹 Clickjacking 공격을 방지하는 데 사용함,
                // 근데 이거 작성하면 보안에 문제가 되는데, 굳이 작성한 이유: h2-console 화면을 사용하기 위해 해당 옵션들을 disable 한 것.
                // => 프로덕트 환경에서는 지워서 다시 보안 헤더를 추가하자.
                .headers(headerConfig -> headerConfig.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                ))
                // HTTP 요청에 대한 권한 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // h2-console 사용하기 위한 설정
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                // 특정 역할을 가진 사용자만 접근 가능
                                .requestMatchers("/guest/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name(), Role.GUEST.name())
                                .requestMatchers("/user/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                                // 인증 없이 접근 가능한 경로들
                                .requestMatchers("/", "/login/**", "/join", "/api/**").permitAll()
                                // 그 외의 모든 요청은 인증 필요 (접근 가능하게 하려면 .authenticated())
                                .anyRequest().permitAll()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginProcessingUrl("/api/login")
                                .defaultSuccessUrl("/", true)

                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .authenticationEntryPoint(unauthorizedEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessUrl("/") // 6번
                );
        return http.build();
    }


    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect("401");
            };

    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendRedirect("403");
            };

    @Getter
    @RequiredArgsConstructor
    public class ErrorResponse {

        private final HttpStatus status;
        private final String message;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
