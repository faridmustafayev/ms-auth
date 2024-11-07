package az.ingress.ms.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ConfigurationProperties {
    @Value("${jwt.accessToken.expiration.time}")
    private int accessTokenExpirationTime;

    @Value("${jwt.refreshToken.expiration.time}")
    private int refreshTokenExpirationTime;
}
