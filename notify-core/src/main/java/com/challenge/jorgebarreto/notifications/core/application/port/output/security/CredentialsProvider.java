package com.challenge.jorgebarreto.notifications.core.application.port.output.security;

public interface CredentialsProvider {
    String get(String key);
}