package org.thevoids.oncologic.repository;

import org.thevoids.oncologic.entity.UserSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSpecialtyRepository extends JpaRepository<UserSpecialty, Long> {
}