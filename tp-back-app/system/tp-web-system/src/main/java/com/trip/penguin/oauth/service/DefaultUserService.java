package com.trip.penguin.oauth.service;

import com.trip.penguin.constant.CommonConstant;
import com.trip.penguin.constant.CommonUserRole;
import com.trip.penguin.constant.OAuthVendor;
import com.trip.penguin.security.dto.SignUpDTO;
import com.trip.penguin.user.domain.UserMS;
import com.trip.penguin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserService extends AbstractOAuth2UserService {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUpUser(SignUpDTO signUpDTO) {

        userService.signUpUser(
                UserMS.builder()
                        .userCity(signUpDTO.getUserCity())
                        .userEmail(signUpDTO.getUserEmail())
                        .userPwd(bCryptPasswordEncoder.encode(signUpDTO.getUserPwd()))
                        .userFirst(signUpDTO.getUserFirst())
                        .userLast(signUpDTO.getUserLast())
                        .userNick(signUpDTO.getUserNick())
                        .offYn(CommonConstant.N.getName())
                        .userRole(CommonUserRole.ROLE_USER.getUserRole())
                        .socialProvider(OAuthVendor.DEFAULT.getOAuthVendor())
                        .build());
    }
}