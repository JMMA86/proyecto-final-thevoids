package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.UserSpecialty;

import java.util.List;

public interface UserSpecialtyService {
    List<UserSpecialty> getAllUserSpecialties();

    UserSpecialty getUserSpecialtyById(Long id);

    UserSpecialty createUserSpecialty(UserSpecialty userSpecialty);

    UserSpecialty updateUserSpecialty(UserSpecialty userSpecialty);

    void deleteUserSpecialty(Long id);
}
