package com.master.alexandria.service;

import com.google.common.base.Strings;
import com.master.alexandria.common.model.JwtValid;
import com.master.alexandria.controller.AuthRequestController;
import com.master.alexandria.repository.JwtRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Component
public class JwtService {

    private final JwtRepository jwtRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthRequestController.class);


    public static Long EXPIRATION_TIME = 60L;
    private static String SECRET = "somesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkeysomesecretkey";

    public JwtService(JwtRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    public String generateToken(String email, String role) {

        //should be aware that user can get only one role
        var claims = new HashMap<String, String>();
        claims.put("role", role);
        return createJWT(email, claims);
    }

    private String createJWT(String email, HashMap<String, String> claims) {

        var validUntil = Instant.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES);
        var jwt =  Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(validUntil))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
        insertJwt(jwt, Timestamp.from(validUntil));
        return jwt;
    }

    private Key getSignKey() {
        var bytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isValid(String jwt) {

        if (Strings.isNullOrEmpty(jwt)
                || !jwt.startsWith("Bearer ")) {
            return false;
        }
        String token = jwt.replace("Bearer ", "");

        var jwtResult = jwtRepository.findByJwt(token);
        if(jwtResult.isEmpty()) {
            return false;
        }
        else {
            return jwtResult.get().isValid() && jwtResult.get().getValidUntil().after(Timestamp.from(Instant.now()));
        }
    }

    private void insertJwt(String jwt, Timestamp validUntil) {
        var jwtModel = JwtValid.builder()
                        .jwt(jwt)
                        .validUntil(validUntil)
                        .valid(true)
                         .build();
          jwtRepository.save(jwtModel);
    }

    //change cron job date
    @Scheduled(cron = "0 */2 * ? * *")
    @Transactional
    public void deleteInvalidJwts() {
        logger.info("Start delete in cron job at {}", Instant.now());
        jwtRepository.deleteAllByValidOrValidUntilBefore(false, Timestamp.from(Instant.now()));
    }

    public boolean logout(String jwt) {
        if (Strings.isNullOrEmpty(jwt)
                || !jwt.startsWith("Bearer ")) {
            return false;
        }
        String token = jwt.replace("Bearer ", "");

        var returnedJwt = jwtRepository.findByJwt(token);
        if(returnedJwt.isEmpty()) {
            return false;
        }
        var jwtModel = returnedJwt.get();
        jwtModel.setValid(false);
        jwtRepository.save(jwtModel);
        return true;
    }
}
