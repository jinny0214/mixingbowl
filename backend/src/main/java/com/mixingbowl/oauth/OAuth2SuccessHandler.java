package com.mixingbowl.oauth;

import com.mixingbowl.auth.PrincipalDetails;
import com.mixingbowl.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private static final String REDIRECT_URL = "http://localhost:5173/oauth2/redirect";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final int COOKIE_MAX_AGE = 60 * 60; // 1시간

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        try {
            PrincipalDetails authUser = (PrincipalDetails) authentication.getPrincipal();

            String email = authUser.getAttribute("email");

            log.info("OAuth2 로그인 성공: {}", email);
            log.info("신규 사용자 여부: {}", authUser.isNewUser());

            // 쿠키에 JWT 저장
            if (!authUser.isNewUser()) {
                String token = jwtUtil.generateToken(email);

                setAccesTokenCookie(response, token);
            }

            // 프론트엔드로 리다이렉트
            String redirectUrl = buildRedirectUrl(email, authUser.isNewUser());
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            log.info("OAuth2 리다이렉트 완료 {}", email);

        } catch (Exception e) {
            log.error("OAuth2 인증 처리 중 오류 발생: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다.");
        }
    }

    private void setAccesTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
        log.debug("Access token cookie set for user");
    }

    private String buildRedirectUrl(String email, boolean isNewUser) {
        return REDIRECT_URL + "?email=" + email + "&isNewUser=" + isNewUser;
    }
}
