package org.thevoids.oncologic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.repository.UserRepository;
import org.thevoids.oncologic.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList;
    }
}
