package com.mixingbowl.config;

import com.mixingbowl.auth.AuthSuccessHandler;
import com.mixingbowl.filter.JwtAuthenticationFilter;
import com.mixingbowl.oauth.OAuth2SuccessHandler;
import com.mixingbowl.oauth.PrincipalOauth2UserService;
import com.mixingbowl.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록
// @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 프론트에서 처리하므로 비활성화
                .cors(Customizer.withDefaults()) // CORS 설정 허용 (Vue 요청 허용)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 기반이라면 STATELESS
                // 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/oauth2/**", "/api/user/**").permitAll() // 로그인/회원가입 허용
                        .anyRequest().authenticated())
                // OAuth2 인증 후 사용자 정보를 받아오는 과정에서 사용할 커스텀 OAuth2UserService 등록(사용자 정보 DB에 저장 및 세션 바인딩 등 처리)
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized2\"}");
                        })));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws  Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

}
