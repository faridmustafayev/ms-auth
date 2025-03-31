package az.ingress.ms.auth.mapper;

import az.ingress.ms.auth.jwt.AccessTokenClaimSet;
import az.ingress.ms.auth.jwt.RefreshTokenClaimSet;
import lombok.NoArgsConstructor;

import java.util.Date;

import static az.ingress.ms.auth.constants.AuthConstants.ISSUER;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public enum TokenMapper {
    TOKEN_MAPPER;

    public static AccessTokenClaimSet buildAccessTokenClaimSet(Long userId, Date expirationTime) {
        return AccessTokenClaimSet.builder()
                .iss(ISSUER)
                .createdTime(new Date())
                .expirationTime(expirationTime)
                .userId(userId)
                .build();
    }

    public static RefreshTokenClaimSet buildRefreshTokenClaimSet(Long userId, int refreshTokenExpirationCount, Date expirationTime) {
        return RefreshTokenClaimSet.builder()
                .iss(ISSUER)
                .count(refreshTokenExpirationCount)
                .userId(userId)
                .exp(expirationTime)
                .build();
    }

}
