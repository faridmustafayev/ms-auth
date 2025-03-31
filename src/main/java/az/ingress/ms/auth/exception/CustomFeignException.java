package az.ingress.ms.auth.exception;

import lombok.Getter;

@Getter
public class CustomFeignException extends RuntimeException {
    private final String code;
    private final int status;

    public CustomFeignException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
