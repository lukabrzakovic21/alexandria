package com.master.alexandria.controller;

import com.master.alexandria.common.dto.AuthRequestDTO;
import com.master.alexandria.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/auth")
public class AuthRequestController {

    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(AuthRequestController.class);

    public AuthRequestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<String> generateJwtToken(@RequestBody AuthRequestDTO authRequestDTO) {
        logger.info("Generate jwt token for email: {}", authRequestDTO.getEmail());
        return ok(authService.generateToken(authRequestDTO));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String jwt) {
        logger.info("Logout flow started");
        return ok(authService.logout(jwt));

    }
}
