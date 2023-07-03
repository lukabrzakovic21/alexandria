package com.master.alexandria.controller;

import com.master.alexandria.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path  = "/jwt/valid")
public class JwtController {

    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(JwtController.class);

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<Boolean> isJwtValid(@RequestHeader("Authorization") String jwt) {
        logger.info("Checking is jwt token valid.");
        return ok(jwtService.isValid(jwt));
    }
}
