package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.UserSpecialty;

import java.util.List;

public interface UserSpecialtyService {
    void addSpecialtyToUser(Long userId, Long specialtyId);

    List<UserSpecialty> getAllUserSpecialties();

    UserSpecialty getUserSpecialtyById(Long id);

    UserSpecialty updateUserSpecialty(UserSpecialty userSpecialty);

    void deleteUserSpecialty(Long id);


}
