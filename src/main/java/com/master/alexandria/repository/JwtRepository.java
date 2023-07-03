package com.master.alexandria.repository;

import com.master.alexandria.common.model.JwtValid;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface JwtRepository extends CrudRepository<JwtValid, Integer> {

    Optional<JwtValid> findByJwt(String jwt);

    void deleteAllByValidOrValidUntilBefore(Boolean valid, Timestamp time);

}
