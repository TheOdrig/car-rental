package com.akif.auth.internal.oauth2;

import com.akif.auth.AuthResponse;
import com.akif.auth.LinkAccountResponse;

public interface IOAuth2AuthService {

    String getAuthorizationUrl(String provider);

    AuthResponse processOAuth2Callback(String provider, String code, String state);

    LinkAccountResponse linkSocialAccount(String provider, String code, String state, Long userId);
}
