package com.mixingbowl.auth;

import com.mixingbowl.user.domain.Users;
import com.mixingbowl.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("PrincipalDetailService  loadUserByUsername.. email : " + email);

        Users findUser = userRepository.findByEmail(email);
        if (findUser != null) {
            return new PrincipalDetails(findUser);
        }
        return null;
    }
}
