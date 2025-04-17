package org.thevoids.oncologic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    public Long userId;
    public String identification;
    public String fullName;
    public String email;
    public String roleName;

    public UserResponseDTO(Long userId, String identification, String fullName, String email, String roleName) {
        this.userId = userId;
        this.identification = identification;
        this.fullName = fullName;
        this.email = email;
        this.roleName = roleName;
    }

    public UserResponseDTO() {
    }
}
