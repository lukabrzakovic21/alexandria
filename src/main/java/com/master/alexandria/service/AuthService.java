package com.master.alexandria.service;

import com.master.alexandria.client.IstanbulClient;
import com.master.alexandria.common.dto.AuthRequestDTO;
import com.master.alexandria.exception.custom.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final IstanbulClient istanbul;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);


    public AuthService(JwtService jwtService, IstanbulClient istanbul) {
        this.jwtService = jwtService;
        this.istanbul = istanbul;
    }

    public String generateToken(AuthRequestDTO authRequestDTO) {
        logger.info("Authentication flow started");
        var authentication = istanbul.authenticateUser(authRequestDTO);
         if(authentication.isSuccessfulLogin()) {
             return jwtService.generateToken(authRequestDTO.getEmail(), authentication.getRole(), authentication.getPublicId());
         }
         else {
             logger.error("User with email: {} doesn't exist.", authRequestDTO.getEmail());
             throw new UserNotFoundException(String.format("User with email %s cannot be authorized.", authRequestDTO.getEmail()));
         }
    }

    public boolean logout(String jwt) {
        return jwtService.logout(jwt);
    }
}
