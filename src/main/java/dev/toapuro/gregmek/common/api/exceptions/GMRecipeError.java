package dev.toapuro.gregmek.common.api.exceptions;

import java.util.function.Function;

public class GMRecipeError extends RuntimeException {
    public static final GMRecipeError INPUT_NULL = createError("Input cannot be null.");
    public static final GMRecipeError OUTPUT_NULL = createError("Output cannot be null.");
    public static final Function<String, GMRecipeError> FACTORY_NULL = string -> createError(String.format("%s cannot be null.", string));
    public static final GMRecipeError INPUT_EMPTY = createError("At least one input must not be empty.");
    public static final GMRecipeError OUTPUT_EMPTY = createError("At least one output must not be empty.");
    public static final Function<String, GMRecipeError> FACTORY_EMPTY = string -> createError(String.format("%s must not be empty.", string));
    public static final GMRecipeError DURATION_NOT_NUMBER = createError("Expected duration to be a number.");
    public static final GMRecipeError DURATION_NOT_POSITIVE = createError("Expected duration to be a number greater than zero.");
    public static final GMRecipeError ENUM_ORDINAL_NOT_POSITIVE = createError("Expected enum ordinal to be a number greater than zero.");
    public static final GMRecipeError ENUM_ORDINAL_OUT_RANGE = createError("Enum ordinal out of range.");
    public static final GMRecipeError ENUM_UNKNOWN = createError("Unknown enum.");
    public static final GMRecipeError UNKNOWN_TYPE = createError("Unknown type of json element.");

    private GMRecipeError(String message, Throwable cause) {
        super(message, cause);
    }

    private GMRecipeError() {
    }

    private GMRecipeError(String message) {
        super(message);
    }

    private GMRecipeError(Throwable cause) {
        super(cause);
    }

    private GMRecipeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private static GMRecipeError createError(String message) {
        return new GMRecipeError(message);
    }

    @Deprecated
    public static GMRecipeError createTempError(String message) {
        return new GMRecipeError(message);
    }
}
