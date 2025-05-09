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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/user/register")
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        log.info("register!! " + userDto.getEmail());
        try {
            Users user = new Users();
            user.setEmail(userDto.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userService.join(user);

            return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("회원가입 실패"));
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        Users user = userService.findUser(userDto.getEmail());

        if (user == null || !userService.checkPassword(userDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("이메일 또는 비밀번호가 틀림"));
        }
        String token = jwtUtil.generateToken(userDto.getEmail());

        return ResponseEntity.ok(ApiResponse.success("로그인 성공", token));
    }

}
