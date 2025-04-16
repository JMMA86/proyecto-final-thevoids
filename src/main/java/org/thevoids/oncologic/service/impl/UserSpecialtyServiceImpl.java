package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.UserSpecialty;
import org.thevoids.oncologic.repository.UserSpecialtyRepository;
import org.thevoids.oncologic.service.UserSpecialtyService;

import java.util.List;

@Service
public class UserSpecialtyServiceImpl implements UserSpecialtyService {
    private final UserSpecialtyRepository userSpecialtyRepository;

    public UserSpecialtyServiceImpl(UserSpecialtyRepository userSpecialtyRepository) {
        this.userSpecialtyRepository = userSpecialtyRepository;
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

    @Override
    public UserSpecialty createUserSpecialty(UserSpecialty userSpecialty) {
        if (userSpecialty == null) {
            throw new IllegalArgumentException("UserSpecialty cannot be null");
        }

        return userSpecialtyRepository.save(userSpecialty);
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
}