package com.mixingbowl.user.controller;

import com.mixingbowl.auth.PrincipalDetails;
import com.mixingbowl.user.domain.UserDto;
import com.mixingbowl.user.domain.Users;
import com.mixingbowl.user.service.UserService;
import com.mixingbowl.util.ApiResponse;
import com.mixingbowl.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody UserDto userDto) {
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.join(user);

        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }

    @PostMapping("/user/login")
    
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserDto userDto, HttpServletResponse response) {
        log.info("Login attempt for email: {}", userDto.getEmail());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        Authentication auth = authenticationManager.authenticate(token);
        PrincipalDetails principalDetails = (PrincipalDetails) auth.getPrincipal();

        log.info("PrincipalDetails: {}", principalDetails);
        log.info("PrincipalDetails username: {}", principalDetails.getUsername());
        log.info("PrincipalDetails authorities: {}", principalDetails.getAuthorities());

        String jwt = jwtUtil.generateToken(principalDetails.getUsername());
        log.info("Generated JWT token: {}", jwt);

        Cookie cookie = new Cookie("access_token", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        log.info("Cookie set in response: {}", cookie.getName());
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", principalDetails.getUsername()));

    }

    /*
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<Users>> login(@RequestBody UserDto userDto) {
        System.out.println("userDto : " + userDto.toString());
        return userService.findUser(userDto.getEmail())
                .filter(user -> userService.checkPassword(userDto.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(userDto.getEmail());
                    //return ResponseEntity.ok(ApiResponse.success("로그인 성공", user));
                    return ResponseEntity
                            .ok()
                            .header("Authorization", "Bearer " + token)
                            .body(ApiResponse.success("로그인 성공", user));

                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("이메일 또는 비밀번호가 틀림")));
    }
     */

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Users>> getUser(@RequestHeader("Authorization") String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Authorization header is missing or malformed."));
        }


        String token = header.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Invalid token"));
        }

        String email = jwtUtil.getEmailFromToken(token);

        return userService.findUser(email)
                .map(user -> ResponseEntity.ok(ApiResponse.success("유저 조회 성공", user)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.fail("Not found User")));
    }

    @GetMapping("/user/check")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("GET /user/me called");

        if (principalDetails == null) {
            log.warn("PrincipalDetails is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized - No valid authentication"));
        }

        // 사용자 정보 추출
        try {
            String email = principalDetails.getUsername();
            log.info("User authenticated: {}", email);
            return ResponseEntity.ok(ApiResponse.success("성공", email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Error processing request"));
        }

    }
}
