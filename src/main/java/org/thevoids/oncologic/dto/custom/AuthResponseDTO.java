package org.thevoids.oncologic.dto.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.thevoids.oncologic.dto.entity.RoleWithPermissionsDTO;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    private List<RoleWithPermissionsDTO> roles;
    
    public AuthResponseDTO(String token, String username, List<RoleWithPermissionsDTO> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public AuthResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }
}