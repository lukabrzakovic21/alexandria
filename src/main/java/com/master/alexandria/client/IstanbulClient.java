package com.master.alexandria.client;

import com.master.alexandria.common.dto.AuthRequestDTO;
import com.master.alexandria.common.util.LoginPair;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "istanbul", url = "http://localhost:8081")
public interface IstanbulClient {

        @RequestMapping(method = RequestMethod.POST, value = "/user/login", produces = "application/json", consumes = "application/json")
        LoginPair authenticateUser(@RequestBody AuthRequestDTO userDTO);
}
