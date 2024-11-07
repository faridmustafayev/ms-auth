package az.ingress.ms.auth.exception;

import az.ingress.ms.auth.constants.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static az.ingress.ms.auth.constants.ExceptionConstant.ACCESS_DENIED_CODE;
import static az.ingress.ms.auth.constants.ExceptionConstant.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception exception) {
        log.error("Exception ", exception);
        return new ErrorResponse(ExceptionConstant.UNEXPECTED_EXCEPTION_CODE, ExceptionConstant.UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException, ", ex);
        return new ErrorResponse(ExceptionConstant.METHOD_NOT_ALLOWED_CODE, ExceptionConstant.METHOD_NOT_ALLOWED_MESSAGE);
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<ErrorResponse> handle(CustomFeignException ex) {
        log.error("CustomFeignException, ", ex);
        return ResponseEntity.status(ex.getStatus()).body(
                ErrorResponse.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handle(AuthException ex) {
        log.error("AuthException, ", ex);
        return ResponseEntity.status(ex.getHttpStatusCode()).body(
                ErrorResponse.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handle(AccessDeniedException ex) {
        log.error("AccessDeniedException, ", ex);
        return new ErrorResponse(ACCESS_DENIED_CODE, ACCESS_DENIED_MESSAGE);
    }

}
