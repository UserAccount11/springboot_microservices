package com.formacionbdi.springboot.app.oauth.security.event;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;
import com.formacionbdi.springboot.app.oauth.services.ICustomUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
    private final ICustomUserService userService;

    public AuthenticationSuccessErrorHandler(ICustomUserService userService) {
        this.userService = userService;
    }

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
//        if (authentication.getName().equalsIgnoreCase("frontendapp")) {
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        logger.info("Success login: " + user.getUsername());

        CustomUser customUser = userService.findByUsername(authentication.getName());

        if (customUser.getTries() != null && customUser.getTries() > 0) {
            customUser.setTries(0);
            userService.update(customUser.getId(), customUser);
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {
        try {
            logger.error("Error in login: " + e.getMessage());

            CustomUser user = userService.findByUsername(authentication.getName());

            if (user.getTries() == null) {
                user.setTries(0);
            }

            user.setTries(user.getTries() + 1);

            logger.info("Number of failed attempts: " + user.getTries());

            if (user.getTries() >= 3) {
                user.setEnabled(false);
                logger.error(String.format("User %s disabled for maximum number of failed attempts.",
                        user.getUsername()));
            }

            userService.update(user.getId(), user);

        } catch (Exception exception) {
            logger.error(String.format("User %s not found in the system.", authentication.getName()));
        }
    }

}
