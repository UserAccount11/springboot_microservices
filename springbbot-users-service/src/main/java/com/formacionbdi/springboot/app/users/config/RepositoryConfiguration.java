package com.formacionbdi.springboot.app.users.config;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;
import com.formacionbdi.springboot.app.commons.users.models.entity.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(CustomUser.class, Role.class);
    }

}
