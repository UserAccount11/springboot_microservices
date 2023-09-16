package com.formacionbdi.springboot.app.oauth.clients;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users-service")
public interface UserFeignClient {

    @GetMapping("/rest-template/search/by-username")
    CustomUser findByUsername(@RequestParam String username);

    @PutMapping("/rest-template/{id}")
    CustomUser update(@PathVariable Long id, @RequestBody CustomUser user);

}
