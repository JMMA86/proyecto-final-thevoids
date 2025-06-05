package org.thevoids.oncologic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thevoids.oncologic.entity.UserSpecialty;

@Repository
public interface UserSpecialtyRepository extends JpaRepository<UserSpecialty, Long> {
    /**
     * Finds all user specialties for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of user specialties for the user
     */
    List<UserSpecialty> findByUser_UserId(Long userId);

    /**
     * Finds the first user specialty for a specific user.
     *
     * @param userId the ID of the user
     * @return an optional containing the first user specialty for the user
     */
    Optional<UserSpecialty> findFirstByUser_UserId(Long userId);
}