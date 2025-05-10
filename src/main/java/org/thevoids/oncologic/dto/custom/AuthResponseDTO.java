package org.thevoids.oncologic.dto.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    
    public AuthResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }
} 