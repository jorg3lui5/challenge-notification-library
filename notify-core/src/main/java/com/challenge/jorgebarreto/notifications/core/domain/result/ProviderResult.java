package com.challenge.jorgebarreto.notifications.core.domain.result;

public record ProviderResult(
        boolean success,
        String errorCode,
        String message,
        Throwable cause
) {
    public static ProviderResult ok() {
        return new ProviderResult(true, null, "OK", null);
    }

    public static ProviderResult failure(String code, String message, Throwable cause) {
        return new ProviderResult(false, code, message, cause);
    }
}
