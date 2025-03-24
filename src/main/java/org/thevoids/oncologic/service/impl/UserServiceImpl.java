package org.thevoids.oncologic.service.impl;

import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User with id " + user.getUserId() + " already exists");
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException("User must have at least one role");
        }

        this.userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User with id " + user.getUserId() + " does not exist");
        }

        this.userRepository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
