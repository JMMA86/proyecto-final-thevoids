package org.thevoids.oncologic.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        if (userRepository.findByIdentification(user.getIdentification()).isPresent()) {
            throw new ResourceAlreadyExistsException("Usuario", "identificación", user.getIdentification());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(user);

        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new ResourceNotFoundException("Usuario", "id", user.getUserId());
        }

        this.userRepository.delete(user);
    }

    @Override
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new ResourceNotFoundException("Usuario", "id", user.getUserId());
        }

        this.userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Override
    public User getUserByIdentification(String identification) {
        return userRepository.findByIdentification(identification)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "identificación", identification));
    }
}
