package com.mixingbowl.oauth;

import com.mixingbowl.auth.PrincipalDetails;
import com.mixingbowl.util.JwtUtil;
import jakarta.servlet.ServletException;
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
    private static final String REDIRECT_URL = "http://localhost:5173/oauth2/redirect?token=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        String token = jwtUtil.generateToken(email);

        // 프론트엔드로 리다이렉트
        String redirectUrl = REDIRECT_URL + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
