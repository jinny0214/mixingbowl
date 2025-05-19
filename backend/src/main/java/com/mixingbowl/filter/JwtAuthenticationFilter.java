package com.mixingbowl.filter;

import com.mixingbowl.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.debug("JwtAuthenticationFilter - Request URI: {}", request.getRequestURI());

        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.debug("JwtFilter cookie {}", cookie.getName());
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    log.debug("Found access_token in cookie: {}", token);
                    break;
                }
            }
        }

        if (token != null) {
            log.debug("Validating token..");
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.getEmailFromToken(token);
                log.debug("Token is valid. Email from token: {}", email);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                log.debug("Loaded user details for email: {}", email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.debug("Token validatin failed");
            }
        } else {
            log.debug("No access_token found in cookies");
        }


        filterChain.doFilter(request, response);
    }
}
