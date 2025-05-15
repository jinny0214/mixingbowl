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
//    private static final String REDIRECT_URL = "http://localhost:5173/oauth2/redirect?token=";
    private static final String REDIRECT_URL = "http://localhost:5173/oauth2/redirect";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        try {
            PrincipalDetails authUser = (PrincipalDetails) authentication.getPrincipal();

            String email = authUser.getAttribute("email");

            log.info("OAuth2 로그인 성공: {}", email);

            String token = jwtUtil.generateToken(email);

            // 쿠키에 JWT 저장
            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // https -> true
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1시간

            response.addCookie(cookie);

            // 프론트엔드로 리다이렉트
            //String redirectUrl = REDIRECT_URL + token;
            getRedirectStrategy().sendRedirect(request, response, REDIRECT_URL);
            log.info("OAuth2 리다이렉트 완료 {}", email);

        } catch (Exception e) {
            log.error("OAuth2 인증 처리 중 오류 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다.");
        }
    }
}
