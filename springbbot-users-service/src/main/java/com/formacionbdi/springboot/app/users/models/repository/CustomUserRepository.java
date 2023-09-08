package com.formacionbdi.springboot.app.users.models.repository;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "rest-template")
public interface CustomUserRepository extends PagingAndSortingRepository<CustomUser, Long> {

    @RestResource(path = "by-username")
    CustomUser findByUsername(@Param("username") String username);

}
