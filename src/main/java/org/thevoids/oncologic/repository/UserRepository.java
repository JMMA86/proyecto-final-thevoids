package org.thevoids.oncologic.repository;

import org.thevoids.oncologic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NonNull
    List<User> findAll();

    @NonNull
    Optional<User> findByIdentification(@NonNull String identification);
}