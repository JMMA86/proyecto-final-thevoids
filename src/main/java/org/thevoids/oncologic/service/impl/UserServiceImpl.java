package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        if (getUserByIdentification(user.getIdentification()) != null) {
            throw new IllegalArgumentException("User with identification " + user.getIdentification() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(user);

        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User with id " + user.getUserId() + " does not exist");
        }

        this.userRepository.delete(user);
    }

    @Override
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User with id " + user.getUserId() + " does not exist");
        }

        this.userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByIdentification(String identification) {
        return userRepository.findByIdentification(identification).orElse(null);
    }
}
