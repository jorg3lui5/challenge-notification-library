package com.challenge.jorgebarreto.notifications.core.infraestructure.adapter.output.security;

import com.challenge.jorgebarreto.notifications.core.application.port.output.security.CredentialsProvider;

public class EnvironmentCredentialsProvider implements CredentialsProvider {
    @Override
    public String get(String key) {
        return System.getenv(key);
    }
}
