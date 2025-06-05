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
    private Long userId;
    private List<RoleWithPermissionsDTO> roles;

    public AuthResponseDTO(String token, String username, Long userId, List<RoleWithPermissionsDTO> roles) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }

    public AuthResponseDTO(String token, String username, Long userId) {
        this.token = token;
        this.username = username;
        this.userId = userId;
    }
}