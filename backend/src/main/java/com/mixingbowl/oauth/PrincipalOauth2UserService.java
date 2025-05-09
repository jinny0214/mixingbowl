package com.mixingbowl.oauth;

import com.mixingbowl.auth.PrincipalDetails;
import com.mixingbowl.user.domain.Users;
import com.mixingbowl.user.repository.UserRepository;
import com.mixingbowl.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("PrincipalOauth2UserService loadUser   !! ");
        //userRequest 정보 -> loadUser 호출 -> 구글로부터 회원 프로필 받아옴
        //userRequest: 구글 로그인 클릭 -> 구글 로그인 화면 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        //loadUser: 회원 프로필 받아야함

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(userRequest.getClientRegistration());
        System.out.println(oAuth2User);
        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();



        if ("google".equals(provider)) {
            log.info("google login");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }
        System.out.println(oAuth2UserInfo.getName());
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        // String loginId = provider + "_" +providerId;
        String name = oAuth2UserInfo.getName();

        Users user;
        Users findUser = userRepository.findByEmail(email);
        if (findUser != null) {
            user = findUser;
        } else {
            // 첫 로그인이면 유저 등록
            user = new Users();
            user.setName(name);
            user.setEmail(email);
            user.setProvider(provider);
            user.setProviderId(providerId);
            userService.join(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
