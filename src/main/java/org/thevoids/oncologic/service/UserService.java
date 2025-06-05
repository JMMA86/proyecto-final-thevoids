package org.thevoids.oncologic.service;

import org.thevoids.oncologic.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User createUser(User user);

    void deleteUser(User user);

    void updateUser(User user);

    User getUserById(Long id);

    User getUserByIdentification(String identification);

    void changePassword(Long userId, String currentPassword, String newPassword);
}
