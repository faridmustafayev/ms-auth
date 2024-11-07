package az.ingress.ms.auth.controller;

import az.ingress.ms.auth.model.request.SignInRequest;
import az.ingress.ms.auth.model.response.TokenResponse;
import az.ingress.ms.auth.service.abstraction.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public TokenResponse signIn(@RequestBody SignInRequest request) {
        return authService.signIn(request);
    }

    @PostMapping("/verify")
    public void verifyToken(String accessToken) {
        authService.verifyToken(accessToken);
    }

    @PostMapping("/refresh")
    public TokenResponse refreshToken(String refreshToken) {
        return authService.refreshTokens(refreshToken);
    }

}
