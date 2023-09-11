package com.formacionbdi.springboot.app.oauth.security;

import com.formacionbdi.springboot.app.commons.users.models.entity.CustomUser;
import com.formacionbdi.springboot.app.oauth.services.ICustomUserService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdditionalInfoToken implements TokenEnhancer {

    private final ICustomUserService userService;

    public AdditionalInfoToken(ICustomUserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();
        CustomUser user = userService.findByUsername(oAuth2Authentication.getName());

        info.put("firstname", user.getFirstname());
        info.put("lastname", user.getLastname());
        info.put("email", user.getEmail());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }

}
