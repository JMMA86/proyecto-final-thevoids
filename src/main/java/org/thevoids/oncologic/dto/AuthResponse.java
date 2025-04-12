package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String accessToken;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
