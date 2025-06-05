package org.thevoids.oncologic.service;

import java.util.List;
import java.util.Optional;

import org.thevoids.oncologic.entity.UserSpecialty;

public interface UserSpecialtyService {
    void addSpecialtyToUser(Long userId, Long specialtyId);

    List<UserSpecialty> getAllUserSpecialties();

    UserSpecialty getUserSpecialtyById(Long id);

    UserSpecialty updateUserSpecialty(UserSpecialty userSpecialty);

    void deleteUserSpecialty(Long id);

    /**
     * Gets user specialties by user ID.
     *
     * @param userId the ID of the user
     * @return a list of user specialties for the user
     */
    List<UserSpecialty> getUserSpecialtiesByUserId(Long userId);

    /**
     * Gets the first user specialty by user ID.
     *
     * @param userId the ID of the user
     * @return an optional containing the first user specialty for the user
     */
    Optional<UserSpecialty> getUserSpecialtyByUserId(Long userId);
}
