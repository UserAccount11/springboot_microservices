package com.formacionbdi.springboot.app.oauth.services;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;
import com.formacionbdi.springboot.app.oauth.clients.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserService implements UserDetailsService, ICustomUserService {

    private final Logger log = LoggerFactory.getLogger(CustomUserService.class);
    private final UserFeignClient client;

    public CustomUserService(UserFeignClient client) {
        this.client = client;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUser user = client.findByUsername(username);

        if (user == null) {
            String msg = String.format("Error in login, user %s not found.", username);
            log.error(msg);
            throw new UsernameNotFoundException(msg);
        }

        log.info("Authenticated user: " + username);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .peek(authority -> log.info("Role: " + authority.getAuthority()))
                .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), user.getEnabled(),
                true, true, true, authorities);
    }

    @Override
    public CustomUser findByUsername(String username) {
        return client.findByUsername(username);
    }

}
