package az.ingress.ms.auth.util;

import az.ingress.ms.auth.constants.AuthConstants;
import az.ingress.ms.auth.exception.AuthException;
import az.ingress.ms.auth.jwt.RefreshTokenClaimSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

import static az.ingress.ms.auth.constants.ExceptionConstant.USER_UNAUTHORIZED_CODE;
import static az.ingress.ms.auth.constants.ExceptionConstant.USER_UNAUTHORIZED_MESSAGE;
import static com.nimbusds.jose.JWSAlgorithm.RS256;

@Component
@Slf4j
public class JwtUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Date generateSessionExpirationTime(Integer expirationMinutes) {
        return new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1_000);
    }

    public KeyPair generateKeyPair() {

        try {
            var keyPairGen = KeyPairGenerator.getInstance(AuthConstants.RSA);
            keyPairGen.initialize(AuthConstants.KEY_SIZE);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            log.error("ActionLog.generateKeyPair.error no such algorithm", ex);
            throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
        }
    }


    public <T> T getClaimsFromToken(String token, Class<T> clazz) {
        try {
            var jwtClaimSet = parseJwtClaimSet(token);
            return MapperUtil.MAPPER_UTIL.map(jwtClaimSet.toString(), clazz);
        } catch (Exception ex) {
            log.error("ActionLog.getClaimsFromAccessToken.error ", ex);
            throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
        }
    }

    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }

    public void verifyToken(String token, RSAPublicKey publicKey) {

        try {
            var signedJWT = SignedJWT.parse(token);
            var verifier = new RSASSAVerifier(publicKey);

            if (!signedJWT.verify(verifier)) {
                log.error("ActionLog.verifyToken.error can not verify signedJWT ");
                throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
            }
        } catch (ParseException | JOSEException ex) {
            log.error("ActionLog.verifyToken.error can not parse token ", ex);
            throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
        }

    }

    @SneakyThrows
    private JWTClaimsSet parseJwtClaimSet(String token) {
        var signedJWT = parseSignedJwt(token);
        return signedJWT.getJWTClaimsSet();
    }

    @SneakyThrows
    private SignedJWT parseSignedJwt(String token) {
        return SignedJWT.parse(token);
    }


    public <T> String generateToken(T tokenClaimSet, PrivateKey privateKey) {

        SignedJWT signedJWT;

        try {
            signedJWT = generateSignedJWT(objectMapper.writeValueAsString(tokenClaimSet), privateKey);
        } catch (Exception ex) {
            log.error("ActionLog.generateToken.error cannot generate token", ex);
            throw new AuthException(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE, 401);
        }

        return signedJWT.serialize();
    }

    @SneakyThrows
    private SignedJWT generateSignedJWT(String tokenClaimSetJson, PrivateKey privateKey) {
        var jwtClaimsSet = JWTClaimsSet.parse(tokenClaimSetJson);
        var header = new JWSHeader(RS256);
        var signedJWT = new SignedJWT(header, jwtClaimsSet);
        var signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);
        return signedJWT;
    }

    public boolean isRefreshTokenTimeExpired(RefreshTokenClaimSet refreshTokenClaimsSet) {
        return refreshTokenClaimsSet.getExp().before(new Date());
    }

    public boolean isRefreshTokenCountExpired(RefreshTokenClaimSet refreshTokenClaimsSet) {
        return refreshTokenClaimsSet.getCount() <= 0;
    }
}
