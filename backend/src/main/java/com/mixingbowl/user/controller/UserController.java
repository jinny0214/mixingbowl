package com.mixingbowl.user.controller;

import com.mixingbowl.auth.PrincipalDetails;
import com.mixingbowl.user.domain.UserDto;
import com.mixingbowl.user.domain.Users;
import com.mixingbowl.user.service.UserService;
import com.mixingbowl.util.ApiResponse;
import com.mixingbowl.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.function.LpadRpadPadEmulation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody UserDto userDto) {
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.join(user);

        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<Users>> login(@RequestBody UserDto userDto) {
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

    @GetMapping("/user/me")
    public ResponseEntity<ApiResponse<String>> getUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            throw new RuntimeException("로그인된 사용자가 없습니다.");
        }

        // 사용자 정보 추출
        String email = principalDetails.getUsername();

        return ResponseEntity.ok(ApiResponse.success("OAuth2 로그인 성공", email));
    }
}
