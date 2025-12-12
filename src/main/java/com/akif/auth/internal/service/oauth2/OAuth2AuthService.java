package com.akif.auth.internal.service.oauth2;

import com.akif.auth.api.AuthResponse;
import com.akif.auth.internal.dto.oauth2.LinkAccountResponse;

public interface OAuth2AuthService {

    String getAuthorizationUrl(String provider);

    AuthResponse processOAuth2Callback(String provider, String code, String state);

    LinkAccountResponse linkSocialAccount(String provider, String code, String state, Long userId);
}
