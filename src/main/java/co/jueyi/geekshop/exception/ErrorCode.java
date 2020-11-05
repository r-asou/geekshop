package co.jueyi.geekshop.exception;

/**
 * Created on Nov, 2020 by @author bobo
 */
public enum ErrorCode {
    PERMISSION_DENIED("You are not currently authorized to perform this action!"),

    INVALID_INPUT_PARAMETER("Invalid request input parameters"),

    INTERNAL_SERVER_ERROR("Unexpected internal server error, please report to our engineering team!"),

    UNAUTHORIZED("The credentials did not match. Please check and try again"),

    USER_INPUT_ERROR("Invalid request input parameters");

    public final String defaultMessage;

    ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}
