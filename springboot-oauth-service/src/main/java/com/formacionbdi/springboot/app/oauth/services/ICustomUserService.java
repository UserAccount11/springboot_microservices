package com.formacionbdi.springboot.app.oauth.services;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;

public interface ICustomUserService {

    CustomUser findByUsername(String username);
    CustomUser update(Long id, CustomUser user);

}
