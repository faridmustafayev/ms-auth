package az.ingress.ms.auth.service.concrete;

import az.ingress.ms.auth.cache.AuthCacheData;
import az.ingress.ms.auth.config.ConfigurationProperties;
import az.ingress.ms.auth.constants.AuthConstants;
import az.ingress.ms.auth.exception.AuthException;
import az.ingress.ms.auth.jwt.AccessTokenClaimSet;
import az.ingress.ms.auth.jwt.RefreshTokenClaimSet;
import az.ingress.ms.auth.model.response.AuthPayloadResponse;
import az.ingress.ms.auth.model.response.TokenResponse;
import az.ingress.ms.auth.service.abstraction.TokenService;
import az.ingress.ms.auth.util.CacheUtil;
import az.ingress.ms.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import static az.ingress.ms.auth.constants.ExceptionConstant.TOKEN_EXPIRED_CODE;
import static az.ingress.ms.auth.constants.ExceptionConstant.TOKEN_EXPIRED_Message;
import static az.ingress.ms.auth.constants.ExceptionConstant.USER_UNAUTHORIZED_CODE;
import static az.ingress.ms.auth.constants.ExceptionConstant.USER_UNAUTHORIZED_MESSAGE;
import static az.ingress.ms.auth.mapper.TokenMapper.buildAccessTokenClaimSet;
import static az.ingress.ms.auth.mapper.TokenMapper.buildRefreshTokenClaimSet;
import static az.ingress.ms.auth.util.CertificateKeyUtil.CERTIFICATE_KEY_UTIL;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceHandler implements TokenService {
    private final JwtUtil jwtUtil;
    private final CacheUtil cacheUtil;
    private final ConfigurationProperties configurationProperties;

    @Override
    public TokenResponse generateToken(Long userId, int refreshTokenExpirationCount) {

        var accessTokenClaimSet = buildAccessTokenClaimSet(
                userId,
                jwtUtil.generateSessionExpirationTime(configurationProperties.getAccessTokenExpirationTime())
        );

        var refreshTokenClaimSet = buildRefreshTokenClaimSet(
                userId,
                refreshTokenExpirationCount,
                jwtUtil.generateSessionExpirationTime(configurationProperties.getRefreshTokenExpirationTime())
        );

        var keyPair = jwtUtil.generateKeyPair();

        var authCacheData = AuthCacheData.of(
                accessTokenClaimSet,
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())
        );

        cacheUtil.saveToCache(AuthConstants.AUTH_CACHE_DATA_PREFIX + userId, authCacheData, AuthConstants.TOKEN_EXPIRE_DAY_COUNT, ChronoUnit.DAYS);

        var accessToken = jwtUtil.generateToken(accessTokenClaimSet, keyPair.getPrivate());
        var refreshToken = jwtUtil.generateToken(refreshTokenClaimSet, keyPair.getPrivate());

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Override
    public TokenResponse refreshTokens(String refreshToken) {
        var refreshTokenClaimsSet = jwtUtil.getClaimsFromToken(refreshToken, RefreshTokenClaimSet.class);
        var refreshTokenExpirationCount = refreshTokenClaimsSet.getCount() - 1;
        var userId = refreshTokenClaimsSet.getUserId();

        try {
            AuthCacheData authCacheData = cacheUtil.getBucket(AuthConstants.AUTH_CACHE_DATA_PREFIX + userId);

            if (authCacheData == null) throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);

            var publicKey = CERTIFICATE_KEY_UTIL.getPublicKey(authCacheData.getPublicKey());

            jwtUtil.verifyToken(refreshToken, (RSAPublicKey) publicKey);

            if (jwtUtil.isRefreshTokenTimeExpired(refreshTokenClaimsSet)) {
                throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
            }

            if (jwtUtil.isRefreshTokenCountExpired(refreshTokenClaimsSet)) {
                throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
            }

            return generateToken(userId, refreshTokenExpirationCount);

        } catch (AuthException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
        }
    }

    @Override
    public AuthPayloadResponse validateToken(String accessToken) {

        try {
            var accessTokenClaimsSet = jwtUtil.getClaimsFromToken(accessToken, AccessTokenClaimSet.class);
            var userId = accessTokenClaimsSet.getUserId();

            AuthCacheData authCacheData = cacheUtil.getBucket(AuthConstants.AUTH_CACHE_DATA_PREFIX + userId);

            if (authCacheData == null) throw new AuthException(TOKEN_EXPIRED_CODE, TOKEN_EXPIRED_Message, 406);

            var publicKey = CERTIFICATE_KEY_UTIL.getPublicKey(authCacheData.getPublicKey());

            jwtUtil.verifyToken(accessToken, (RSAPublicKey) publicKey);

            if (jwtUtil.isTokenExpired(authCacheData.getAccessTokenClaimSet().getExpirationTime())) {
                throw new AuthException(TOKEN_EXPIRED_CODE, TOKEN_EXPIRED_Message, 406);
            }

            return AuthPayloadResponse.of(userId);
        } catch (AuthException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
            throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
        }

    }

}