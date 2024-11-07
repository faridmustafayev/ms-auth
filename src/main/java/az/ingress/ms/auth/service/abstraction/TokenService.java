package az.ingress.ms.auth.service.abstraction;

import az.ingress.ms.auth.model.response.AuthPayloadResponse;
import az.ingress.ms.auth.model.response.TokenResponse;

public interface TokenService {
    TokenResponse generateToken(Long userId, int refreshTokenExpirationCount);
    TokenResponse refreshTokens(String refreshToken);
    AuthPayloadResponse validateToken(String accessToken);
}
