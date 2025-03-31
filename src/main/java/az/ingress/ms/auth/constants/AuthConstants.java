package az.ingress.ms.auth.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AuthConstants {
    public static final String ISSUER = "spring-school-student";
    public static final String RSA = "RSA";
    public static final int KEY_SIZE = 2048;
    public static final String AUTH_CACHE_DATA_PREFIX = "spring-school-student:";
    public static final Long TOKEN_EXPIRE_DAY_COUNT = 1L;
}
