package com.prozacto.vault.controller;

import com.prozacto.vault.exception.InvalidRefreshTokenException;
import com.prozacto.vault.repository.UserRepository;
import com.prozacto.vault.security.SecurityConstants;
import com.prozacto.vault.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationServerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenUtil tokenUtil;


    @PostMapping("/refresh-token")
    public String refreshToken(@RequestBody String token) throws InvalidRefreshTokenException {
        String newToken = tokenUtil.refreshToken(token.replace(SecurityConstants.HEADER_STRING, ""));
        return newToken;
    }
}
