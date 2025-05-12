package com.mixingbowl.auth;

import com.mixingbowl.user.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Users user;
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(Users user) {
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(Users user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        // collect.add((GrantedAuthority) () -> user.getName());

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //ex) 1년 동안 회원이 로그인을 하지 않으면 => 휴면 계정
        //    현재 시간 - 로그인 시간 => 1년을 초과하면 false
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
