package org.thevoids.oncologic.mapper;

import org.springframework.stereotype.Component;
import org.thevoids.oncologic.dto.entity.UserSpecialtyDTO;
import org.thevoids.oncologic.entity.UserSpecialty;

@Component
public class UserSpecialtyMapper {
    
    public UserSpecialtyDTO toUserSpecialtyDTO(UserSpecialty userSpecialty) {
        if (userSpecialty == null) {
            return null;
        }
        
        return new UserSpecialtyDTO(
            userSpecialty.getId(),
            userSpecialty.getUser().getUserId(),
            userSpecialty.getSpecialty().getSpecialtyId(),
            userSpecialty.getUser().getFullName(),
            userSpecialty.getSpecialty().getSpecialtyName()
        );
    }
} 