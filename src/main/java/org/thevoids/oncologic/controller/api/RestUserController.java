package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.UserResponseDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A {@link ResponseEntity} containing a list of {@link UserResponseDTO} objects.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOList = userList.stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOList);
    }

    /**
     * Registers a new user in the system.
     *
     * @param user The {@link User} object containing the user's details.
     * @return A {@link ResponseEntity} containing the created {@link UserResponseDTO} object or an error response.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            UserResponseDTO userResponseDTO = userMapper.toUserResponseDTO(createdUser);
            return ResponseEntity.ok(userResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}