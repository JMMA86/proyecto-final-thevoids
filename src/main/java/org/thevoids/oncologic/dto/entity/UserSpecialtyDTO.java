package org.thevoids.oncologic.dto.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSpecialtyDTO {
    private Long id;
    private Long userId;
    private Long specialtyId;
    private String userName;
    private String specialtyName;

    public UserSpecialtyDTO(Long id, Long userId, Long specialtyId, String userName, String specialtyName) {
        this.id = id;
        this.userId = userId;
        this.specialtyId = specialtyId;
        this.userName = userName;
        this.specialtyName = specialtyName;
    }

    public UserSpecialtyDTO() {
    }
} 