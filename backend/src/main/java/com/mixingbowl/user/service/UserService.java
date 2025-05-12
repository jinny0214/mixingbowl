package com.mixingbowl.user.service;

import com.mixingbowl.user.domain.Users;
import com.mixingbowl.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

     private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long join(Users user) {
        validateDuplicateMember(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(Users user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(findUser -> {
                    throw new IllegalStateException("이미 존재하는 이메일입니다.");
                });
    }

    public List<Users> findUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> findUser(Long userId) {
        return userRepository.findOne(userId);
    }

    public Optional<Users> findUser(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
