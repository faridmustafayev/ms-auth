package az.ingress.ms.auth.constants;

public interface ExceptionConstant {
    String UNEXPECTED_EXCEPTION_CODE = "UNEXPECTED_EXCEPTION";
    String UNEXPECTED_EXCEPTION_MESSAGE = "Unexpected exception occurred!";

    String CLIENT_ERROR_CODE = "CLIENT_ERROR";
    String CLIENT_ERROR_MESSAGE = "Exception from Client";

    String USER_UNAUTHORIZED_CODE = "USER_UNAUTHORIZED_CODE";
    String USER_UNAUTHORIZED_MESSAGE = "User unauthorized message";

    String TOKEN_EXPIRED_CODE = "TOKEN_EXPIRED_CODE";
    String TOKEN_EXPIRED_Message = "Token expired code";

    String METHOD_NOT_ALLOWED_CODE = "METHOD_NOT_ALLOWED_CODE";
    String METHOD_NOT_ALLOWED_MESSAGE = "method of the HTTP does not enter correctly";

    String ACCESS_DENIED_CODE = "ACCESS_DENIED_CODE";
    String ACCESS_DENIED_MESSAGE = "User don't have access for this operation";
}
