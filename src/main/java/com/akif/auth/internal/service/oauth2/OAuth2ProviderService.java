package com.akif.auth.internal.service.oauth2;

import com.akif.auth.internal.dto.oauth2.OAuth2UserInfo;
import com.akif.auth.internal.dto.oauth2.OAuth2TokenResponse;

public interface OAuth2ProviderService {

    OAuth2TokenResponse exchangeCodeForTokens(String code);

    OAuth2UserInfo getUserInfo(String accessToken);

    String getProviderName();
}
