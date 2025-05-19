package com.mixingbowl.auth;

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
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private static final String REDIRECT_URL = "http://localhost:5173/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        try {
            PrincipalDetails authUser = (PrincipalDetails) authentication.getPrincipal();
            String email = authUser.getUsername();

            String token = jwtUtil.generateToken(email);

            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // HTTPS면 true
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);

            response.addCookie(cookie);

            getRedirectStrategy().sendRedirect(request, response, REDIRECT_URL);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다.");
        }
    }
}
