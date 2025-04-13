package org.thevoids.oncologic.controller.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.ErrorResponse;
import org.thevoids.oncologic.dto.UserResponseDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A {@link ResponseEntity} containing a list of {@link UserResponseDTO} objects.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOList = userList.stream()
                .map(this::convertToResponseDTO)
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
            return ResponseEntity.ok().body(convertToResponseDTO(createdUser));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }

    /**
     * Converts a {@link User} entity to a {@link UserResponseDTO} object.
     *
     * @param user The {@link User} entity to be converted.
     * @return A {@link UserResponseDTO} object containing the user's details.
     */
    public UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setIdentification(user.getIdentification());
        userResponseDTO.setFullName(user.getFullName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRoleName(user.getRole().getRoleName());
        return userResponseDTO;
    }
}