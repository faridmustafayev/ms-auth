package az.ingress.ms.auth.service.concrete;

import az.ingress.ms.auth.client.UserClient;
import az.ingress.ms.auth.model.client.UserResponseDto;
import az.ingress.ms.auth.model.request.SignInRequest;
import az.ingress.ms.auth.model.response.TokenResponse;
import az.ingress.ms.auth.service.abstraction.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceHandler implements AuthService {
    private final TokenServiceHandler tokenServiceHandler;
    private final UserClient userClient;

    @Override
    public TokenResponse signIn(SignInRequest request) {
        UserResponseDto userResponse = userClient.checkCredential(request.getUsername(), request.getPassword());
        return tokenServiceHandler.generateToken(userResponse.getId(), 50);
    }

    @Override
    public void verifyToken(String accessToken) {
        tokenServiceHandler.validateToken(accessToken);
    }

    @Override
    public TokenResponse refreshTokens(String refreshToken) {
        return tokenServiceHandler.refreshTokens(refreshToken);
    }
}
