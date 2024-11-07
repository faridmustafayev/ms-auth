package az.ingress.ms.auth.service.abstraction;

import az.ingress.ms.auth.model.request.SignInRequest;
import az.ingress.ms.auth.model.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest request);

    void verifyToken(String accessToken);

    TokenResponse refreshTokens(String refreshToken);
}
