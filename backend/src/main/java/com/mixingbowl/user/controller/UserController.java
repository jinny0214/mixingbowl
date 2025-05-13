package com.mixingbowl.user.controller;

import com.mixingbowl.user.domain.UserDto;
import com.mixingbowl.user.domain.Users;
import com.mixingbowl.user.service.UserService;
import com.mixingbowl.util.ApiResponse;
import com.mixingbowl.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody UserDto userDto) {
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userService.join(user);

        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserDto userDto) {
        return userService.findUser(userDto.getEmail())
                .filter(user -> userService.checkPassword(userDto.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(userDto.getEmail());
                    return ResponseEntity.ok(ApiResponse.success("로그인 성공", token));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("이메일 또는 비밀번호가 틀림")));
    }
}
