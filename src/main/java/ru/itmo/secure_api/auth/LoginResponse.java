package ru.itmo.secure_api.auth;

public record LoginResponse(String accessToken, String tokenType
) {
}
