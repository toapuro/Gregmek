package dev.toapuro.gregmek.common.api.exceptions;

public class GMGenericError extends RuntimeException {

    public GMGenericError(String message, Throwable cause) {
        super(message, cause);
    }

    public GMGenericError() {
    }

    public GMGenericError(String message) {
        super(message);
    }

    public GMGenericError(Throwable cause) {
        super(cause);
    }

    public GMGenericError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static GMGenericError createError(String message) {
        return new GMGenericError(message);
    }
}
