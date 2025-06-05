package org.thevoids.oncologic.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.Specialty;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.entity.UserSpecialty;
import org.thevoids.oncologic.repository.SpecialtyRepository;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.repository.UserSpecialtyRepository;
import org.thevoids.oncologic.service.UserSpecialtyService;

import jakarta.transaction.Transactional;

@Service
public class UserSpecialtyServiceImpl implements UserSpecialtyService {
    private final UserSpecialtyRepository userSpecialtyRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;

    public UserSpecialtyServiceImpl(
            UserSpecialtyRepository userSpecialtyRepository,
            UserRepository userRepository,
            SpecialtyRepository specialtyRepository) {
        this.userSpecialtyRepository = userSpecialtyRepository;
        this.userRepository = userRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<UserSpecialty> getAllUserSpecialties() {
        return userSpecialtyRepository.findAll();
    }

    @Override
    public UserSpecialty getUserSpecialtyById(Long id) {
        return userSpecialtyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserSpecialty with id " + id + " does not exist"));
    }

    @Transactional
    @Override
    public void addSpecialtyToUser(Long userId, Long specialtyId) {
        if (userId == null || specialtyId == null) {
            throw new IllegalArgumentException("User ID and Specialty ID cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not exist"));

        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Specialty with id " + specialtyId + " does not exist"));

        UserSpecialty userSpecialty = new UserSpecialty();
        userSpecialty.setUser(user);
        userSpecialty.setSpecialty(specialty);

        userSpecialtyRepository.save(userSpecialty);
    }

    @Override
    public UserSpecialty updateUserSpecialty(UserSpecialty userSpecialty) {
        if (userSpecialty == null) {
            throw new IllegalArgumentException("UserSpecialty cannot be null");
        }

        if (userSpecialty.getId() == null) {
            throw new IllegalArgumentException("UserSpecialty ID cannot be null");
        }

        if (!userSpecialtyRepository.existsById(userSpecialty.getId())) {
            throw new IllegalArgumentException("UserSpecialty with id " + userSpecialty.getId() + " does not exist");
        }

        return userSpecialtyRepository.save(userSpecialty);
    }

    @Override
    public void deleteUserSpecialty(Long id) {
        if (!userSpecialtyRepository.existsById(id)) {
            throw new IllegalArgumentException("UserSpecialty with id " + id + " does not exist");
        }

        userSpecialtyRepository.deleteById(id);
    }

    @Override
    public List<UserSpecialty> getUserSpecialtiesByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userSpecialtyRepository.findByUser_UserId(userId);
    }

    @Override
    public Optional<UserSpecialty> getUserSpecialtyByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userSpecialtyRepository.findFirstByUser_UserId(userId);
    }
}